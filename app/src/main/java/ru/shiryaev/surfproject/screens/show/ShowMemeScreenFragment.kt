package ru.shiryaev.surfproject.screens.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.databinding.FragmentShowMemeScreenBinding

class ShowMemeScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentShowMemeScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_meme_screen, container, false)
        val view = binding.root
        return view
    }

    companion object {
        @BindingAdapter("android:src")
        fun setImageUrl(imageView: ImageView, url: String) {
            Glide.with(imageView).load(url).into(imageView)
        }
    }
}