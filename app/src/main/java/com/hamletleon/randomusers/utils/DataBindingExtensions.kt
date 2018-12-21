package com.hamletleon.randomusers.utils

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter

@BindingAdapter("imageUrl")
fun loadImage(view: AppCompatImageView, url: String?) {
    if (url != null) Glide.with(view.context).load(url).into(view)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) Glide.with(view.context).load(url).into(view)
}