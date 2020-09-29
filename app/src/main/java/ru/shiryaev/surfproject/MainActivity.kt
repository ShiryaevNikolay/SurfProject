package ru.shiryaev.surfproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.shiryaev.surfproject.interfaces.NavGraphFragment

class MainActivity : AppCompatActivity(), NavGraphFragment {

    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun startLoginFragment() {
        mNavController.navigate(R.id.action_splashFragment_to_loginFragment)
    }
}