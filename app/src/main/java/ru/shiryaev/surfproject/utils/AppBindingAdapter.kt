package ru.shiryaev.surfproject.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object AppBindingAdapter {
    @BindingAdapter("app:url")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).into(imageView)
    }
}