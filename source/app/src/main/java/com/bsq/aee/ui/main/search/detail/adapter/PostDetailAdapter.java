package com.bsq.aee.ui.main.search.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.data.model.api.response.ReplyResponse;
import com.bsq.aee.databinding.LayoutPostDetailsBinding;
import com.bsq.aee.databinding.LayoutReplpyBinding;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.PostDetailAdapterViewHolder> {

    @Getter
    @Setter
    private PostResponse header;

    @Getter
    @Setter
    private List<ReplyResponse> replies;


    @NonNull
    @Override
    public PostDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            LayoutPostDetailsBinding layoutPostDetailsBinding = LayoutPostDetailsBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new PostDetailAdapterViewHolder(layoutPostDetailsBinding);
        } else {
            LayoutReplpyBinding layoutReplpyBinding = LayoutReplpyBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new PostDetailAdapterViewHolder(layoutReplpyBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostDetailAdapterViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            holder.header.setItem(header);
            holder.header.executePendingBindings();
        } else {
            holder.layoutReplpyBinding.setItem(replies.get(position-1));
            holder.layoutReplpyBinding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return replies.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return  position == 0 ? 0 : 1;
    }

    public static class PostDetailAdapterViewHolder extends RecyclerView.ViewHolder {
        LayoutPostDetailsBinding header;
        LayoutReplpyBinding layoutReplpyBinding;
        public PostDetailAdapterViewHolder(@NonNull LayoutPostDetailsBinding header) {
            super(header.getRoot());
            this.header = header;
        }
        public PostDetailAdapterViewHolder(@NonNull LayoutReplpyBinding layoutReplpyBinding) {
            super(layoutReplpyBinding.getRoot());
            this.layoutReplpyBinding = layoutReplpyBinding;
        }
    }
}
