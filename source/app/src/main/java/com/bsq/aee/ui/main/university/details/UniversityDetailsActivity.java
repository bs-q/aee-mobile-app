package com.bsq.aee.ui.main.university.details;

import android.view.View;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.databinding.ActivityUniversityDetailsBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;

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
    public void onClick(View v) {

    }
}
