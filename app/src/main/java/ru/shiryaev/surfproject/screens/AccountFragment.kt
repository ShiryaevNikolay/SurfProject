package ru.shiryaev.surfproject.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.fragment_main_screen.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.dialogs.LogoutDialog
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.interfaces.LogoutListener
import ru.shiryaev.surfproject.interfaces.ShowMemeListener
import ru.shiryaev.surfproject.screens.main.MainScreenFragment
import ru.shiryaev.surfproject.models.DbMeme
import ru.shiryaev.surfproject.models.controllers.DbMemeItemController
import ru.shiryaev.surfproject.utils.SpaceItemDecoration
import ru.shiryaev.surfproject.utils.UserUtils
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList

class AccountFragment : Fragment(), androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {

    private val memesAdapter = EasyAdapter()
    private val memeController = DbMemeItemController()
    private val mLogoutDialog = LogoutDialog()
    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var logoutListener: LogoutListener
    private lateinit var mContext: Context
    private lateinit var mMainScreenFragment: MainScreenFragment
    private lateinit var showMeme: ShowMemeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        logoutListener = context as LogoutListener
        mMainScreenFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as MainScreenFragment
        currentFragment = mMainScreenFragment
        showMeme = mMainScreenFragment
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        Glide.with(mContext).load("file:///android_asset/avatar.jpeg").circleCrop().into(view.account_im)
        mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE).apply {
            view.account_username.text = getString(UserUtils.USER_FIRST_NAME, "") + " " + getString(UserUtils.USER_LAST_NAME, "")
            view.account_userinfo.text = getString(UserUtils.USER_DESCRIPTION, "")
        }

        (mContext as MainActivity).snackbarLogoutShow = {
            val snack = Snackbar.make(mainLayout, "Во время запроса произошла ошибка, возможно вы неверно ввели логин/пароль", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            if (context != null) {
                snack.view.setBackgroundResource(R.drawable.warning_layout)
                snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
                snack.show()
            }
        }

        (mContext as MainActivity).mainActivityViewModel.getAllMemeOfUser(mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE).getString(UserUtils.USER_NAME, "")!!).observe(viewLifecycleOwner, {
            if (it != null) {
                val memesList = ItemList.create().apply {
                    addAll(it, memeController)
                }
                memesAdapter.setItems(memesList)
            }
            view.info_list_empty.isVisible = memesAdapter.itemCount == 0
            view.progressBar.isVisible = false
        })

        initRecyclerView(view.recyclerView)
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(ACCOUNT_FRAGMENT)
        mMainScreenFragment.toolbar.setOnMenuItemClickListener(this)

        memeController.apply {
            onClickShareBtn = { shareMeme(it) }
            onClickItemListener = { itemClick(it) }
            onClickFavoriteBtn = { (mContext as MainActivity).mainActivityViewModel.update(it) }
        }

        mLogoutDialog.onClickLogoutBtn = { (mContext as MainActivity).mainActivityViewModel.requestLogout() }

        (mContext as MainActivity).logout = {
            mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
            logoutListener.logout()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu) {
            val popup = PopupMenu(mContext, mMainScreenFragment.popupMenu, Gravity.BOTTOM)
            popup.menuInflater.inflate(R.menu.toolbar_account_menu, popup.menu)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.exit -> logout()
                }
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }
        return true
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        with(recyclerView) {
            setHasFixedSize(false)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = memesAdapter
            addItemDecoration(SpaceItemDecoration(20))
        }
    }

    private fun logout() { mLogoutDialog.show(childFragmentManager, null) }

    private fun shareMeme(data: DbMeme) {
        val shareMeme = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data.title)
            putExtra(Intent.EXTRA_STREAM, data.photoUrl)
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, null)
        startActivity(shareMeme)
    }

    private fun itemClick(data: DbMeme) { showMeme.showMeme(data, ACCOUNT_FRAGMENT) }

    companion object {
        const val ACCOUNT_FRAGMENT = "AccountFragment"
    }
}