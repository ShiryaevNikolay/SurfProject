package ru.shiryaev.surfproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.shiryaev.surfproject.database.repository.AppRepository
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.utils.App

class MainActivityViewModel : ViewModel() {
    private var repository: AppRepository
    val allMeme = MutableLiveData<List<Meme>>()
    val progressBarState = MutableLiveData<Boolean>()
    val listEmptyState = MutableLiveData<Boolean>()
    val snackbarState = MutableLiveData<Boolean>()
    val refreshState = MutableLiveData<Boolean>()

    init {
        progressBarState.value = false
        listEmptyState.value = false
        snackbarState.value = false
        refreshState.value = false
        repository = AppRepository()
    }

    fun requestMeme(state: String) {
        if (state == App.LOADING_MEME) {
            loadingMeme()
        } else if (state == App.REFRESH_MEME) {
            refreshMeme()
        }
    }

    private fun loadingMeme() {
        repository.requestMeme()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.doOnSubscribe { progressBarState.value = true }
            ?.doFinally { progressBarState.value = false }
            ?.subscribe({
                allMeme.value = it
            }, {
                listEmptyState.value = true
                snackbarState.value = true
            })
    }

    private fun refreshMeme() {
        repository.requestMeme()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.doFinally { refreshState.value = false }
            ?.subscribe({
                allMeme.value = it
            }, {
                listEmptyState.value = true
                snackbarState.value = true
            })
    }
}