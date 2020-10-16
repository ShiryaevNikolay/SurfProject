package ru.shiryaev.surfproject.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.interfaces.ShowMemeListener
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.screens.MemesFragment
import ru.shiryaev.surfproject.screens.ShowMemeFragment

class MainScreenFragment : Fragment(), CurrentFragmentListener, ShowMemeListener {

    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var mNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_screen, container, false)
        toolbar = view.toolbar
        bottomNavView = view.bottomNavView
        hideAllItemMenuToolbar()
        showItemMenuToolbar(MemesFragment.MEMES_FRAGMENT)

        // Привязка content_graph к bottomNavView
        mNavController = Navigation.findNavController(view.findViewById(R.id.nav_host_fragment))
        view.bottomNavView.setupWithNavController(mNavController)
        return view
    }

    override fun currentFragment(currentFragment: String) {
        when (currentFragment) {
            MemesFragment.MEMES_FRAGMENT -> {
                toolbarMemes(currentFragment)
                bottomNavView.isVisible = true
            }
            ShowMemeFragment.SHOW_MEME_FRAGMENT -> {
                toolbarShowMeme(currentFragment)
                bottomNavView.isVisible = false
            }
        }
    }

    private fun toolbarMemes(currentFragment: String) {
        toolbar.title = context?.resources?.getString(R.string.toolbar_title_list_memes)
        toolbar.navigationIcon = null
        toolbar.setNavigationOnClickListener(null)
        hideAllItemMenuToolbar()
        showItemMenuToolbar(currentFragment)
    }

    private fun toolbarShowMeme(currentFragment: String) {
        toolbar.title = null
        toolbar.navigationIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_close) }
        toolbar.setNavigationOnClickListener { mNavController.popBackStack() }
        hideAllItemMenuToolbar()
        showItemMenuToolbar(currentFragment)
    }

    private fun showItemMenuToolbar(currentFragment: String) {
        when (currentFragment) {
            MemesFragment.MEMES_FRAGMENT -> toolbar.menu.getItem(0).isVisible = true
            ShowMemeFragment.SHOW_MEME_FRAGMENT -> toolbar.menu.getItem(1).isVisible = true
        }
    }

    private fun hideAllItemMenuToolbar() {
        for (item in 0 until toolbar.menu.size()) {
            toolbar.menu.getItem(item).isVisible = false
        }
    }

    override fun showMeme(meme: Meme) {
        val args = Bundle().apply {
            putInt("idMeme", meme.id!!.toInt())
            putString("title", meme.title)
            putString("photoUrl", meme.photoUrl)
            putString("description", meme.description)
            putBoolean("isFavorite", meme.isFavorite!!)
            putInt("createdDate", meme.createdDate!!)
        }
        mNavController.navigate(R.id.action_memesFragment_to_showMemeFragment, args)
    }
}