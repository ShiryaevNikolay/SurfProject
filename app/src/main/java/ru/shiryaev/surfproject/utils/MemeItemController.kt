package ru.shiryaev.surfproject.utils

import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.meme_item.view.*
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.models.Meme
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class MemeItemController : BindableItemController<Meme, MemeItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    override fun getItemId(data: Meme) = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Meme>(parent, R.layout.meme_item) {
        override fun bind(data: Meme?) {
            if (data != null) {
                Glide.with(itemView).load(data.photoUrl).into(itemView.photoMeme)
                itemView.title.text = data.title
                itemView.btn_favorite.isChecked = data.isFavorite!!
            }
        }
    }
}