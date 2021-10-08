package com.hq.remview.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hq.remview.BR;
import com.hq.remview.BuildConfig;
import com.hq.remview.R;
import com.hq.remview.data.model.api.ApiModelUtils;
import com.hq.remview.data.model.api.Command;
import com.hq.remview.data.model.api.ResponseCode;
import com.hq.remview.data.model.api.request.WebQRCodeRequest;
import com.hq.remview.data.model.api.request.NavigationRequest;
import com.hq.remview.data.model.api.request.ToggleCalendarRequest;
import com.hq.remview.data.model.api.request.VerifyQRCodeRequest;
import com.hq.remview.data.model.api.request.VerifyTokenRequest;
import com.hq.remview.data.model.api.response.BaseSocketResponse;
import com.hq.remview.data.model.api.response.VerifyQRCodeResponse;
import com.hq.remview.data.model.db.RestaurantEntity;
import com.hq.remview.databinding.ActivityMainBinding;
import com.hq.remview.di.component.ActivityComponent;
import com.hq.remview.ui.base.activity.BaseActivity;
import com.hq.remview.ui.base.activity.BaseViewModel;
import com.hq.remview.ui.base.adapter.OnItemClickListener;
import com.hq.remview.ui.main.adapter.MainAdapter;
import com.hq.remview.ui.main.adapter.MainItem;
import com.hq.remview.ui.main.scanner.CustomScanner;
import com.hq.remview.ui.main.webview.WebViewInterface;
import com.hq.remview.ui.main.webview.WebViewPath;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener, WebViewInterface.WebViewInterfaceHandler {
    private static final int REQUEST_OPEN_SCAN_QRCODE = 2309;
    private static final int REQUEST_OPEN_SCAN_QRCODE_FROM_PC = 2139;
    private WebViewInterface webViewInterface;
    private List<MainItem> mainItems = new ArrayList<>();
    private MainAdapter mainAdapter;
    private Context context = this;
    // receive error flag
    private boolean loadSuccess = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setMainActivity(this);
        // setting restaurant page
        viewModel.restaurantSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get()) {
                    viewBinding.restaurantSelect.setVisibility(View.VISIBLE);
                    viewBinding.restaurantNotSelect.setVisibility(View.GONE);
                    viewBinding.mainAppBarBack.setVisibility(View.VISIBLE);
                    viewBinding.toolbarTitle.setText(R.string.sales_report);
                    hideScanQrCode();
                } else {
                    viewBinding.restaurantSelect.setVisibility(View.GONE);
                    viewBinding.restaurantNotSelect.setVisibility(View.VISIBLE);
                    viewBinding.mainAppBarBack.setVisibility(View.INVISIBLE);
                    viewBinding.toolbarTitle.setText(R.string.restaurant_list);
                    showScanQrCode();
                }
            }
        });
        // setting details page
        viewModel.detailSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get()) {
                    viewBinding.toolbarTitle.setText(R.string.bill_detail);
                    hideCalendar();
                } else {
                    viewBinding.toolbarTitle.setText(R.string.sales_report);
                    viewBinding.options.setVisibility(View.VISIBLE);
                    showCalendar();
                }
            }
        });

        mainAdapter = new MainAdapter(mainItems);
        mainAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                viewModel.currentRestaurant = mainItems.get(position).getEntity();
                verifyToken();
                Timber.d("Click");
            }

            @Override
            public void onItemDelete(int position) {
                showWarningDelete(position);
            }
        });
        viewBinding.restaurantRecyclerView.setAdapter(mainAdapter);

        // init recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        viewBinding.restaurantRecyclerView.setLayoutManager(linearLayoutManager);
        viewModel.restaurantEntityLiveData.observe(this, restaurantEntities -> {
            mainItems.clear();
            for (RestaurantEntity entity : restaurantEntities) {
                mainItems.add(new MainItem(entity));
            }
            if (mainItems.isEmpty()) {
                viewBinding.withoutRestaurant.setVisibility(View.VISIBLE);
                viewBinding.withRestaurant.setVisibility(View.GONE);

            } else {
                viewBinding.withoutRestaurant.setVisibility(View.GONE);
                viewBinding.withRestaurant.setVisibility(View.VISIBLE);
            }
            mainAdapter.notifyDataSetChanged();
        });
        // init web view
        viewModel.showLoading();
        viewBinding.mainWebView.getSettings().setDomStorageEnabled(true);
        viewBinding.mainWebView.getSettings().setJavaScriptEnabled(true);
        viewBinding.mainWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        viewBinding.mainWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                viewModel.hideLoading();
                // unlock retry btn
                viewBinding.retryBtn.setClickable(true);
                if (loadSuccess){
                    viewModel.connection.set(true);
                    loadSuccess = true;
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                viewModel.connection.set(false);
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.can_not_connect_to_server));
                // unlock retry btn
                viewBinding.retryBtn.setClickable(true);
                loadSuccess = false;
            }
        });

        webViewInterface = new WebViewInterface();
        webViewInterface.setWebviewInterfaceHandler(this);
        viewBinding.mainWebView.addJavascriptInterface(webViewInterface, "NodeJS");
        viewBinding.mainWebView.loadUrl(BuildConfig.BASE_URL);


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getBindingVariable() {
        return BR.mainViewModel;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void showWarningDelete(final int pos) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.main_dialog_title)
                .setMessage(R.string.main_dialog_message)
                .setPositiveButton(R.string.main_dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        viewModel.deleteRestaurant(mainItems.get(pos).getEntity());
                        mainAdapter.current = null;
                    }
                })
                .setNegativeButton(R.string.main_dialog_cancel, null)
                .show();
    }

    public void openScanQRCode(int requestOpenScanQrcode) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScanner.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setPrompt("");
        integrator.setBeepEnabled(false);
        integrator.setRequestCode(requestOpenScanQrcode);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        if (requestCode == REQUEST_OPEN_SCAN_QRCODE) {
            Timber.d("Handle request from mobile");
            handleQRCode(result);
        } else if (requestCode == REQUEST_OPEN_SCAN_QRCODE_FROM_PC) {
            Timber.d("Handle request from pc");
            handleQRCodeFromPC(result);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void handleQRCodeFromPC(IntentResult result){
        String qrCode = result.getContents();
        viewModel.showSuccessMessage(getString(R.string.scan_qr_success));
        sendQRCodeToWebView(qrCode);
    }
    private void sendQRCodeToWebView(String qrCode ){
        Timber.d("Send to qrcode pc: %s", qrCode);
        WebQRCodeRequest webQRCodeRequest = new WebQRCodeRequest();
        webQRCodeRequest.setQrCode(qrCode);
        viewBinding.mainWebView.post(() -> viewBinding.mainWebView.evaluateJavascript(
                " callback(" + Command.CMD_VERIFY_QRCODE_FOR_WEB + ",'','" + ApiModelUtils.toJson(webQRCodeRequest) + "')", null));

    }
    private void handleQRCode(IntentResult result){
        if (result.getContents() == null) {
            viewModel.showErrorMessage(getString(R.string.scan_qr_code_cancelled));
        } else {
            String qrCode = result.getContents();

            try {
                String id = qrCode.substring(qrCode.indexOf("::") + 2, qrCode.lastIndexOf("::"));
                String name = qrCode.substring(qrCode.lastIndexOf("::") + 2);
                String doSend = qrCode.substring(0, qrCode.lastIndexOf("::"));
                viewModel.currentRestaurant = new RestaurantEntity();
                viewModel.currentRestaurant.setLastAccessDate(new Date());
                viewModel.currentRestaurant.setId(id);
                viewModel.currentRestaurant.setName(name);
                doVerifyQRCode(doSend);
            } catch (IndexOutOfBoundsException e) {
                viewModel.showErrorMessage(getString(R.string.qr_code_wrong_format));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_app_bar_qr:
                scanQrCode();
                break;
            case R.id.scan_qr_code:
                scanQrCode();
                break;
            case R.id.main_app_bar_back:
                if (viewModel.detailSelected.get()) {
                    viewModel.detailSelected.set(false);
                    viewBinding.mainWebView.goBack();
                } else {
                    navigateToUrl("/");
                    viewModel.setRestaurantSelected(false);
                    viewBinding.list.setImageResource(R.drawable.list_selected);
                    viewBinding.note.setImageResource(R.drawable.note);
                    viewBinding.statistic.setImageResource(R.drawable.statistic);
                }
                break;
            case R.id.main_app_bar_calendar:
                mainAdapter.closeCurrent();
                toggleCalender("true");
                Timber.d("Show calendar");
                break;
            case R.id.list:
                viewBinding.list.setImageResource(R.drawable.list_selected);
                viewBinding.note.setImageResource(R.drawable.note);
                viewBinding.statistic.setImageResource(R.drawable.statistic);
                navigateToUrl(WebViewPath.REVENUE);
                viewBinding.toolbarTitle.setText(R.string.sales_report);
                showCalendar();
                break;
            case R.id.note:
                if (viewModel.detailSelected.get()) {
                    viewModel.detailSelected.set(false);
                }
                viewBinding.list.setImageResource(R.drawable.list);
                viewBinding.note.setImageResource(R.drawable.note_selected);
                viewBinding.statistic.setImageResource(R.drawable.statistic);
                navigateToUrl(WebViewPath.FOOD_HISTORY);
                viewBinding.toolbarTitle.setText(R.string.food_report);
                showCalendar();
                break;
            case R.id.statistic:
                if (viewModel.detailSelected.get()) {
                    viewModel.detailSelected.set(false);
                }
                viewBinding.list.setImageResource(R.drawable.list);
                viewBinding.note.setImageResource(R.drawable.note);
                viewBinding.statistic.setImageResource(R.drawable.statistic_selected);
                hideCalendar();
                navigateToUrl(WebViewPath.REPORT);
                viewBinding.toolbarTitle.setText(R.string.statistic_report);
                break;
            case R.id.retry_btn:
                // lock click btb
                viewBinding.mainWebView.reload();
                viewModel.showLoading();
                Timber.d("retry");
                loadSuccess = true;
                viewBinding.retryBtn.setClickable(false);
                break;
            case R.id.pc_qrcode:
                openScanQRCode(REQUEST_OPEN_SCAN_QRCODE_FROM_PC);
                break;
            default:
                break;
        }
    }

    private void hideScanQrCode() {
        viewBinding.mainAppBarQr.setVisibility(View.INVISIBLE);
        viewBinding.mainAppBarCalendar.setVisibility(View.VISIBLE);
    }

    private void showScanQrCode() {
        viewBinding.mainAppBarQr.setVisibility(View.VISIBLE);
        viewBinding.mainAppBarCalendar.setVisibility(View.INVISIBLE);
    }

    private void hideCalendar() {
        viewBinding.mainAppBarCalendar.setVisibility(View.INVISIBLE);
        viewBinding.mainAppBarCalendar.setClickable(false);
    }

    private void showCalendar() {
        viewBinding.mainAppBarCalendar.setVisibility(View.VISIBLE);
        viewBinding.mainAppBarCalendar.setClickable(true);
    }

    private void scanQrCode() {
        mainAdapter.closeCurrent();
        openScanQRCode(REQUEST_OPEN_SCAN_QRCODE);
    }

    private void doVerifyQRCode(String message) {
        Timber.d("Send to nodejs%s", message);
        VerifyQRCodeRequest verifyQRCodeRequest = new VerifyQRCodeRequest();
        verifyQRCodeRequest.setDeviceId(deviceId);
        verifyQRCodeRequest.setQrcode(message);
        viewBinding.mainWebView.post(() -> viewBinding.mainWebView.evaluateJavascript(
                " callback(" + Command.CMD_VERIFY_QRCODE + ",'','" + ApiModelUtils.toJson(verifyQRCodeRequest) + "')", null));

    }

    private void navigateToUrl(String url) {
        NavigationRequest navigationRequest = new NavigationRequest();
        navigationRequest.setPath(url);
        viewBinding.mainWebView.post(() -> viewBinding.mainWebView.evaluateJavascript(
                " callback(" + Command.CMD_NAVIGATE + ",'','" + ApiModelUtils.toJson(navigationRequest) + "');"
                , null
        ));
    }

    private void toggleCalender(String show) {
        ToggleCalendarRequest toggleCalendarRequest = new ToggleCalendarRequest();
        toggleCalendarRequest.setShow(show);
        viewBinding.mainWebView.post(() -> {
            viewBinding.mainWebView.evaluateJavascript(
                    " callback(" + Command.CMD_TOGGLE_CALENDAR + ",'','" + ApiModelUtils.toJson(toggleCalendarRequest) + "');"
                    , null);
        });
    }

    private void verifyToken() {
        Timber.d("Verify token");
        Timber.d("current path: " +viewBinding.mainWebView.getUrl());
        VerifyTokenRequest verifyTokenRequest = new VerifyTokenRequest();
        verifyTokenRequest.setToken(viewModel.currentRestaurant.getToken());
        Timber.d("request: "+verifyTokenRequest.toString());
        viewBinding.mainWebView.post(() -> viewBinding.mainWebView.evaluateJavascript(
                " callback(" + Command.CMD_VERIFY_TOKEN + ",'','" + ApiModelUtils.toJson(verifyTokenRequest) + "');", null
        ));

    }


    /**
     * Webview Handle Interface
     */
    @Override
    public BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void handleResponse(String cmd, String subCmd, String data, String error) {
        switch (cmd) {
            case Command.CMD_VERIFY_QRCODE:
                VerifyQRCodeResponse response = ApiModelUtils.fromJson(data, VerifyQRCodeResponse.class);
                handleVerifyQRCodeResponse(response);
                break;
            case Command.CMD_VERIFY_TOKEN:
                Timber.d("handle verify token");
                BaseSocketResponse verifyTokenResponse = ApiModelUtils.fromJson(data, BaseSocketResponse.class);
                handleVerifyToken(verifyTokenResponse);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleError(String errorCode) {
        switch (Integer.parseInt(errorCode)) {
            case ResponseCode.RESPONSE_ERROR_CODE_TIMEOUT:
                this.runOnUiThread(() -> viewModel.showErrorMessage(getString(R.string.connection_error)));
                break;
            case ResponseCode.RESPONSE_SESSION_TIMEOUT:
                this.runOnUiThread(() -> viewModel.showErrorMessage(getString(R.string.session_timeout)));
                break;
            case ResponseCode.RESPONSE_ERROR_CODE_UNDEFINED:
                Timber.d("RESPONSE_ERROR_CODE_UNDEFINED");
                break;
            case ResponseCode.RESPONSE_INVALID_CMD:
                Timber.d("RESPONSE_INVALID_CMD");
                break;
            default:
                break;
        }
    }

    @Override
    public void handlePath(String path) {
        Timber.d("receive"+path);
        switch (path) {
            case WebViewPath.REVENUE:
                this.runOnUiThread(() -> {
                    viewModel.setRestaurantSelected(true);
                });
                break;
            case WebViewPath.REVENUE_DETAIL:
                this.runOnUiThread(() -> {
                    viewModel.setDetailSelected(true);
                });
                break;
            default:
                break;
        }

    }

    private void handleVerifyQRCodeResponse(VerifyQRCodeResponse verifyQRCodeResponse) {
        if (verifyQRCodeResponse != null && Objects.equals(verifyQRCodeResponse.getResponseCode(), ResponseCode.RESPONSE_SUCCESS)) {
            viewModel.currentRestaurant.setToken(verifyQRCodeResponse.getData());
            viewModel.addRestaurant(viewModel.currentRestaurant);
            viewModel.currentRestaurant = null;
        } else if (verifyQRCodeResponse != null && Objects.equals(verifyQRCodeResponse.getResponseCode(), ResponseCode.RESPONSE_ERROR_CODE_TIMEOUT)) {
            this.runOnUiThread(() -> {
                viewModel.showErrorMessage(getString(R.string.connection_error));
            });
        } else if (verifyQRCodeResponse != null && Objects.equals(verifyQRCodeResponse.getResponseCode(), ResponseCode.RESPONSE_SESSION_TIMEOUT)) {
            this.runOnUiThread(() -> {
                viewModel.showErrorMessage(getString(R.string.session_timeout));
            });
            viewModel.currentRestaurant = null;
        }
    }

    private void handleVerifyToken(BaseSocketResponse verifyTokenResponse) {
        this.runOnUiThread(()->{
            Timber.d(viewBinding.mainWebView.getUrl());
        });
        if (verifyTokenResponse != null && Objects.equals(verifyTokenResponse.getResponseCode(), ResponseCode.RESPONSE_SUCCESS)) {
            viewModel.showLoading();
            Timber.d("navigate to %s", WebViewPath.REVENUE);
            navigateToUrl(WebViewPath.REVENUE);
        } else if (verifyTokenResponse != null && Objects.equals(verifyTokenResponse.getResponseCode(), ResponseCode.RESPONSE_ERROR_CODE_TIMEOUT)) {
            Timber.d("Timeout");
            this.runOnUiThread(() -> {
                viewModel.showErrorMessage(getString(R.string.connection_error));
            });
        } else {
            if (viewModel.currentRestaurant == null) {
                Timber.d("Null restaurant");
                return;
            }
            this.runOnUiThread(() -> {
                viewModel.showErrorMessage(getString(R.string.session_timeout));
            });
            viewModel.deleteRestaurant(viewModel.currentRestaurant);
            viewModel.currentRestaurant = null;
        }
    }

    @Override
    public void onBackPressed() {

        if (!viewModel.restaurantSelected.get()) {
            super.onBackPressed();
            return;
        }
        if (viewModel.detailSelected.get()) {
            viewModel.detailSelected.set(false);
            viewBinding.mainWebView.goBack();
        } else {
            navigateToUrl("/");
            viewModel.setRestaurantSelected(false);
            viewBinding.list.setImageResource(R.drawable.list_selected);
            viewBinding.note.setImageResource(R.drawable.note);
            viewBinding.statistic.setImageResource(R.drawable.statistic);
        }
    }
}
