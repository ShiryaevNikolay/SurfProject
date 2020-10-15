package ru.shiryaev.surfproject.database.repository

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Single
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
    private lateinit var allMeme: LiveData<List<Meme>>
    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    init {
        App.getComponent().injectsAppRepository(this)
        memeDao = roomDatabase.memeDao()
    }

    fun insert(meme: Meme) {
        InsertMemeAsyncTask(memeDao).execute(meme)
    }

    fun update(meme: Meme) {
        UpdateMemeAsyncTask(memeDao).execute(meme)
    }

    fun delete(meme: Meme) {
        DeleteMemeAsyncTask(memeDao).execute(meme)
    }

    fun deleteAllMeme() {
        DeleteAllMemeAsyncTask(memeDao).execute()
    }

    fun getAllMeme() : LiveData<List<Meme>> {
        return allMeme
    }

    fun requestMeme() : Single<List<Meme>>? {
        return NetworkService
            .getJSONApi(NetworkService.GET_MEMES)
            ?.getMemes()
    }

    fun requestLogin(login: String, password: String) : Single<User>? {
        val json: String = "{\n" +
                "  \"login\": \"${login}\",\n" +
                "  \"password\": \"${password}\"\n" +
                "}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        return NetworkService
            .getJSONApi(NetworkService.POST_LOGIN)
            ?.postLogin(requestBody)
    }

    companion object {
        private class InsertMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<Meme, Void, Void>() {
            override fun doInBackground(vararg params: Meme?): Void? {
                params[0]?.let { memeDao.insert(it) }
                return null
            }
        }

        private class UpdateMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<Meme, Void, Void>() {
            override fun doInBackground(vararg params: Meme?): Void? {
                params[0]?.let { memeDao.update(it) }
                return null
            }
        }

        private class DeleteMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<Meme, Void, Void>() {
            override fun doInBackground(vararg params: Meme?): Void? {
                params[0]?.let { memeDao.delete(it) }
                return null
            }
        }

        private class DeleteAllMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                memeDao.deleteAllMeme()
                return null
            }
        }
    }
}