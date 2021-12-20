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
import com.bsq.aee.ui.main.search.adapter.PageAdapter;
import com.bsq.aee.ui.main.search.adapter.PostAdapter;
import com.bsq.aee.ui.main.search.create.CreatePostActivity;
import com.bsq.aee.ui.main.search.detail.PostDetailActivity;
import com.bsq.aee.utils.DeviceUtils;

import java.util.Objects;

import timber.log.Timber;

public class SearchFragment extends BaseFragment<FragmentSearchBinding,SearchViewModel>
        implements View.OnClickListener, PostAdapter.PostClickListener, PageAdapter.PagingButtonClickListener {

    PostAdapter adapter;
    PageAdapter pageAdapter;
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
                setUpPageAdapter();
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
                if (dy > 0) {
                    binding.fab.hide();
                    binding.search.hide();
                } else {
                    binding.fab.show();
                    binding.search.show();
                }
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

    private void setUpPageAdapter(){
        pageAdapter = new PageAdapter(this);
        pageAdapter.setSize(viewModel.responseList.getTotalPages());

        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.horizontal_divider)));
        binding.paginationBar.addItemDecoration(divider);

        binding.paginationBar.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false));

        binding.paginationBar.setAdapter(pageAdapter);

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
        } else if (v.getId() == R.id.search){
            binding.searchBar.getRoot().setVisibility(
                    binding.searchBar.getRoot().getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
        } else if (v.getId() == R.id.search_btn){
            if (binding.searchBar.getSearch().get().isEmpty()) {
                viewModel.showErrorMessage("Vui lòng nhập nội dung");
                return;
            }
            DeviceUtils.hideSoftKeyboard(requireActivity());
            viewModel.showLoading();
            viewModel.search(binding.searchBar.getSearch().get(), new BaseCallback() {
                @Override
                public void doError(Throwable error) {
                    viewModel.hideLoading();
                    Timber.d(error);
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }

                @Override
                public void doSuccess() {
                    viewModel.hideLoading();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void doFail() {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.api_error));
                }
            });
        } else if (v.getId() == R.id.reply_btn){
            Timber.d("reply btn click");
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

    @Override
    public void pageClick(int index) {
        Timber.d("page index %d",index);
        getPost(5,index);

    }
    private void getPost(int size, int page){
        viewModel.showLoading();
        viewModel.getPost(page, size, new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                Timber.d(error);
            }

            @Override
            public void doSuccess() {
                viewModel.hideLoading();
                if (pageAdapter.getSize() != viewModel.responseList.getTotalPages()){
                    updatePageAdapter();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void doFail() {
                Timber.d("Error it get replies");
            }
        });
    }
    private void updatePageAdapter(){
        pageAdapter.setSize(viewModel.responseList.getTotalPages());
        pageAdapter.notifyDataSetChanged();
    }
}