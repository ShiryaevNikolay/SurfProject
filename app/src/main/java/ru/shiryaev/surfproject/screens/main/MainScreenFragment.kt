package ru.shiryaev.surfproject.screens.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CreateMemeListener
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.interfaces.ShowMemeListener
import ru.shiryaev.surfproject.models.DbMeme
import ru.shiryaev.surfproject.screens.AccountFragment
import ru.shiryaev.surfproject.screens.CreateMemeFragment
import ru.shiryaev.surfproject.screens.MemesFragment
import ru.shiryaev.surfproject.screens.ShowMemeFragment
import ru.shiryaev.surfproject.utils.UserUtils

class MainScreenFragment : Fragment(), CurrentFragmentListener, ShowMemeListener, CreateMemeListener, MenuItem.OnMenuItemClickListener {

    var onNavigationClickToolbar: (() -> Unit)? = null

    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var mNavController: NavController
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

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

    override fun onResume() {
        super.onResume()
        toolbar.menu.findItem(R.id.search).setOnMenuItemClickListener(this)
    }

    override fun currentFragment(currentFragment: String) {
        when (currentFragment) {
            MemesFragment.MEMES_FRAGMENT -> {
                toolbarMemes(currentFragment)
                setToolbarBgColorSecondary()
                bottomNavView.isVisible = true
            }
            ShowMemeFragment.SHOW_MEME_FRAGMENT -> {
                toolbarShowMeme(currentFragment)
                setToolbarBgColorSecondary()
                bottomNavView.isVisible = false
            }
            CreateMemeFragment.CREATE_MEME_FRAGMENT -> {
                toolbarCreateMeme()
                setToolbarBgColorSecondary()
                bottomNavView.isVisible = false
            }
            AccountFragment.ACCOUNT_FRAGMENT -> {
                toolbarAccount(currentFragment)
                setToolbarBgColorBg()
                bottomNavView.isVisible = true
            }
        }
    }

    override fun showMeme(meme: DbMeme, currentFragment: String) {
        val args = Bundle().apply {
            putLong("idMeme", meme.id!!)
            putString("title", meme.title)
            putString("photoUrl", meme.photoUrl)
            putString("description", meme.description)
            putBoolean("isFavorite", meme.isFavorite!!)
            putLong("createdDate", meme.createdDate!!)
            putString("services", meme.services)
        }
        if (currentFragment == MemesFragment.MEMES_FRAGMENT) {
            mNavController.navigate(R.id.action_memesFragment_to_showMemeFragment, args)
        } else if (currentFragment == AccountFragment.ACCOUNT_FRAGMENT) {
            mNavController.navigate(R.id.action_accountFragment_to_showMemeFragment, args)
        }
    }

    override fun createMeme() { mNavController.popBackStack() }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.search) { showSearchView() }
        return true
    }

    private fun toolbarMemes(currentFragment: String) {
        toolbar.apply {
            title = context?.resources?.getString(R.string.toolbar_title_list_memes)
            navigationIcon = null
            toolbar_user_info.isVisible = false
            setNavigationOnClickListener(null)
            search_til.isVisible = false
            setBackgroundResource(R.color.secondaryColor)
        }
        hideAllItemMenuToolbar()
        showItemMenuToolbar(currentFragment)
    }

    private fun toolbarShowMeme(currentFragment: String) {
        toolbar.apply {
            title = null
            navigationIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_close) }
            setNavigationOnClickListener { mNavController.popBackStack() }
            toolbar_user_info.isVisible = true
            toolbar_user_info.layout_user_info.isVisible = true
            toolbar_user_info.layout_user_info.toolbar_user_name.text =
                context?.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)?.getString(UserUtils.USER_NAME, "")
            toolbar_user_info.create_meme_btn.isVisible = false
            search_til.isVisible = false
            setBackgroundResource(R.color.secondaryColor)
        }
        Glide.with(mContext).load("file:///android_asset/avatar.jpeg").into(toolbar.toolbar_user_image)
        hideAllItemMenuToolbar()
        showItemMenuToolbar(currentFragment)
    }

    private fun toolbarCreateMeme() {
        toolbar.apply {
            title = null
            navigationIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_close) }
            setNavigationOnClickListener { mNavController.popBackStack() }
            toolbar_user_info.isVisible = true
            toolbar_user_info.layout_user_info.isVisible = false
            toolbar_user_info.create_meme_btn.isVisible = true
            search_til.isVisible = false
            setBackgroundResource(R.color.secondaryColor)
        }
        hideAllItemMenuToolbar()
    }

    private fun toolbarAccount(currentFragment: String) {
        toolbar.apply {
            title = null
            navigationIcon = null
            toolbar_user_info.isVisible = false
            search_til.isVisible = false
            setBackgroundResource(R.color.bgColor)
        }
        hideAllItemMenuToolbar()
        showItemMenuToolbar(currentFragment)
    }

    private fun showItemMenuToolbar(currentFragment: String) {
        when (currentFragment) {
            MemesFragment.MEMES_FRAGMENT -> toolbar.menu.getItem(0).isVisible = true
            ShowMemeFragment.SHOW_MEME_FRAGMENT -> toolbar.menu.getItem(1).isVisible = true
            AccountFragment.ACCOUNT_FRAGMENT -> toolbar.menu.getItem(2).isVisible = true
        }
    }

    private fun hideAllItemMenuToolbar() {
        for (item in 0 until toolbar.menu.size()) {
            toolbar.menu.getItem(item).isVisible = false
        }
    }

    private fun setToolbarBgColorSecondary() {
        // Устанавливаем цвет StatusBar
        val window: Window = (mContext as MainActivity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(mContext, R.color.secondaryColor)
    }

    private fun setToolbarBgColorBg() {
        // Устанавливаем цвет StatusBar
        val window: Window = (mContext as MainActivity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(mContext, R.color.bgColor)
    }

    private fun showSearchView() {
        hideAllItemMenuToolbar()
        toolbar.apply {
            title = null
            toolbar_user_info.isVisible = false
            navigationIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_back) }
            setNavigationOnClickListener {
                toolbarMemes(MemesFragment.MEMES_FRAGMENT)
                onNavigationClickToolbar?.invoke()
            }
            menu.getItem(3).isVisible = true
            search_til.isVisible = true
            search_et.text = null
        }
    }
}