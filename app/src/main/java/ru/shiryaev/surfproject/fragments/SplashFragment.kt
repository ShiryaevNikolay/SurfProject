package ru.shiryaev.surfproject.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.NavGraphFragment

class SplashFragment : Fragment() {

    private lateinit var navGraphFragment: NavGraphFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navGraphFragment = context as NavGraphFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            navGraphFragment.startLoginFragment()
        }, 300)
    }
}