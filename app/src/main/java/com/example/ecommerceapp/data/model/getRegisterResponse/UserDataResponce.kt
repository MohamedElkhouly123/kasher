package com.example.ecommerceapp.data.model.getRegisterResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDataResponce {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("user_type")
    @Expose
    var userType: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("work_without_sim")
    @Expose
    var work_without_sim = 0

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("profile_img")
    @Expose
    var profileImg: String? = null

    @SerializedName("cover_img")
    @Expose
    var coverImg: String? = null

    @SerializedName("university")
    @Expose
    var university: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("mobile_privacy")
    @Expose
    var mobile_privacy: String? = null

    @SerializedName("email_privacy")
    @Expose
    var email_privacy: String? = null

    @SerializedName("bio")
    @Expose
    var bio: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("joined_at")
    @Expose
    var joinedAt: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("email_verified")
    @Expose
    var emailVerified: Int? = null

    @SerializedName("mobile_verified")
    @Expose
    var mobileVerified: Int? = null

    @SerializedName("del_schedule")
    @Expose
    var delSchedule: String? = null

    @SerializedName("account_status")
    @Expose
    var accountStatus: String? = null

    @SerializedName("notifications_count")
    @Expose
    var notifications_count = 0

}