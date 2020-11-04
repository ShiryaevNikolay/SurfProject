package ru.shiryaev.surfproject.models.controllers

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.models.DbMeme
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class DbMemeItemController : BindableItemController<DbMeme, DbMemeItemController.Holder>() {
    var onClickItemListener: ((DbMeme) -> Unit)? = null
    var onClickShareBtn: ((DbMeme) -> Unit)? = null
    var onClickFavoriteBtn: ((DbMeme) -> Unit)? = null

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    override fun getItemId(data: DbMeme) = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<DbMeme>(parent, R.layout.meme_item) {

        private val titleMeme = itemView.findViewById<TextView>(R.id.title)
        private val favoriteBtn = itemView.findViewById<CheckBox>(R.id.btn_favorite)
        private val shareBtn = itemView.findViewById<CheckBox>(R.id.btn_share)
        private val photoMeme = itemView.findViewById<ImageView>(R.id.photoMeme)

        override fun bind(data: DbMeme?) {
            if (data != null) {
                Glide.with(itemView).load(data.photoUrl).into(photoMeme)
                titleMeme.text = data.title
                favoriteBtn.isChecked = data.isFavorite!!

                favoriteBtn.setOnClickListener {
                    data.isFavorite = favoriteBtn.isChecked
                    onClickFavoriteBtn?.invoke(data)
                }
                shareBtn.setOnClickListener { onClickShareBtn?.invoke(data) }
                itemView.setOnClickListener { onClickItemListener?.invoke(data) }
            }
        }
    }
}