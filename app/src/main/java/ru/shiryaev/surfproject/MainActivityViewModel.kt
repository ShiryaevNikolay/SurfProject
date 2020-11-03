package ru.shiryaev.surfproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.shiryaev.surfproject.database.repository.AppRepository
import ru.shiryaev.surfproject.models.NetworkMeme
import ru.shiryaev.surfproject.models.User
import ru.shiryaev.surfproject.utils.App
import ru.shiryaev.surfproject.models.DbMeme

class MainActivityViewModel : ViewModel() {
    private var repository: AppRepository
//    var allMeme = LiveData(listOf(DbMeme))
    val progressBarMemeState = MutableLiveData<Boolean>()
    val listEmptyState = MutableLiveData<Boolean>()
    val snackbarMemeState = MutableLiveData<Boolean>()
    val refreshState = MutableLiveData<Boolean>()

    val progressBarLoginState = MutableLiveData<Boolean>()
    val snackbarLoginState = MutableLiveData<Boolean>()
    val snackbarLogout = MutableLiveData<Boolean>()

    val user = MutableLiveData<User>()

    init {
        progressBarMemeState.value = false
        listEmptyState.value = false
        snackbarMemeState.value = false
        refreshState.value = false
        snackbarLogout.value = false
        repository = AppRepository()
    }

    fun requestMeme(state: String) {
        if (state == App.LOADING_MEME) {
            loadingMeme()
        } else if (state == App.REFRESH_MEME) {
            refreshMeme()
        }
    }

    fun requestLogin(login: String, password: String) {
        repository.requestLogin(login, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { progressBarLoginState.value = true }
            .doFinally { progressBarLoginState.value = false }
            .subscribe({
                user.value = it
            }, {
                snackbarLoginState.value = true
            })
    }

    fun requestLogout() {
        repository.requestLogout()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                snackbarLogout.value = false
            }, {
                snackbarLogout.value = true
            })
    }

    fun insert(meme: DbMeme) { repository.insert(meme) }

    fun getAll() : LiveData<List<DbMeme>> = repository.getAllMeme()

    fun getAllMemeOfUser(username: String) : LiveData<List<DbMeme>> = repository.getAllMemeOfUser(username)

    fun getMemeOfFilter(filter: String) : LiveData<List<DbMeme>> = repository.getMemeOfFilter(filter)

    private fun loadingMeme() {
        repository.requestMeme()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { progressBarMemeState.value = true }
            .doFinally { progressBarMemeState.value = false }
            .subscribe({
                saveMemeToDatabase(it)
            }, {
                listEmptyState.value = true
                snackbarMemeState.value = true
            })
    }

    private fun refreshMeme() {
        repository.requestMeme()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doFinally { refreshState.value = false }
            .subscribe({
                saveMemeToDatabase(it)
            }, {
                listEmptyState.value = true
                snackbarMemeState.value = true
            })
    }

    private fun saveMemeToDatabase(listMeme: List<NetworkMeme>) {
        val listDbMeme = mutableListOf<DbMeme>()
        for (item in listMeme) {
            val meme = DbMeme().apply {
                id = item.id?.toLong()
                title = item.title
                description = item.description
                photoUrl = item.photoUrl
                createdDate = item.createdDate
                isFavorite = item.isFavorite
                services = "network"
            }
            listDbMeme += meme
        }
        repository.insertList(listDbMeme)
    }
}