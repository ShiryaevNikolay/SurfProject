package ru.shiryaev.surfproject.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.shiryaev.surfproject.interfaces.JSONPlaceHolderApi

class NetworkService {
    private var mInstance: NetworkService? = null

    fun getInstance() : NetworkService {
        if (mInstance == null) {
            mInstance = NetworkService()
        }
        return mInstance as NetworkService
    }

    private var mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://virtserver.swaggerhub.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getJSONApi() : JSONPlaceHolderApi {
        return mRetrofit.create(JSONPlaceHolderApi::class.java)
    }
}