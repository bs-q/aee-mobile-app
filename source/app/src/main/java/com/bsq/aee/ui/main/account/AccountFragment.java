package com.bsq.aee.ui.main.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;

import com.bsq.aee.R;
import com.bsq.aee.databinding.FragmentAccountBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.account.login.LoginActivity;
import com.bsq.aee.ui.account.password.ChangePasswordActivity;
import com.bsq.aee.ui.account.post.MyPostActivity;
import com.bsq.aee.ui.base.fragment.BaseFragment;

public class AccountFragment extends BaseFragment<FragmentAccountBinding,AccountViewModel>
implements View.OnClickListener {
    @Override
    public int getBindingVariable() {
        return BR.a;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        binding.setEmail(viewModel.getFullName());
        binding.setAvatarUrl(viewModel.getAvatarPath());

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_password:
                navigateToChangePassword();
                break;
            case R.id.logout_btn:
                navigateToLogin();
                break;
            case R.id.my_post:
                navigateToMyPost();
                break;
            default:
                break;
        }
    }

    private void navigateToChangePassword(){
        Intent it = new Intent(requireContext(), ChangePasswordActivity.class);
        startActivity(it);
    }
    private void navigateToLogin(){
        viewModel.clearToken();
        Intent it = new Intent(requireContext(), LoginActivity.class);
        startActivity(it);
    }
    private void navigateToMyPost(){
        Intent it = new Intent(requireContext(), MyPostActivity.class);
        startActivity(it);
    }
}
