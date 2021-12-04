package com.bsq.aee.ui.web;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityWebBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;

import lombok.Setter;

public class WebActivity extends BaseActivity<ActivityWebBinding,WebViewModel> {

    @Setter
    private static String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.showLoading();
        viewBinding.web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                    viewModel.hideLoading();

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                    viewModel.hideLoading();

            }
        });
        viewBinding.web.loadUrl(url);
    }
}