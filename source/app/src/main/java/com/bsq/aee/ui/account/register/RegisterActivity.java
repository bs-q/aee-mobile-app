package com.bsq.aee.ui.account.register;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityRegisterBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.account.login.LoginActivity;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding,RegisterViewModel>
implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                doRegister();
                break;
            case R.id.login_btn:
                navigateToLoginActivity();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            viewModel.mEmail.set(Objects.requireNonNull(bundle.getString(LoginActivity.LOGIN_EMAIl)));
            viewModel.mToken = Objects.requireNonNull(bundle.getString(LoginActivity.LOGIN_TOKEN));
            viewModel.mFirebaseUID = Objects.requireNonNull(bundle.getString(LoginActivity.LOGIN_UID));
        }
    }

    private void doRegister() {
        if (viewModel.validateForm()){
            viewModel.doRegister(new BaseCallback() {
                @Override
                public void doError(Throwable error) {
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }

                @Override
                public void doSuccess() {
                    viewModel.showSuccessMessage(getString(R.string.create_account_success));
                    navigateToLoginActivity();
                }

                @Override
                public void doFail() {
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }
            });
        }
    }

    private void navigateToLoginActivity() {
        finish();
    }

}