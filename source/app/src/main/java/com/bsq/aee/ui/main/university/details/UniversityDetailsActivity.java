package com.bsq.aee.ui.main.university.details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.databinding.ActivityUniversityDetailsBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.main.university.UniversityFragment;

public class UniversityDetailsActivity extends BaseActivity<ActivityUniversityDetailsBinding,UniversityDetailsViewModel> implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_university_details;
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
        Bundle bundle = getIntent().getExtras();
        String jsonObject = bundle.getString(UniversityFragment.UNIVERSITY_ITEM);
        UniversityResponse universityResponse = ApiModelUtils.fromJson(jsonObject,UniversityResponse.class);
        viewModel.address.set(universityResponse.getAddress());
        viewModel.image.set(universityResponse.getImage());
        viewModel.email.set(universityResponse.getEmail());
        viewModel.description.set(universityResponse.getDescription());
        viewModel.phone.set(universityResponse.getPhoneNumber());
        viewModel.universityName.set(universityResponse.getName());
    }

    @Override
    public void onClick(View v) {

    }
}
