package ru.shiryaev.surfproject.screens.auth

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login_screen.*
import kotlinx.android.synthetic.main.fragment_login_screen.view.*
import kotlinx.android.synthetic.main.fragment_login_screen.view_btn
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.NavGraphFragment
import ru.shiryaev.surfproject.models.User
import ru.shiryaev.surfproject.utils.UserUtils

class LoginScreenFragment : Fragment(), View.OnClickListener {

    private lateinit var navGraphFragment: NavGraphFragment
    private lateinit var mContext: Context

    private lateinit var progressBarState: LiveData<Boolean>
    private lateinit var snackbarSate: LiveData<Boolean>
    private lateinit var userListener: LiveData<User>

    private var showPassword = false
    private var cursorPosition = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        progressBarState = (context as MainActivity).mainActivityViewModel.progressBarLoginState
        snackbarSate = context.mainActivityViewModel.snackbarLoginState
        userListener = context.mainActivityViewModel.user
        navGraphFragment = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_screen, container, false)
        view.progressBar.isVisible = false

        (mContext as MainActivity).snackbarLoginShow = {
            val snack = Snackbar.make(mainLayout, "Во время запроса произошла ошибка, возможно вы неверно ввели логин/пароль", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            if (context != null) {
                snack.view.setBackgroundResource(R.drawable.warning_layout)
                snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
                snack.show()
            }
        }

        progressBarState.observe((mContext as MainActivity), {
            view.login_btn.isVisible = !it
            view.progressBar.isVisible = it
        })

        userListener.observe((mContext as MainActivity), {
            saveUserData(it)
            navGraphFragment.startMainScreenFragment()
        })
        return view
    }

    override fun onResume() {
        super.onResume()
        input_field_password
        view_btn.login_btn.setOnClickListener(this)
        input_layout_password.endIconImageButton.setOnClickListener(this)
        input_layout_password.setSimpleTextChangeWatcher { theNewText, isError ->
            cursorPosition = input_field_password.selectionStart
            // Если поле ввода пароля не пустое
            if (theNewText.isNotEmpty()) {

                // Проверка количества символов пароля
                if (theNewText.length == 6) {
                    input_layout_password.helperText = null
                } else {
                    input_layout_password.helperText = context?.resources?.getString(R.string.input_password_helper)
                }

                funShowPassword(showPassword)
            } else {
                input_layout_password.setEndIcon(null)
                input_layout_password.helperText = null
            }
            input_field_password.setSelection(cursorPosition)
        }
    }

    override fun onPause() {
        super.onPause()
        view_btn.login_btn.setOnClickListener(null)
        input_layout_password.endIconImageButton.setOnClickListener(null)
        input_layout_password.setSimpleTextChangeWatcher(null)
    }

    override fun onClick(v: View?) {
        when (v) {
            view_btn.login_btn -> Handler().postDelayed({clickLoginBtn()}, 200)
            input_layout_password.endIconImageButton -> {
                showPassword = !showPassword
                cursorPosition = input_field_password.selectionStart
                funShowPassword(showPassword)
                input_field_password.setSelection(cursorPosition)
            }
        }
    }

    private fun clickLoginBtn() {
        if (input_field_login.text.isEmpty()) {
            input_layout_login.setError(context?.resources?.getString(R.string.input_login_error), false)
        }
        if (input_field_password.text.isEmpty()) {
            input_layout_password.setError(context?.resources?.getString(R.string.input_login_error), false)
        }
        if (input_field_login.text.isNotEmpty() && input_field_password.text.isNotEmpty()) {
            (mContext as MainActivity).mainActivityViewModel.progressBarLoginState.value = true
            Handler().postDelayed({
                (mContext as MainActivity).mainActivityViewModel.requestLogin(input_field_login.text.toString(), input_field_password.text.toString())
            }, 1000)
        }
    }

    private fun funShowPassword(showPassword: Boolean) {
        if (showPassword) {
            input_field_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            input_layout_password.setEndIcon(R.drawable.ic_eye_true)
        } else {
            input_field_password.transformationMethod = PasswordTransformationMethod.getInstance()
            input_layout_password.setEndIcon(R.drawable.ic_eye_false)
        }
    }

    private fun saveUserData(user: User) {
        context?.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)?.edit()?.apply {
            this.putString(UserUtils.USER_TOKEN, user.accessToken)
            user.userInfo?.id?.let { this.putInt(UserUtils.USER_ID, it) }
            this.putString(UserUtils.USER_NAME, user.userInfo?.username)
            this.putString(UserUtils.USER_FIRST_NAME, user.userInfo?.firstName)
            this.putString(UserUtils.USER_LAST_NAME, user.userInfo?.lastName)
            this.putString(UserUtils.USER_DESCRIPTION, user.userInfo?.userDescription)
        }?.apply()
    }
}