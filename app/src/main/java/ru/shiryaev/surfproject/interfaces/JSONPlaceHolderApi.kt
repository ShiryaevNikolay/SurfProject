package ru.shiryaev.surfproject.interfaces

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.models.User

interface JSONPlaceHolderApi {
    @POST("AndroidSchool/SurfAndroidSchool/1.0.0/auth/login")
    fun getToken(@Body body: RequestBody): Observable<User>

    @GET("android_vsu/memes")
    fun getMemes() : Observable<List<Meme>>
}