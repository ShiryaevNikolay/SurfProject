package ru.shiryaev.surfproject.fragments

import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view_btn
import ru.shiryaev.surfproject.R

class LoginFragment : Fragment(), View.OnClickListener {

    private var showPassword = false
    private var cursorPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        view.progressBar.isVisible = false
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
            view_btn.login_btn -> clickLoginBtn()
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
        val textBtn = login_btn.text
        login_btn.text = null
        progressBar.isVisible = true
        Handler().postDelayed({
            login_btn.text = textBtn
            progressBar.isVisible = false
        }, 1500)
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
}