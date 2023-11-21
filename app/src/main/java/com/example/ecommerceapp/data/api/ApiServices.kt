package com.example.ecommerceapp.data.api

import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.utils.notifictionsServices.Config.BASE
import com.example.ecommerceapp.utils.notifictionsServices.Config.reset
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServices {

    @Headers("Cookie: sessionid=sessionid; token=token")
    @POST("api/auth/login")
    @FormUrlEncoded
    fun login(
        @Field("phone") phone: String?,
//        @Field("mac_addr") mac_addr: String?,
        @Field("password") password: String?,
        @Field("lang") lang: String?
    ): Call<GetRegisterResponse?>?

    @Headers("Accept: application/json")
    @POST(reset)
    @FormUrlEncoded
    fun userResetPassword(
        @Field("email") email: String?
    ): Call<GetGeneralResponse?>?

    @Headers("Accept: application/json")
    @POST("api/auth/forgot-pwd")
    @FormUrlEncoded
    fun saveFireBaseToken(
        @Field("email") email: String?
    ): Call<GetGeneralResponse?>?

    @GET("api/auth/forgot-pwd")
    fun getCategories(
        @Field("page") page: Int
    ): Call<GetGeneralResponse?>?
}