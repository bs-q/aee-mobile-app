package com.bsq.aee.ui.main.home;

import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.bsq.aee.R;
import com.bsq.aee.databinding.FragmentHomeBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.fragment.BaseFragment;

import java.util.Objects;

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
//        DividerItemDecoration gameDivider = new DividerItemDecoration(binding.featureGame.getContext(), DividerItemDecoration.HORIZONTAL);
//        gameDivider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.horizontal_divider)));
//        binding.featureGame.addItemDecoration(gameDivider);
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}