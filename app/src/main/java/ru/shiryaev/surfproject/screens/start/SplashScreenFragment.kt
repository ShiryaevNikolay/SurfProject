package ru.shiryaev.surfproject.screens.start

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.shiryaev.surfproject.MainActivityViewModel
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.NavGraphFragment
import ru.shiryaev.surfproject.utils.UserUtils

class SplashScreenFragment : Fragment() {

    private lateinit var navGraphFragment: NavGraphFragment
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        navGraphFragment = context as NavGraphFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            if (mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE).contains(UserUtils.USER_ID)) {
                navGraphFragment.startMainScreenFragmentFromSplashScreenFragment()
            } else {
                navGraphFragment.startLoginScreenFragmentFromSplashScreenFragment()
            }

//            if (mContext.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE).getBoolean(
//                    MainActivityViewModel.IS_LOGIN, false)) {
//                navGraphFragment.startMainScreenFragmentFromSplashScreenFragment()
//            } else {
//                navGraphFragment.startLoginScreenFragmentFromSplashScreenFragment()
//            }
        }, 300)
    }
}