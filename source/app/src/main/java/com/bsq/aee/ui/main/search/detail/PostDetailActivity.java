package com.bsq.aee.ui.main.search.detail;

import static com.bsq.aee.ui.main.search.SearchFragment.POST_ITEM;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

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
import com.bsq.aee.databinding.ActivityPostDetailBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.main.search.adapter.PageAdapter;
import com.bsq.aee.ui.main.search.detail.adapter.PostDetailAdapter;

import java.util.Objects;

import timber.log.Timber;

public class PostDetailActivity extends BaseActivity<ActivityPostDetailBinding,PostDetailViewModel>
        implements View.OnClickListener, PageAdapter.PagingButtonClickListener {
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
        setUpPageAdapter();
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
                if (pageAdapter.getSize() != viewModel.replies.getTotalPages()){
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
        pageAdapter.setSize(viewModel.replies.getTotalPages());
        pageAdapter.notifyDataSetChanged();
    }
    private PostDetailAdapter  adapter;
    PageAdapter pageAdapter;

    private void setUpAdapter(){
        adapter = new PostDetailAdapter();
        adapter.setHeader(viewModel.header);
        adapter.setReplies(viewModel.replies.getData());

        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.recyclerView.setAdapter(adapter);
    }
    private void setUpPageAdapter(){
        pageAdapter = new PageAdapter(this);
        pageAdapter.setSize(viewModel.replies.getTotalPages());

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.horizontal_divider)));
        viewBinding.paginationBar.addItemDecoration(divider);

        viewBinding.paginationBar.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        viewBinding.paginationBar.setAdapter(pageAdapter);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reply){
            viewBinding.replyBar.getRoot().setVisibility(
                    viewBinding.replyBar.getRoot().getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.reply_btn){
            Timber.d("Reply");
            doReply();
        } 
    }
    private void doReply(){
        if (Objects.requireNonNull(viewModel.reply.get()).isEmpty()) return;
        viewModel.showLoading();
        viewModel.reply(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                Timber.d(error);
            }

            @Override
            public void doSuccess() {
                viewModel.resetReplies(viewModel.replies.getCurrentPage(), 10, new BaseCallback() {
                    @Override
                    public void doError(Throwable error) {
                        viewModel.hideLoading();
                        Timber.d(error);
                        viewModel.showErrorMessage(getString(R.string.api_error));
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void doSuccess() {
                        viewModel.showSuccessMessage("Bình luận của bạn đã đăng");

                        viewModel.reply.set("");
                        hideKeyboard();
                        adapter.setReplies(viewModel.replies.getData());
                        viewModel.hideLoading();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void doFail() {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(getString(R.string.api_error));
                    }
                });

            }

            @Override
            public void doFail() {
                viewModel.hideLoading();
                viewModel.showErrorMessage(getString(R.string.api_error));
            }
        });
    }

    @Override
    public void pageClick(int index) {
        Timber.d("index %d",index);
        getReplies(10,index);
    }
}
