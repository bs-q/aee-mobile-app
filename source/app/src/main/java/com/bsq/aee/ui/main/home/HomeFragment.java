package com.bsq.aee.ui.main.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.data.model.api.response.news.NewsResponse;
import com.bsq.aee.databinding.FragmentHomeBinding;
import com.bsq.aee.databinding.LayoutFieldItemBinding;
import com.bsq.aee.databinding.LayoutHomeItemBinding;
import com.bsq.aee.databinding.LayoutReplpyBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragment;
import com.bsq.aee.ui.web.WebActivity;
import com.bsq.aee.utils.SimpleRecyclerViewAdapter;

import java.util.Objects;

import timber.log.Timber;

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

    private boolean initialize = false;
    @Override
    public void onStart() {
        super.onStart();
        if (!initialize){
            initialize = true;
            viewModel.getNews(new BaseCallback() {
                @Override
                public void doError(Throwable error) {
                    viewModel.hideLoading();
                    Timber.d(error);
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }

                @Override
                public void doSuccess() {
                    setUpAdapter();
                }

                @Override
                public void doFail() {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }
            });
        }
    }
    SimpleRecyclerViewAdapter<LayoutHomeItemBinding, NewsResponse> adapter;

    private void setUpAdapter(){
        adapter = new SimpleRecyclerViewAdapter<>(new SimpleRecyclerViewAdapter.SimpleRecyclerViewCallback<LayoutHomeItemBinding>() {
            @Override
            public LayoutHomeItemBinding setUpView(@NonNull ViewGroup parent, int viewType) {
                LayoutHomeItemBinding layoutHomeItemBinding = LayoutHomeItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                layoutHomeItemBinding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return layoutHomeItemBinding;
            }

            @Override
            public void bindData(SimpleRecyclerViewAdapter.SimpleRecyclerViewAdapterViewHolder<LayoutHomeItemBinding> holder, int position) {
                holder.getView().setItem(viewModel.newsResponseList.get(position));
                holder.getView().getRoot().setOnClickListener(v -> {
                    navigateToNewsActivity(holder.getView().getItem().getUrl());
                });
                holder.getView().executePendingBindings();
            }

            @Override
            public int size() {
                return viewModel.newsResponseList.size();
            }
        });

        DividerItemDecoration vertical = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        vertical.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.vertical_divider_small)));
        binding.recyclerView.addItemDecoration(vertical);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void navigateToNewsActivity(String url){
        WebActivity.setUrl(url);
        Intent it = new Intent(requireContext(),WebActivity.class);
        startActivity(it);
    }
}