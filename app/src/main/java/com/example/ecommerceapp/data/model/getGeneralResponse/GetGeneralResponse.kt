package com.example.ecommerceapp.data.model.getGeneralResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetGeneralResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}