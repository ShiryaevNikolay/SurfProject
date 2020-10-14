package ru.shiryaev.surfproject.database.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.shiryaev.surfproject.database.room.AppRoomDatabase
import ru.shiryaev.surfproject.database.room.dao.MemeDao
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.models.User
import ru.shiryaev.surfproject.services.NetworkService
import ru.shiryaev.surfproject.utils.App
import javax.inject.Inject

class AppRepository {
    private var memeDao: MemeDao
    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    init {
        App.getComponent()?.injectsAppRepository(this)
        memeDao = roomDatabase.memeDao()
    }

    private fun requestMeme() : io.reactivex.rxjava3.core.Observable<List<Meme>>? {
        return NetworkService
            .getJSONApi(NetworkService.GET_MEMES)
            ?.getMemes()
    }

    private fun requestLogin(login: String, password: String) : io.reactivex.rxjava3.core.Observable<User>? {
        val json: String = "{\n" +
                "  \"login\": \"${login}\",\n" +
                "  \"password\": \"${password}\"\n" +
                "}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        return NetworkService
            .getJSONApi(NetworkService.POST_LOGIN)
            ?.postLogin(requestBody)
    }
}