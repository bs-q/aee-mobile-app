package com.bsq.aee.ui.main.university;

import androidx.databinding.library.baseAdapters.BR;

import com.bsq.aee.R;
import com.bsq.aee.databinding.FragmentUniversityBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.fragment.BaseFragment;

public class UniversityFragment extends BaseFragment<FragmentUniversityBinding,UniversityViewModel> {
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_university;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}

