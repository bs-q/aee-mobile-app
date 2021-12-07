package com.bsq.aee.ui.account.password;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityChangePasswordBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;

import timber.log.Timber;

public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding,ChangePasswordViewModel>
implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
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
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_password_btn){
            hideKeyboard();
            changePassword();
        }
    }
    private void changePassword(){
        if (viewModel.validateForm()){
            viewModel.showLoading();
            viewModel.changePassword(new BaseCallback() {
                @Override
                public void doError(Throwable error) {
                    Timber.d(error);
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }

                @Override
                public void doSuccess() {
                    viewModel.hideLoading();
                    finish();
                }

                @Override
                public void doFail() {
                    viewModel.hideLoading();
                }
            });
        }
    }
}
