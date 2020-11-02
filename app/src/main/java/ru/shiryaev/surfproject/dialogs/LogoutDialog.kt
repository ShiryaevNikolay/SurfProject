package ru.shiryaev.surfproject.dialogs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.shiryaev.surfproject.R

class LogoutDialog : DialogFragment() {

    var onClickLogoutBtn: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let { AlertDialog.Builder(it) }
            ?.setMessage(R.string.title_logout_dialog)
            ?.setPositiveButton(R.string.exit_btn_logout_dialog) { _, _ ->
                onClickLogoutBtn?.invoke()
            }
            ?.setNegativeButton(R.string.cancel_btn_logout_dialog) { _, _ ->
                Toast.makeText(activity, "О приложении", Toast.LENGTH_LONG).show()
            }
            ?.create()!!
    }
}