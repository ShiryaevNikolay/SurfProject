package ru.shiryaev.surfproject.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.screens.MemesFragment

class MainScreenFragment : Fragment(), CurrentFragmentListener {

    private lateinit var toolbar: Toolbar
    private lateinit var mNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_screen, container, false)
        toolbar = view.toolbar
        hideAllItemMenuToolbar()
        showItemMenuToolbar(MemesFragment.NAME_FRAGMENT)

        // Привязка content_graph к bottomNavView
        mNavController = Navigation.findNavController(view.findViewById(R.id.nav_host_fragment))
        view.bottomNavView.setupWithNavController(mNavController)
        return view
    }

    override fun currentFragment(currentFragment: String) {
        when (currentFragment) {
            MemesFragment.NAME_FRAGMENT -> {
                toolbar.title = context?.resources?.getString(R.string.toolbar_title_list_memes)
                hideAllItemMenuToolbar()
                showItemMenuToolbar(currentFragment)
            }
        }
    }

    private fun showItemMenuToolbar(currentFragment: String) {
        when (currentFragment) {
            MemesFragment.NAME_FRAGMENT -> {
                toolbar.menu.getItem(0).isVisible = true
            }
        }
    }

    private fun hideAllItemMenuToolbar() {
        for (item in 0 until toolbar.menu.size()) {
            toolbar.menu.getItem(item).isVisible = false
        }
    }
}