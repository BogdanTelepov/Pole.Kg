package ru.app.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {

    private val api: Api

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val gson = GsonBuilder().setLenient().create()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(interceptor = logging)

        val retrofit = Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("url_api")
            .build()

        api = retrofit.create(Api::class.java)
    }

    companion object {
        private var instance: RetrofitService? = null

        fun getInstance(): RetrofitService {
            if (instance == null) {
                instance = RetrofitService()
            }

            return instance as RetrofitService
        }
    }

    fun getApi(): Api {
        return api
    }
}