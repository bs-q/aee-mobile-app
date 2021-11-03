package com.bsq.aee.ui.main.search;

import androidx.databinding.library.baseAdapters.BR;

import com.bsq.aee.R;
import com.bsq.aee.databinding.FragmentSearchBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.fragment.BaseFragment;

public class SearchFragment extends BaseFragment<FragmentSearchBinding,SearchViewModel> {
    @Override
    public int getBindingVariable() {
        return BR.a;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}