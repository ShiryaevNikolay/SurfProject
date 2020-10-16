package ru.shiryaev.surfproject.services

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    private var mRetrofitGetMemes: Retrofit = Retrofit.Builder()
        .baseUrl("https://r2.mocker.surfstudio.ru/android_vsu/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getJSONApi(): NetworkServiceApi = mRetrofitGetMemes.create(NetworkServiceApi::class.java)
}