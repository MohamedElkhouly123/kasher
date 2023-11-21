package com.example.ecommerceapp.data.api

import com.example.ecommerceapp.utils.notifictionsServices.Config.BASE
import com.example.ecommerceapp.utils.notifictionsServices.Config.BASE_cook
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientWithCookie {
    var retrofit: Retrofit? = null
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private var retrofitCreat: ApiServices? = null
//    private static String pattern="http://5d1d-102-42-53-70.ngrok.io/";
    fun getApiClient(): ApiServices? {
//        CertificatePinner certificatePinner = new CertificatePinner.Builder()
////        BuildConfig.pattern
//                .add(pattern,  "sha256/XaAoojsxKe7yZb0AEcI0WZRuqu0xvA7LALinySwW5aU=")
//                .add(pattern, "sha256/R3hcMOAGw0WFztuG2skTodoHp8IGid3Qg63Cn7YUYoM=")
//                .add(pattern, "sha256/x4QzPSC810K5/cMjb05Qm4k3Bw5zBn4lTdO/nEW/Td4=").build();
        if (retrofit == null) {
            try {
                val httpClient = OkHttpClient.Builder()
                httpClient .connectTimeout(200, TimeUnit.SECONDS)
                    .writeTimeout(150, TimeUnit.SECONDS)
                    .readTimeout(150, TimeUnit.SECONDS)
                    .build()
                val builder: Retrofit.Builder = Retrofit.Builder()
                    .baseUrl(BASE)
                    .addConverterFactory(GsonConverterFactory.create(gson)) //                        .client(httpClient.certificatePinner(certificatePinner).
                    .client(  httpClient.addInterceptor(Interceptor { chain ->
                        val original = chain.request()
                        val authorized = original.newBuilder()
                            .addHeader("Cookie", BASE_cook)
                            .build()
                        chain.proceed(authorized)
                    }).build())
                retrofit = builder.build()
                retrofitCreat = retrofit!!
                    .create(ApiServices::class.java)
            } catch (e: Exception) {
            }
        }
        return retrofitCreat
    }
}