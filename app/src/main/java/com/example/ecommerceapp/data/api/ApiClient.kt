package com.example.ecommerceapp.data.api

import com.example.ecommerceapp.utils.notifictionsServices.Config.BASE
import com.example.ecommerceapp.utils.notifictionsServices.Config.BASE2
import com.example.ecommerceapp.view.activity.BaseActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    var retrofit: Retrofit? = null
//    val BASE_URL = BASE2
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private var retrofitCreat: ApiServices? = null
    val apiClient: ApiServices?
        get() {
            if (retrofit == null) {
                try {
                    val httpClient: OkHttpClient.Builder =
                        OkHttpClient.Builder() //                    .callTimeout(2, TimeUnit.MINUTES)
                            .connectTimeout(200, TimeUnit.SECONDS)
                            .readTimeout(150, TimeUnit.SECONDS)
                            .writeTimeout(150, TimeUnit.SECONDS)
                    val builder = Retrofit.Builder()
                        .baseUrl(BASE2)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(
                            httpClient.build()
                        )
                    retrofit = builder.build()
                    retrofitCreat = retrofit!!
                        .create(ApiServices::class.java)
                } catch (e: Exception) {
                }
            }
            return retrofitCreat
        }
}