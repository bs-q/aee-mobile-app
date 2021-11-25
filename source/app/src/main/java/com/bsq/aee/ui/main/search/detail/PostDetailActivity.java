package com.bsq.aee.ui.main.search.detail;

import static com.bsq.aee.ui.main.search.SearchFragment.POST_ITEM;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.databinding.ActivityPostDetailBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.main.search.detail.adapter.PostDetailAdapter;

import timber.log.Timber;

public class PostDetailActivity extends BaseActivity<ActivityPostDetailBinding,PostDetailViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_post_detail;
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
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        Bundle bundle = getIntent().getExtras();
        viewModel.header = ApiModelUtils.fromJson(bundle.getString(POST_ITEM), PostResponse.class);
        getReplies(10,0);
        setUpAdapter();
    }
    private void getReplies(int size, int page){
        viewModel.showLoading();
        viewModel.getReplies(page, size, new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                Timber.d(error);
            }

            @Override
            public void doSuccess() {
                viewModel.hideLoading();
                adapter.notifyItemRangeChanged(viewModel.replies.getOldIndex()
                        ,viewModel.replies.getData().size() - viewModel.replies.getOldIndex());
            }

            @Override
            public void doFail() {
                Timber.d("Error it get replies");
            }
        });
    }
    private PostDetailAdapter  adapter;

    private void setUpAdapter(){
        adapter = new PostDetailAdapter();
        adapter.setHeader(viewModel.header);
        adapter.setReplies(viewModel.replies.getData());

        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.recyclerView.setAdapter(adapter);
        viewBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollHorizontally(1) && viewModel.replies.hasNext()) { //1 for down
                    Timber.d("Load more videos");
                    getReplies(10,viewModel.replies.getNext());
                }
            }
        });
    }
}
