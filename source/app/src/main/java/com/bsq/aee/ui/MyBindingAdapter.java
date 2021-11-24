package com.bsq.aee.ui;

import androidx.databinding.BindingAdapter;

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
}
