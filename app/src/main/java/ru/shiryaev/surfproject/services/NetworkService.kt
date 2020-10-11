package ru.shiryaev.surfproject.services

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.shiryaev.surfproject.interfaces.JSONPlaceHolderApi

object NetworkService {

    private var mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://r2.mocker.surfstudio.ru/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getJSONApi() : JSONPlaceHolderApi {
        return mRetrofit.create(JSONPlaceHolderApi::class.java)
    }
}