package ru.shiryaev.surfproject.interfaces

import ru.shiryaev.surfproject.models.NetworkMeme

interface ShowMemeListener {
    fun showMeme(meme: NetworkMeme)
}