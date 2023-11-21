package com.example.ecommerceapp.data.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.ecommerceapp.data.model.getRegisterResponse.UserData
import com.example.ecommerceapp.data.model.getRegisterResponse.UserDataResponce
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.google.gson.Gson

object SharedPreferencesManger {
    var sharedPreferences: SharedPreferences? = null
    var USER_DATA = "USER_DATA"
    var ADRESS_DATA = "ADRESS_DATA"
    var REMEMBER_ME = "REMEMBER_ME"
    var USER_PASSWORD = "USER_PASSWORD"
    var USER_TOKEN = "USER_TOKEN"
    var CLIENT = "CLIENT"
    var TABPOSION = "TABPOSION"
    var SELECTED = "SELECTED"
    var GOOGLECHECK = "GOOGLECHECK"
    var USER_FireBase_TOKEN = "USER_FireBase_TOKEN"

    fun setSharedPreferences(activity: Activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                "Sofra", Context.MODE_PRIVATE
            )
        }
    }

    fun SaveData(activity: Activity, data_Key: String?, data_Value: String?) {
        setSharedPreferences(activity)
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            editor.putString(data_Key, data_Value)
            editor.commit()
        } else {
            setSharedPreferences(activity)
        }
    }

    fun LoadLanguage(activity: Activity, data_Key: String?): String? {
        setSharedPreferences(activity)
        return sharedPreferences!!.getString(data_Key, "ar")
    }

    fun SaveLanguage(activity: Activity, data_Key: String?, data_Value: String?) {
        setSharedPreferences(activity)
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            editor.putString(data_Key, data_Value)
            editor.commit()
        } else {
            setSharedPreferences(activity)
        }
    }

    fun SaveData(activity: Activity, data_Key: String?, data_Value: Boolean) {
        setSharedPreferences(activity)
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            editor.putBoolean(data_Key, data_Value)
            editor.commit()
        } else {
            setSharedPreferences(activity)
        }
    }

    fun SaveData(activity: Activity, data_Key: String?, data_Value: Any?) {
        setSharedPreferences(activity)
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            val gson = Gson()
            val StringData = gson.toJson(data_Value)
            editor.putString(data_Key, StringData)
            editor.commit()
        }
    }

    fun LoadData(activity: Activity, data_Key: String?): String? {
        setSharedPreferences(activity)
        return sharedPreferences!!.getString(data_Key, null)
    }

    fun LoadUserData(activity: Activity): UserDataResponce? {
        setSharedPreferences(activity)
        var loginData: UserDataResponce? = null
        val gson = Gson()
        loginData = gson.fromJson(
            LoadData(
                activity,
                USER_DATA
            ),
            UserDataResponce::class.java
        )


        return loginData
    }

    fun LoadUserData2(activity: Context): UserData? {
        setSharedPreferences(activity as Activity)
        var loginData: UserData? = null
        val gson = Gson()
        loginData = gson.fromJson(
            LoadData(
                activity, USER_DATA
            ),
            UserData::class.java
        )
        return loginData
    }

    fun LoadLastCurrentLocationData(activity: Context): AddressesForRoom? {
        setSharedPreferences(activity as Activity)
        var lastCurrentLocationData: AddressesForRoom? = null
        val gson = Gson()
        lastCurrentLocationData = gson.fromJson(
            LoadData(
                activity, ADRESS_DATA
            ),
            AddressesForRoom::class.java
        )
        return lastCurrentLocationData
    }

    fun LoadBoolean(activity: Activity, data_Key: String?): Boolean {
        setSharedPreferences(activity)
        return sharedPreferences!!.getBoolean(data_Key, false)
    }

    fun clean(activity: Activity) {
        setSharedPreferences(activity)
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            editor.clear()
            editor.commit()
        }
    }
}