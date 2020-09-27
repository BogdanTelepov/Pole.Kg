package ru.app.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.app.responseModels.LoginResponseModel

interface Api {

    @POST("login")
    fun login(@Body headers:RequestBody): Call<LoginResponseModel>
}