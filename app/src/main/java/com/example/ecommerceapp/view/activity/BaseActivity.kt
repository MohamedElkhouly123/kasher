package com.example.ecommerceapp.view.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.data.api.ApiClient.apiClient
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadData
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadLanguage
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadUserData2
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SaveLanguage
import com.example.ecommerceapp.data.local.SharedPreferencesManger.USER_TOKEN
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.utils.GeneralRequest.makeGeneralAction
import com.example.ecommerceapp.utils.GeneralRequest.makeGeneralActionWithoutProgress
import com.example.ecommerceapp.view.fragment.BaseFragment
import retrofit2.Call
import java.util.*

open class BaseActivity : AppCompatActivity() {

    var baseFragment: BaseFragment? = null

    //    ​
    override fun onBackPressed() {
        try {
            baseFragment!!.onBack()
        } catch (e: Exception) {
        }
    }

    //​
    fun superBackPressed() {
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        languageToLoad = LoadLanguage(this, "LANGUAGE")
        //        Toast.makeText(this, ""+languageCode, Toast.LENGTH_SHORT).show();
      if (languageToLoad == null || languageToLoad?.length == 0) {
          languageToLoad = LoadLanguage(this, "LANGUAGE")
            if (languageToLoad == null || languageToLoad!!.isEmpty() || languageToLoad.equals("")||languageToLoad?.length==0
            ) {
                languageToLoad = "ar"
                val locale = Locale(languageToLoad)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
//                Toast.makeText(this, "onCreate: 1" + languageCode, Toast.LENGTH_SHORT).show()
                this.resources.updateConfiguration(config, this.resources.displayMetrics)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (languageToLoad == null || languageToLoad?.length==0) {
            languageToLoad =LoadLanguage(this, "LANGUAGE")
            if (languageToLoad == null || languageToLoad!!.isEmpty() || languageToLoad.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                languageToLoad = "ar"
                val locale = Locale(languageToLoad)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
//                Toast.makeText(this, "onCreate: 1" + languageCode, Toast.LENGTH_SHORT).show()
                this.resources.updateConfiguration(config, this.resources.displayMetrics)
                SaveLanguage(this, "LANGUAGE", languageToLoad)
            }
        }
    }

    fun getUserData(token: String) {
        var clientData = LoadUserData2(this@BaseActivity)
        var userToken = LoadData(this@BaseActivity, USER_TOKEN)
        val basicAuthorization = "Bearer $userToken"
        var generalDataSendedModel = GeneralDataSendedModel()
        generalDataSendedModel!!.activity=this@BaseActivity
        val tokenCall: Call<GetGeneralResponse?>
        tokenCall = apiClient?.saveFireBaseToken(token)!!
        makeGeneralActionWithoutProgress(generalDataSendedModel,tokenCall)
    }

    companion object{
        var languageToLoad: String?=null

    }


}