package com.bsq.aee.ui.main.university.field;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.databinding.ActivityFieldDetailScreenBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.main.university.UniversityFragment;
import com.bsq.aee.ui.main.university.details.UniversityDetailsActivity;

public class FieldDetailActivity extends BaseActivity<ActivityFieldDetailScreenBinding,FieldDetailViewModel>
implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_field_detail_screen;
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
        Bundle bundle = getIntent().getExtras();
        String jsonObject = bundle.getString(UniversityDetailsActivity.FIELD_ITEM);
        viewModel.fieldResponse = ApiModelUtils.fromJson(jsonObject,FieldResponse.class);
        viewBinding.setItem(viewModel.fieldResponse);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.favorite_btn){

        }
    }
}
