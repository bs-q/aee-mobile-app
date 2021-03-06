package com.bsq.aee.ui.main.search.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.databinding.LayoutPostBinding;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostAdapterViewHolder> {

    @Getter
    @Setter
    List<PostResponse> items;

    private PostClickListener listener;

    public interface PostClickListener{
        void postClick(PostResponse item);
    }

    public PostAdapter(PostClickListener listener){
        this.listener = listener;
        Objects.requireNonNull(listener);
    }

    @NonNull
    @Override
    public PostAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPostBinding layoutPostBinding = LayoutPostBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PostAdapterViewHolder(layoutPostBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapterViewHolder holder, int position) {
        holder.layoutPostBinding.setItem(items.get(position));
        holder.layoutPostBinding.root.setOnClickListener(
              v ->  listener.postClick(holder.layoutPostBinding.getItem())
        );
        holder.layoutPostBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PostAdapterViewHolder extends RecyclerView.ViewHolder {
        LayoutPostBinding layoutPostBinding;
        public PostAdapterViewHolder(@NonNull LayoutPostBinding itemView) {
            super(itemView.getRoot());
            layoutPostBinding = itemView;
        }
    }
}

