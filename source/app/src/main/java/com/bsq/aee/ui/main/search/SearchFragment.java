package com.bsq.aee.ui.main.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.databinding.FragmentSearchBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragment;
import com.bsq.aee.ui.main.search.adapter.PostAdapter;
import com.bsq.aee.ui.main.search.create.CreatePostActivity;
import com.bsq.aee.ui.main.search.detail.PostDetailActivity;

import java.util.Objects;

import timber.log.Timber;

public class SearchFragment extends BaseFragment<FragmentSearchBinding,SearchViewModel>
implements View.OnClickListener, PostAdapter.PostClickListener {

    PostAdapter adapter;
    public static final String POST_ITEM = "POST_ITEM";

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);


    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.showLoading();
        viewModel.responseList = null;
        viewModel.getPost(0, 10, new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                Timber.d(error);
                viewModel.showErrorMessage(requireContext().getString(R.string.api_error));
            }

            @Override
            public void doSuccess() {
                viewModel.hideLoading();
                setUpAdapter();
            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(requireContext().getString(R.string.api_error));
            }
        });
    }

    private void setUpAdapter(){
        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.vertical_divider)));
        binding.recyclerView.addItemDecoration(divider);
        adapter = new PostAdapter(this);
        adapter.setItems(viewModel.responseList.getData());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //dy > 0: scroll up; dy < 0: scroll down
                if (dy > 0) binding.fab.hide();
                else binding.fab.show();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && viewModel.responseList.hasNext()) { //1 for down
                    loadMorePost(viewModel.responseList.getNext());
                }
            }
        });
    }

    private void loadMorePost(Integer page) {
        viewModel.showLoading();
        viewModel.getPost(page,10,new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                Timber.d(error);
                viewModel.showErrorMessage(requireContext().getString(R.string.api_error));
            }

            @Override
            public void doSuccess() {
                viewModel.hideLoading();
                adapter.setItems(viewModel.responseList.getData());
                adapter.notifyItemRangeChanged(viewModel.responseList.getOldIndex(),viewModel.responseList.getData().size());
            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(requireContext().getString(R.string.api_error));
            }
        });
    }


    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab){
            navigateToCreatePost();
        }
    }
    private void navigateToCreatePost(){
        Intent it = new Intent(requireActivity(), CreatePostActivity.class);
        startActivity(it);
    }

    @Override
    public void postClick(PostResponse item) {
        Intent it = new Intent(requireActivity(), PostDetailActivity.class);
        it.putExtra(POST_ITEM, ApiModelUtils.toJson(item));
        startActivity(it);
    }
}