package com.bsq.aee.ui.account.post;

import static com.bsq.aee.ui.main.search.SearchFragment.POST_ITEM;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.databinding.ActivityMyPostBinding;
import com.bsq.aee.databinding.LayoutPostBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.main.search.detail.PostDetailActivity;
import com.bsq.aee.utils.SimpleRecyclerViewAdapter;

import java.util.Objects;

import timber.log.Timber;

public class MyPostActivity extends BaseActivity<ActivityMyPostBinding,MyPostViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_post;
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
        getMyPost();

    }

    private void getMyPost(){
        viewModel.showLoading();
        viewModel.getPost(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
                Timber.d(error);
            }

            @Override
            public void doSuccess() {
                viewModel.hideLoading();
                setUpAdapter();
            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
            }
        });
    }

    SimpleRecyclerViewAdapter<LayoutPostBinding, PostResponse> adapter;

    private void setUpAdapter(){
        adapter = new SimpleRecyclerViewAdapter<>(new SimpleRecyclerViewAdapter.SimpleRecyclerViewCallback<LayoutPostBinding>() {
            @Override
            public LayoutPostBinding setUpView(@NonNull ViewGroup parent, int viewType) {
                LayoutPostBinding layoutHomeItemBinding = LayoutPostBinding.inflate(LayoutInflater.from(parent.getContext()));
                layoutHomeItemBinding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return layoutHomeItemBinding;
            }

            @Override
            public void bindData(SimpleRecyclerViewAdapter.SimpleRecyclerViewAdapterViewHolder<LayoutPostBinding> holder, int position) {
                holder.getView().setItem(viewModel.postResponses.get(position));
                holder.getView().getRoot().setOnClickListener(v -> {
                    postClick(holder.getView().getItem());
                });
                holder.getView().executePendingBindings();
            }

            @Override
            public int size() {
                return viewModel.postResponses.size();
            }
        });

        DividerItemDecoration vertical = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        vertical.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.vertical_divider_small)));
        viewBinding.recyclerView.addItemDecoration(vertical);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.recyclerView.setAdapter(adapter);
    }
    public void postClick(PostResponse item) {
        Intent it = new Intent(this, PostDetailActivity.class);
        it.putExtra(POST_ITEM, ApiModelUtils.toJson(item));
        startActivity(it);
    }

}
