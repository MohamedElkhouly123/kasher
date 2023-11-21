package com.example.ecommerceapp.data.model.getRegisterResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetRegisterResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("same_device")
    @Expose
    var same_device = 0

    @SerializedName("data")
    @Expose
    var getRegisterUserDataResponse: UserDataResponce? = null
}