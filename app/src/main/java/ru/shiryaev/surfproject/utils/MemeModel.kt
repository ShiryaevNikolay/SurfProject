package ru.shiryaev.surfproject.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class MemeModel {
    var createdDate: Int? = null
    var description: String? = null
    var id: String? = null
    var isFavorite: Boolean? = null
    var photoUrl: String? = null
    var title: String? = null

    companion object {
        @BindingAdapter("app:url")
        @JvmStatic
        fun setImageUrl(imageView: ImageView, url: String) {
            Glide.with(imageView.context).load(url).into(imageView)
        }
    }
}