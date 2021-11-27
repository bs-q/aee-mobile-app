package com.bsq.aee.ui;

import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bsq.aee.R;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class MyBindingAdapter {
    private MyBindingAdapter(){

    }
    @BindingAdapter("glide_image")
    public static void loadImageByGlide(ShapeableImageView view, String url){
        Glide.with(view.getContext()).load(url)
                .centerCrop()
                .into(view);
    }
    @BindingAdapter("paging_button")
    public static void pagingButtonState(TextView view, boolean selected){
        if (selected){
            view.setTextColor(Color.WHITE);
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.pagination_background));
        } else {
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.main_color));
            view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.pagination_background_disable));
        }
    }
}
