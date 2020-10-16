package ru.shiryaev.surfproject.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.databinding.FragmentShowMemeBinding
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.screens.main.MainScreenFragment
import ru.shiryaev.surfproject.utils.MemeModel

class ShowMemeFragment : Fragment() {
    private lateinit var meme: MemeModel
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
            meme = MemeModel().apply {
                id = this@with?.getInt("idMeme")
                title = this@with?.getString("title")
                photoUrl = this@with?.getString("photoUrl")
                description = this@with?.getString("description")
                isFavorite = this@with?.getBoolean("isFavorite")
                createdDate = this@with?.getInt("createdDate")
            }
        }
        binding.meme = meme
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(SHOW_MEME_FRAGMENT)
    }

    companion object {
        const val SHOW_MEME_FRAGMENT = "ShowMemeFragment"
    }
}