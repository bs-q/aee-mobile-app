package com.bsq.aee.ui.main.university.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.databinding.UniversityItemBinding;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniversityAdapterViewHolder> {

    @Getter
    @Setter
    List<UniversityResponse> items;

    private UniversityAdapter.UniversityItemClickListener listener;

    public interface UniversityItemClickListener{
        void UniversityItemClick(UniversityResponse item);
    }

    public UniversityAdapter(UniversityAdapter.UniversityItemClickListener listener){
        this.listener = listener;
        Objects.requireNonNull(listener);
    }

    @NonNull
    @Override
    public UniversityAdapter.UniversityAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UniversityItemBinding universityItemBinding = UniversityItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new UniversityAdapter.UniversityAdapterViewHolder(universityItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityAdapter.UniversityAdapterViewHolder holder, int position) {
        holder.UniversityItemBinding.setName(items.get(position).getName());
        holder.UniversityItemBinding.setShortDescription(items.get(position).getDescription());
        holder.UniversityItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class UniversityAdapterViewHolder extends RecyclerView.ViewHolder {
        UniversityItemBinding UniversityItemBinding;
        public UniversityAdapterViewHolder(@NonNull UniversityItemBinding itemView) {
            super(itemView.getRoot());
            UniversityItemBinding = itemView;
        }
    }
}

