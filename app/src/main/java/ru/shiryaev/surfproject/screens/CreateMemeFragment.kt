package ru.shiryaev.surfproject.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener

class CreateMemeFragment : Fragment() {

    private lateinit var currentFragment: CurrentFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as CurrentFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_meme, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(CREATE_MEME_FRAGMENT)
    }

    companion object {
        const val CREATE_MEME_FRAGMENT = "CreateMemeFragment"
    }
}