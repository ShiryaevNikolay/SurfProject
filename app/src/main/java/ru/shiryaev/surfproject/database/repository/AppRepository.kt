package ru.shiryaev.surfproject.database.repository

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.shiryaev.surfproject.database.room.AppRoomDatabase
import ru.shiryaev.surfproject.database.room.dao.MemeDao
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.models.User
import ru.shiryaev.surfproject.services.NetworkService
import ru.shiryaev.surfproject.utils.App
import ru.shiryaev.surfproject.utils.MemeModel
import javax.inject.Inject

class AppRepository {
    private var memeDao: MemeDao
    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    init {
        App.getComponent().injectsAppRepository(this)
        memeDao = roomDatabase.memeDao()
    }

    fun insert(meme: MemeModel) {
        InsertMemeAsyncTask(memeDao).execute(meme)
    }

    fun update(meme: MemeModel) {
        UpdateMemeAsyncTask(memeDao).execute(meme)
    }

    fun delete(meme: MemeModel) {
        DeleteMemeAsyncTask(memeDao).execute(meme)
    }

    fun deleteAllMeme() {
        DeleteAllMemeAsyncTask(memeDao).execute()
    }

    fun getAllMeme() : LiveData<List<MemeModel>> {
        return memeDao.getAllMeme()
    }

    fun requestMeme() : Single<List<Meme>> {
        return NetworkService
            .getJSONApi()
            .getMemes()
    }

    fun requestLogin(login: String, password: String) : Single<User> {
        val json: String = "{\n" +
                "  \"login\": \"${login}\",\n" +
                "  \"password\": \"${password}\"\n" +
                "}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        return NetworkService
            .getJSONApi()
            .postLogin(requestBody)
    }

    fun requestLogout() : Completable {
        return NetworkService
            .getJSONApi()
            .postLogout()
    }

    companion object {
        private class InsertMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<MemeModel, Void, Void>() {
            override fun doInBackground(vararg params: MemeModel?): Void? {
                params[0]?.let { memeDao.insert(it) }
                return null
            }
        }

        private class UpdateMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<MemeModel, Void, Void>() {
            override fun doInBackground(vararg params: MemeModel?): Void? {
                params[0]?.let { memeDao.update(it) }
                return null
            }
        }

        private class DeleteMemeAsyncTask(private val memeDao: MemeDao) : AsyncTask<MemeModel, Void, Void>() {
            override fun doInBackground(vararg params: MemeModel?): Void? {
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