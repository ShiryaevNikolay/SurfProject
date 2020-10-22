package ru.shiryaev.surfproject.screens

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_create_meme.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.dialogs.AddImageDialog
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener

class CreateMemeFragment : Fragment(), View.OnClickListener {

    private var titleMaxLength: Int? = null
    private var descriptionMaxLength: Int? = null
    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var addImBtn: ImageButton
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        currentFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as CurrentFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_meme, container, false)
        titleMaxLength = view.title_meme_til.counterMaxLength
        descriptionMaxLength = view.description_meme_til.counterMaxLength
        addImBtn = view.add_im_meme_btn
        view.title_et.addTextChangedListener {
            if (it != null) {
                showErrorInput(view.title_meme_til, it)
            }
        }
        view.description_et.addTextChangedListener {
            if (it != null) {
                showErrorInput(view.description_meme_til, it)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(CREATE_MEME_FRAGMENT)
        addImBtn.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        addImBtn.setOnClickListener(null)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.add_im_meme_btn -> onClickAddImBtn()
        }
    }

    private fun showErrorInput(v: TextInputLayout, text: Editable) {
        if (text.length > titleMaxLength!!) {
            v.error = "Ошибка ввода"
        } else {
            v.error = null
        }
    }

    private fun onClickAddImBtn() {
        val addImDialog = AddImageDialog()
        addImDialog.show(childFragmentManager, null)
        addImDialog.onClickAddImageDialog = {
            when(it) {
                AddImageDialog.CAMERA_BTN -> onClickCameraBtn()
                AddImageDialog.GALLERY_BTN -> onClickGalleryBnt()
            }
            addImDialog.dismiss()
        }
    }

    private fun onClickCameraBtn() {
        Toast.makeText(mContext, "CAMERA", Toast.LENGTH_LONG).show()
    }

    private fun onClickGalleryBnt() {
        Toast.makeText(mContext, "GALLERY", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val CREATE_MEME_FRAGMENT = "CreateMemeFragment"
    }
}