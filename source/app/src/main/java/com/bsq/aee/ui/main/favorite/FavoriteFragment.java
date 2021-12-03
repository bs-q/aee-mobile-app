package com.bsq.aee.ui.main.favorite;

import static com.bsq.aee.ui.main.university.details.UniversityDetailsActivity.FIELD_ITEM;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.databinding.FragmentFavoriteBinding;
import com.bsq.aee.databinding.LayoutFavoriteItemBinding;
import com.bsq.aee.databinding.LayoutFieldItemBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragment;
import com.bsq.aee.ui.main.university.field.FieldDetailActivity;
import com.bsq.aee.utils.SimpleRecyclerViewAdapter;

import java.util.Objects;

import timber.log.Timber;

public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding,FavoriteViewModel> {
    @Override
    public int getBindingVariable() {
        return BR.a;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void performDataBinding() {

    }

    boolean init = false;
    @Override
    public void onStart() {
        super.onStart();
        if (!init){
            initialize();
            init = false;
        } else {
            updateData();
        }
    }

    private void updateData(){
        viewModel.showLoading();
        viewModel.getFavorite(new BaseCallback() {
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
    }

    private void initialize() {
        viewModel.showLoading();
        viewModel.getFavorite(new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
                Timber.d(error);
                viewModel.showErrorMessage(getString(R.string.api_error));
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

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    SimpleRecyclerViewAdapter<LayoutFavoriteItemBinding, FieldResponse> adapter;
    private void setUpAdapter(){
        adapter = new SimpleRecyclerViewAdapter<>(new SimpleRecyclerViewAdapter.SimpleRecyclerViewCallback<LayoutFavoriteItemBinding>() {
            @Override
            public LayoutFavoriteItemBinding setUpView(@NonNull ViewGroup parent, int viewType) {
                return LayoutFavoriteItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            }

            @Override
            public void bindData(SimpleRecyclerViewAdapter.SimpleRecyclerViewAdapterViewHolder<LayoutFavoriteItemBinding> holder, int position) {
                holder.getView().setItem(viewModel.favorite.getData().get(position));
                holder.getView().getRoot().setOnClickListener(v -> {
                    navigateToFieldDetail(ApiModelUtils.toJson(holder.getView().getItem()));
                });
                holder.getView().executePendingBindings();
            }

            @Override
            public int size() {
                return viewModel.favorite.getData().size();
            }
        });

        DividerItemDecoration vertical = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        vertical.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.vertical_divider_small)));
        binding.rc.addItemDecoration(vertical);
        binding.rc.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rc.setAdapter(adapter);
    }
    public static final String NO_FAVORITE = "NO_FAVORITE";
    private void navigateToFieldDetail(String jsonObject){
        Intent it = new Intent(requireContext(), FieldDetailActivity.class);
        it.putExtra(FIELD_ITEM,jsonObject);
        it.putExtra(NO_FAVORITE,true);
        startActivity(it);
    }
}