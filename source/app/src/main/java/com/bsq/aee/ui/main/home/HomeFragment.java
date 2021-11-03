package com.bsq.aee.ui.main.home;

import androidx.databinding.library.baseAdapters.BR;

import com.bsq.aee.R;
import com.bsq.aee.databinding.FragmentHomeBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.fragment.BaseFragment;

public class HomeFragment extends BaseFragment<FragmentHomeBinding,HomeViewModel> {
    @Override
    public int getBindingVariable() {
        return BR.a;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}