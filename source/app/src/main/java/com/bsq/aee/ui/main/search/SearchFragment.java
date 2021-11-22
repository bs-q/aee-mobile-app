package com.bsq.aee.ui.main.search;

import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bsq.aee.R;
import com.bsq.aee.databinding.FragmentSearchBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.fragment.BaseFragment;
import com.bsq.aee.ui.main.search.adapter.PostAdapter;

import java.util.Objects;

public class SearchFragment extends BaseFragment<FragmentSearchBinding,SearchViewModel> {

    PostAdapter adapter;

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
        adapter = new PostAdapter();
        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.vertical_divider)));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}