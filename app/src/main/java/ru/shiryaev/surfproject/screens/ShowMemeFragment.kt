package ru.shiryaev.surfproject.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.checkbox.MaterialCheckBox
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.databinding.FragmentShowMemeBinding
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.screens.main.MainScreenFragment
import ru.shiryaev.surfproject.models.DbMeme

class ShowMemeFragment : Fragment() {
    private lateinit var meme: DbMeme
    private lateinit var mContext: Context
    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var parentFragment: MainScreenFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        parentFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as MainScreenFragment
        currentFragment = parentFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentShowMemeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_meme, container, false)
        with(arguments) {
            meme = DbMeme().apply {
                id = this@with?.getLong("idMeme")
                title = this@with?.getString("title")
                photoUrl = this@with?.getString("photoUrl")
                description = this@with?.getString("description")
                isFavorite = this@with?.getBoolean("isFavorite")
                createdDate = this@with?.getLong("createdDate")
                services = this@with?.getString("services")
            }
        }
        binding.meme = meme
        binding.favoriteBtn.setOnClickListener { onClickFavorite(meme, (it as MaterialCheckBox).isChecked) }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(SHOW_MEME_FRAGMENT)
    }

    private fun onClickFavorite(meme: DbMeme, checked: Boolean) {
        meme.isFavorite = checked
        (mContext as MainActivity).mainActivityViewModel.update(meme)
    }

    companion object {
        const val SHOW_MEME_FRAGMENT = "ShowMemeFragment"
    }
}