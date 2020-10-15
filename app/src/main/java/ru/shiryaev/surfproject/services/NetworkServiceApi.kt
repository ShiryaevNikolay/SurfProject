package ru.shiryaev.surfproject.services

import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.models.User

interface NetworkServiceApi {
    @POST("AndroidSchool/SurfAndroidSchool/1.0.0/auth/login")
    fun postLogin(@Body body: RequestBody): Single<User>

    @GET("memes")
    fun getMemes() : Single<List<Meme>>
}