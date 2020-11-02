package ru.shiryaev.surfproject.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_create_meme.view.*
import kotlinx.android.synthetic.main.fragment_main_screen.*
import okio.IOException
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.dialogs.AddImageDialog
import ru.shiryaev.surfproject.interfaces.CreateMemeListener
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.screens.main.MainScreenFragment
import ru.shiryaev.surfproject.utils.MemeModel
import java.io.File
import java.io.FileOutputStream

class CreateMemeFragment : Fragment(), View.OnClickListener {

    private var isNotEmptyImage: ((Boolean) -> Unit)? = null
    private var isEnabledCreateBtn: ((Boolean) -> Unit)? = null
    private var titleMaxLength: Int? = null
    private var descriptionMaxLength: Int? = null
    private var mCurrentPhotoPath: String = ""
    private lateinit var mainScreenFragment: MainScreenFragment
    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var createMemeListener: CreateMemeListener
    private lateinit var titleLayout: TextInputLayout
    private lateinit var descriptionText: TextInputEditText
    private lateinit var addImBtn: ImageButton
    private lateinit var clearImgBtn: ImageButton
    private lateinit var mContext: Context
    private lateinit var imageMeme: ImageView
    private lateinit var layoutImage: FrameLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mainScreenFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as MainScreenFragment
        currentFragment = mainScreenFragment
        createMemeListener = mainScreenFragment
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
        clearImgBtn = view.clear_meme_im
        layoutImage = view.layout_create_meme_im
        titleLayout = view.title_meme_til
        descriptionText = view.description_et
        view.description_et.addTextChangedListener {
            if (it != null) showErrorInput(view.description_meme_til, it)
        }
        view.clear_meme_im.background.alpha = 204
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(CREATE_MEME_FRAGMENT)
        addImBtn.setOnClickListener(this)
        clearImgBtn.setOnClickListener(this)
        mainScreenFragment.create_meme_btn.setOnClickListener(this)
        isNotEmptyImage = { showLayoutImage(it) }
        isEnabledCreateBtn = { mainScreenFragment.create_meme_btn.isEnabled = it }

        titleLayout.title_et.addTextChangedListener {
            if (it != null) {
                showErrorInput(titleLayout, it)
                if (layoutImage.isVisible && it.isNotEmpty()) isEnabledCreateBtn?.invoke(true)
                else isEnabledCreateBtn?.invoke(false)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        addImBtn.setOnClickListener(null)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.add_im_meme_btn -> { if (!layoutImage.isVisible) onClickAddImBtn() }
            R.id.clear_meme_im -> { isNotEmptyImage?.invoke(false) }
            R.id.create_meme_btn -> { createMeme() }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startCamera()
                } else {
                    Toast.makeText(mContext, "Нужен доступ к камере и хранилищу", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            createImageView()
            when(requestCode) {
                REQUEST_TAKE_PHOTO -> {
                    Glide.with(this).load(mCurrentPhotoPath).into(imageMeme)
                    layoutImage.isVisible = true
                }
                REQUEST_PICK_PHOTO_FROM_GALLERY -> {
                    Glide.with(this).load(data?.data).into(imageMeme)
                    layoutImage.isVisible = true
                }
            }
        }
    }

    private fun showErrorInput(v: TextInputLayout, text: Editable) {
        if (text.length > titleMaxLength!!) v.error = "Ошибка ввода"
        else v.error = null
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
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA)
        } else {
            startCamera()
        }
    }

    private fun onClickGalleryBnt() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_PICK
        }
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), REQUEST_PICK_PHOTO_FROM_GALLERY)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(mContext.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                Toast.makeText(mContext, "Что-то пошло не так", Toast.LENGTH_LONG).show()
            }
            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(mContext, "ru.shiryaev.surfproject.fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile() : File {
        val imageFileName = "photo_" + System.currentTimeMillis()
        val photoDir = File(mContext.cacheDir, "MyImage")
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
        val image = File.createTempFile(imageFileName, ".jpg", photoDir)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun createImageView() {
        // Создаем ImageView для показа картинки с мемом
        imageMeme = ImageView(mContext)
        imageMeme.id = ID_IMAGE_VIEW
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        imageMeme.layoutParams = lp
        imageMeme.scaleType = ImageView.ScaleType.CENTER_INSIDE

        // Добавляем наш созданный ImageView в контейнер
        layoutImage.addView(imageMeme, 0)
        isNotEmptyImage?.invoke(true)
        if (titleLayout.title_et.text?.isNotEmpty()!!) isEnabledCreateBtn?.invoke(true)
    }

    private fun showLayoutImage(visible: Boolean) {
        if (!visible && layoutImage[0].id == ID_IMAGE_VIEW) {
            layoutImage.removeViewAt(0)
        }
        layoutImage.isVisible = visible
        addImBtn.imageAlpha = if (visible) 128 else 255
        if (titleLayout.title_et.text?.isNotEmpty()!!) isEnabledCreateBtn?.invoke(false)
    }

    private fun createMeme() {
        mCurrentPhotoPath = saveImage((imageMeme.drawable as BitmapDrawable).bitmap).toString()
        val meme = MemeModel().apply {
            title = titleLayout.title_et.text.toString()
            description = descriptionText.text.toString()
            photoUrl = mCurrentPhotoPath
            createdDate = System.currentTimeMillis()
            isFavorite = false
        }
        (mContext as MainActivity).mainActivityViewModel.insert(meme)
        createMemeListener.createMeme()
    }

    private fun saveImage(image: Bitmap) : Uri? {
        val imageFolder = File(mContext.cacheDir, "MyImage")
        val imageFileName = "photo_" + System.currentTimeMillis()
        var uri: Uri? = null
        try {
            if (!imageFolder.exists()) imageFolder.mkdirs()
            val file = File(imageFolder, imageFileName)
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(mContext, "ru.shiryaev.surfproject.fileprovider", file)
        } catch (e: IOException) {
            Toast.makeText(mContext, "Что-то пошло не так", Toast.LENGTH_LONG).show()
        }
        return uri
    }

    companion object {
        const val ID_IMAGE_VIEW = 0
        const val CREATE_MEME_FRAGMENT = "CreateMemeFragment"
        const val REQUEST_CAMERA = 1
        const val REQUEST_TAKE_PHOTO = 2
        const val REQUEST_PICK_PHOTO_FROM_GALLERY = 3
    }
}