package ru.shiryaev.surfproject.services

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.shiryaev.surfproject.interfaces.JSONPlaceHolderApi

object NetworkService {

    const val POST_LOGIN = "POST_LOGIN"
    const val GET_MEMES = "GET_MEMES"

    private var mRetrofitPostLogin: Retrofit = Retrofit.Builder()
        .baseUrl("https://virtserver.swaggerhub.com/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var mRetrofitGetMemes: Retrofit = Retrofit.Builder()
        .baseUrl("https://r2.mocker.surfstudio.ru/android_vsu/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getJSONApi(request: String) : JSONPlaceHolderApi? {
        return when(request) {
            POST_LOGIN -> postLogin()
            GET_MEMES -> getMemes()
            else -> null
        }
    }

    private fun postLogin() : JSONPlaceHolderApi {
        return mRetrofitPostLogin.create(JSONPlaceHolderApi::class.java)
    }

    private fun getMemes() : JSONPlaceHolderApi {
        return mRetrofitGetMemes.create(JSONPlaceHolderApi::class.java)
    }
}