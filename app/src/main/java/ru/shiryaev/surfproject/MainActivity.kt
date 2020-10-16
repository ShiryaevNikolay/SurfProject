package ru.shiryaev.surfproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.shiryaev.surfproject.interfaces.NavGraphFragment

class MainActivity : AppCompatActivity(), NavGraphFragment {

    private lateinit var mNavController: NavController
    lateinit var mainActivityViewModel: MainActivityViewModel
        private set

    var snackbarMemeShow: (() -> Unit)? = null
    var snackbarLoginShow: (() -> Unit)? = null
    var refreshState: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.snackbarMemeState.observe(this, {
            if (it) snackbarMemeShow?.invoke()
        })
        mainActivityViewModel.snackbarLoginState.observe(this, {
            if (it) snackbarLoginShow?.invoke()
        })
        mainActivityViewModel.refreshState.observe(this, {
            if (!it) refreshState?.invoke()
        })

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun startLoginScreenFragment() {
        mNavController.navigate(R.id.action_splashScreenFragment_to_loginScreenFragment)
    }

    override fun startMainScreenFragment() {
        // Скрываем клавиатуру
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        mNavController.navigate(R.id.action_loginScreenFragment_to_mainScreenFragment)

        // Устанавливаем цвет StatusBar
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondaryColor)
    }
}