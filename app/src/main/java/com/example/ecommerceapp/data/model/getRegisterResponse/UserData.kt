package com.example.ecommerceapp.data.model.getRegisterResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("mobile")
    @Expose
    var data1: String? = null
        set(message) {
            this.message = message
        }

    @SerializedName("account")
    @Expose
    var account = 0
}
