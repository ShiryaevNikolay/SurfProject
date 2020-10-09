package ru.shiryaev.surfproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.meme_item.view.*
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.models.Meme

class ListMemeAdapter : RecyclerView.Adapter<ListMemeAdapter.MemeHolder>() {

    private var listMemes: ArrayList<Meme> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeHolder {
        return MemeHolder(LayoutInflater.from(parent.context).inflate(R.layout.meme_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemeHolder, position: Int) {
        if (listMemes.isNotEmpty()) {
            holder.itemView.id_meme.text = listMemes[0].id
            holder.itemView.title.text = listMemes[0].title
            holder.itemView.description.text = listMemes[0].description
            holder.itemView.isFavorite.text = listMemes[0].isFavorite.toString()
            holder.itemView.createdDate.text = listMemes[0].createdDate.toString()
            holder.itemView.photoUrl.text = listMemes[0].photoUrl
        }
    }

    override fun getItemCount() = listMemes.size + 5

    fun setList(listMemes: List<Meme>) {
        this.listMemes = listMemes as ArrayList<Meme>
        notifyDataSetChanged()
    }

    inner class MemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}