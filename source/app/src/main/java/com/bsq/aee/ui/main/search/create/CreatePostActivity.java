package com.bsq.aee.ui.main.search.create;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityCreatePostBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;

import java.util.Objects;

public class CreatePostActivity extends BaseActivity<ActivityCreatePostBinding,CreatePostViewModel>
implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_create_post;
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
        if (v.getId() == R.id.post_btn){
            createPost();
        }
    }
    private void createPost(){
        if (viewModel.validate() || viewBinding.content.getText()==null || viewBinding.content.getText().toString().equals("")){
            viewModel.showErrorMessage("Tiêu đề và nội dung không được để trống");
        } else {
            viewModel.content = Objects.requireNonNull(viewBinding.content.getText()).toString();
            viewModel.showLoading();
            viewModel.createPost(new BaseCallback() {
                @Override
                public void doError(Throwable error) {
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
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);

    }
}

