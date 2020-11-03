package ru.shiryaev.surfproject.interfaces

import ru.shiryaev.surfproject.models.DbMeme

interface ShowMemeListener {
    fun showMeme(meme: DbMeme, currentFragment: String)
}