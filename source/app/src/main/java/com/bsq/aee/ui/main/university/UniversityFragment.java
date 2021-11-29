package com.bsq.aee.ui.main.university;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.databinding.FragmentUniversityBinding;
import com.bsq.aee.di.component.FragmentComponent;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.base.fragment.BaseFragment;
import com.bsq.aee.ui.main.search.adapter.PageAdapter;
import com.bsq.aee.ui.main.search.adapter.PostAdapter;
import com.bsq.aee.ui.main.search.detail.adapter.PostDetailAdapter;
import com.bsq.aee.ui.main.university.adapter.UniversityAdapter;
import com.bsq.aee.ui.main.university.details.UniversityDetailsActivity;

import java.util.Objects;

import timber.log.Timber;

public class UniversityFragment extends BaseFragment<FragmentUniversityBinding,UniversityViewModel>
        implements UniversityAdapter.UniversityItemClickListener, View.OnClickListener, PageAdapter.PagingButtonClickListener {
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_university;
    }


    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        getUniversities(5,0);
        setUpAdapter();
        setUpPageAdapter();
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
    private UniversityAdapter adapter;
    private void setUpAdapter(){
        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.vertical_divider)));
        binding.recyclerView.addItemDecoration(divider);
        adapter = new UniversityAdapter(this);
        adapter.setItems(viewModel.universities.getData());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //dy > 0: scroll up; dy < 0: scroll down
                if (dy > 0) {
                    binding.search.hide();
                } else {
                    binding.search.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void updatePageAdapter(){
        pageAdapter.setSize(viewModel.universities.getTotalPages());
        pageAdapter.notifyDataSetChanged();
    }
    PageAdapter pageAdapter;
    private void setUpPageAdapter(){
        pageAdapter = new PageAdapter(this);
        pageAdapter.setSize(viewModel.universities.getTotalPages());

        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.horizontal_divider)));
        binding.paginationBar.addItemDecoration(divider);

        binding.paginationBar.setLayoutManager(new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false));

        binding.paginationBar.setAdapter(pageAdapter);
    }
    private void getUniversities(int size, int page){
        viewModel.showLoading();
        viewModel.getReplies(page, size, new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                Timber.d(error);
            }

            @Override
            public void doSuccess() {
                viewModel.hideLoading();
                if (pageAdapter.getSize() != viewModel.universities.getTotalPages()){
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

    public static final String UNIVERSITY_ITEM = "UNIVERSITY_ITEM";

    @Override
    public void universityItemClick(UniversityResponse item) {
        Intent it = new Intent(requireActivity(), UniversityDetailsActivity.class);
        it.putExtra(UNIVERSITY_ITEM, ApiModelUtils.toJson(item));
        startActivity(it);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search){
            binding.searchBar.getRoot().setVisibility(
                    binding.searchBar.getRoot().getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void pageClick(int index) {
        getUniversities(5,index);
    }
}

