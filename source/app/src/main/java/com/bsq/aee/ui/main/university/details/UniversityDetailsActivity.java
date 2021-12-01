package com.bsq.aee.ui.main.university.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bsq.aee.BR;
import com.bsq.aee.R;
import com.bsq.aee.data.model.api.ApiModelUtils;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.databinding.ActivityUniversityDetailsBinding;
import com.bsq.aee.databinding.LayoutFieldItemBinding;
import com.bsq.aee.di.component.ActivityComponent;
import com.bsq.aee.ui.base.activity.BaseActivity;
import com.bsq.aee.ui.base.activity.BaseCallback;
import com.bsq.aee.ui.main.university.UniversityFragment;
import com.bsq.aee.ui.main.university.field.FieldDetailActivity;
import com.bsq.aee.utils.SimpleRecyclerViewAdapter;

import java.util.Objects;

public class UniversityDetailsActivity extends BaseActivity<ActivityUniversityDetailsBinding,UniversityDetailsViewModel> implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_university_details;
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
        String jsonObject = bundle.getString(UniversityFragment.UNIVERSITY_ITEM);
        UniversityResponse universityResponse = ApiModelUtils.fromJson(jsonObject,UniversityResponse.class);
        viewModel.address.set(universityResponse.getAddress());
        viewModel.image.set(universityResponse.getImage());
        viewModel.email.set(universityResponse.getEmail());
        viewModel.description.set(universityResponse.getDescription());
        viewModel.phone.set(universityResponse.getPhoneNumber());
        viewModel.universityName.set(universityResponse.getName());
        viewModel.getField(universityResponse.getId(), new BaseCallback() {
            @Override
            public void doError(Throwable error) {
                // do nothing
            }

            @Override
            public void doSuccess() {
                setUpAdapter();
            }

            @Override
            public void doFail() {
                // do nothing
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_btn){
            viewBinding.infoBtn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.main_color));
            viewBinding.fieldTbn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.white));
            viewModel.page.set(false);
        } else if (v.getId() == R.id.field_tbn){
            viewModel.page.set(true);
            viewBinding.fieldTbn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.main_color));
            viewBinding.infoBtn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.white));
        }
    }

    SimpleRecyclerViewAdapter<LayoutFieldItemBinding, FieldResponse> adapter;
    public static final String FIELD_ITEM = "FIELD_ITEM";

    private void setUpAdapter(){
        adapter = new SimpleRecyclerViewAdapter<>(new SimpleRecyclerViewAdapter.SimpleRecyclerViewCallback<LayoutFieldItemBinding>() {
            @Override
            public LayoutFieldItemBinding setUpView(@NonNull ViewGroup parent, int viewType) {
                return LayoutFieldItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            }

            @Override
            public void bindData(SimpleRecyclerViewAdapter.SimpleRecyclerViewAdapterViewHolder<LayoutFieldItemBinding> holder, int position) {
                holder.getView().setItem(viewModel.fieldResponseList.get(position));
                holder.getView().getRoot().setOnClickListener(v -> {
                    navigateToFieldDetail(ApiModelUtils.toJson(holder.getView().getItem()));
                });
                holder.getView().executePendingBindings();
            }

            @Override
            public int size() {
                return viewModel.fieldResponseList.size();
            }
        });

        DividerItemDecoration vertical = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        vertical.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.vertical_divider_small)));
        viewBinding.rc.addItemDecoration(vertical);
        viewBinding.rc.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rc.setAdapter(adapter);
    }

    private void navigateToFieldDetail(String jsonObject){
        Intent it = new Intent(this, FieldDetailActivity.class);
        it.putExtra(FIELD_ITEM,jsonObject);
        startActivity(it);
    }
}
