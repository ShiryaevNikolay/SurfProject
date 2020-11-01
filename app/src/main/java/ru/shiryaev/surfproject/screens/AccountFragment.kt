package ru.shiryaev.surfproject.screens

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.fragment_main_screen.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.MainActivityViewModel
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.interfaces.LogoutListener
import ru.shiryaev.surfproject.screens.main.MainScreenFragment
import ru.shiryaev.surfproject.utils.UserUtils

class AccountFragment : Fragment(), androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {

    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var logoutListener: LogoutListener
    private lateinit var mContext: Context
    private lateinit var mMainScreenFragment: MainScreenFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        logoutListener = context as LogoutListener
        mMainScreenFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as MainScreenFragment
        currentFragment = mMainScreenFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        Glide.with(mContext).load("file:///android_asset/avatar.jpeg").circleCrop().into(view.account_im)
        mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE).apply {
            view.account_username.text = getString(UserUtils.USER_NAME, "")
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

        (mContext as MainActivity).logout = {
            mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)
                ?.edit()
                ?.putBoolean(MainActivityViewModel.IS_LOGIN, false)
                ?.apply()
            logoutListener.logout()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(ACCOUNT_FRAGMENT)
        mMainScreenFragment.toolbar.setOnMenuItemClickListener(this)
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

    private fun logout() {
        (mContext as MainActivity).mainActivityViewModel.requestLogout()
    }

    companion object {
        const val ACCOUNT_FRAGMENT = "AccountFragment"
    }
}