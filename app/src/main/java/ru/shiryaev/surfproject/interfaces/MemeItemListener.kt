package ru.shiryaev.surfproject.interfaces

import android.view.View
import ru.shiryaev.surfproject.models.Meme

interface MemeItemListener {
    fun onClick(v: View, data: Meme)
}