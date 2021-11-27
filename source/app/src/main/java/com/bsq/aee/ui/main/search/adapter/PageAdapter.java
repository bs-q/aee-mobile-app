package com.bsq.aee.ui.main.search.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.RecyclerView;

import com.bsq.aee.databinding.LayoutPagingButtonBinding;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageAdapterViewHolder> {

   private int index = 0;
   @Getter
   @Setter
   private int size;

    private PagingButtonClickListener listener;

    public interface PagingButtonClickListener{
        void pageClick(int index);
    }

    public PageAdapter(PagingButtonClickListener listener){
        this.listener = listener;
        Objects.requireNonNull(listener);
    }

    @NonNull
    @Override
    public PageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPagingButtonBinding layoutPagingButtonBinding = LayoutPagingButtonBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PageAdapterViewHolder(layoutPagingButtonBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageAdapterViewHolder holder, int position) {
        if (position == 0 ){
            holder.layoutPagingButtonBinding.setSelected(new ObservableBoolean(true));
        }
        holder.layoutPagingButtonBinding.setIndex(position);
        holder.layoutPagingButtonBinding.root.setOnClickListener(
                v ->  listener.pageClick(index)
        );
        holder.layoutPagingButtonBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public static class PageAdapterViewHolder extends RecyclerView.ViewHolder {
        LayoutPagingButtonBinding layoutPagingButtonBinding;
        public PageAdapterViewHolder(@NonNull LayoutPagingButtonBinding itemView) {
            super(itemView.getRoot());
            layoutPagingButtonBinding = itemView;
        }
    }
}
