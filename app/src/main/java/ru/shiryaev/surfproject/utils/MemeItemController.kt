package ru.shiryaev.surfproject.utils

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.models.Meme
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class MemeItemController : BindableItemController<Meme, MemeItemController.Holder>() {

    var onClickItemListener: ((Meme) -> Unit)? = null
    var onClickShareBtn: ((Meme) -> Unit)? = null

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    override fun getItemId(data: Meme) = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Meme>(parent, R.layout.meme_item) {

        private val titleMeme = itemView.findViewById<TextView>(R.id.title)
        private val favoriteBtn = itemView.findViewById<CheckBox>(R.id.btn_favorite)
        private val shareBtn = itemView.findViewById<CheckBox>(R.id.btn_share)
        private val photoMeme = itemView.findViewById<ImageView>(R.id.photoMeme)

        override fun bind(data: Meme?) {
            if (data != null) {
                Glide.with(itemView).load(data.photoUrl).into(photoMeme)
                titleMeme.text = data.title
                favoriteBtn.isChecked = data.isFavorite!!

                shareBtn.setOnClickListener { onClickShareBtn?.invoke(data) }

                itemView.setOnClickListener { onClickItemListener?.invoke(data) }
            }
        }
    }
}