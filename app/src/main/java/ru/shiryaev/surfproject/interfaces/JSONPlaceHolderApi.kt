package ru.shiryaev.surfproject.interfaces

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.shiryaev.surfproject.models.User

interface JSONPlaceHolderApi {
    @POST("AndroidSchool/SurfAndroidSchool/1.0.0/auth/login")
    fun getToken(@Body body: RequestBody): Call<User>
}