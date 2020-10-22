package ru.shiryaev.surfproject.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_add_image.view.*
import ru.shiryaev.surfproject.R

class AddImageDialog : DialogFragment() {

    var onClickAddImageDialog: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_image, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
        view.camera_btn.setOnClickListener { onClickAddImageDialog?.invoke(CAMERA_BTN) }
        view.gallery_btn.setOnClickListener { onClickAddImageDialog?.invoke(GALLERY_BTN) }
        return view
    }

    companion object {
        const val CAMERA_BTN = "CAMERA_BTN"
        const val GALLERY_BTN = "GALLERY_BTN"
    }
}