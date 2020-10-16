package ru.shiryaev.surfproject.interfaces

import ru.shiryaev.surfproject.models.Meme

interface ShowMemeListener {
    fun showMeme(meme: Meme)
}