package com.example.ecommerceapp.view.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadData
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadUserData2
import com.example.ecommerceapp.data.local.SharedPreferencesManger.USER_FireBase_TOKEN
import com.example.ecommerceapp.data.model.ClientFireBaseToken
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getRegisterResponse.UserData
import com.example.ecommerceapp.databinding.ActivityHomeCycleBinding
import com.example.ecommerceapp.databinding.ActivitySplashCycleBinding
import java.util.*

class SplashCycleActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashCycleBinding
    private var fireBaseToken: String?=null
    private var bundle: Bundle= Bundle()
    private var clientData: UserData?=null
    private var appLink: String? =null
    private var appLinkAction: String?=null
    var TAG = "SplashScrean"
    var sideAnimation: Animation? = null

//    @BindView(R.id.fragment_splash_logo_image)
    var fragmentSplashLogoImage: ImageView? = null



    private var isTransactionSafe = false
    private var isStart = false

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientData = LoadUserData2(this@SplashCycleActivity)
        //        showToast(SplashCycleActivity.this, menuFragment+"");
        fireBaseToken = LoadData(this@SplashCycleActivity, USER_FireBase_TOKEN)


        binding = ActivitySplashCycleBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        //        replaceFragment(getSupportFragmentManager(), R.id.splash_frame, new SplashPageFragment());
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent = intent
        appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data
        if (appLinkData != null) {
            appLink = appLinkData.toString()
//            showToast(this,  appLink)
        }
        initionAnimation()
        setAnimation()
        StartSplash()
    }

    fun StartSplash() {
        Handler().postDelayed({
            if (isTransactionSafe) {
                try {
//                    if (clientData != null && LoadBoolean(this@SplashCycleActivity, REMEMBER_ME)) {

                    if (fireBaseToken == null) {
                        var generalDataSendedModel = GeneralDataSendedModel()
                        generalDataSendedModel!!.activity=this@SplashCycleActivity
                        ClientFireBaseToken( generalDataSendedModel)
                    }
                        languageToLoad =
                            "ar"
                        val locale =
                            Locale(languageToLoad)
                        Locale.setDefault(locale)
                        val config =
                            Configuration()
                        config.locale = locale
                        val res = resources
                        val dm = res.displayMetrics
                        res.updateConfiguration(config, dm)
                        onConfigurationChanged(config)
                        this@SplashCycleActivity.resources
                            .updateConfiguration(config, res.displayMetrics)
                        res.displayMetrics
                        config.locale = locale
                        val intent = Intent(this, HomeCycleActivity::class.java)
                        if(appLink!=null) {
                            bundle.putSerializable("appLink", appLink)
                            intent.putExtras(bundle)
                        }
                    startActivity(intent)
//                    }else{
//                        startActivity(Intent(this, UserCycleActivity::class.java))
//                    }
                    isStart = false
                    finish()
                } catch (e: Exception) {
                    Log.d(TAG, "Exception __error " + e.message)
                } finally {
                }

            }
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
        if (isStart) {
            StartSplash()
        }
    }

    public override fun onPostResume() {
        super.onPostResume()
        isTransactionSafe = true
    }

    public override fun onPause() {
        super.onPause()
        isTransactionSafe = false
    }

    override fun onStop() {
        super.onStop()
        isStart = true
    }

    private fun setAnimation() {
        fragmentSplashLogoImage?.animation = sideAnimation
    }

    private fun initionAnimation() {
        try {
            fragmentSplashLogoImage=binding.fragmentSplashLogoImage
            sideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        } catch (e: Exception) {
            Log.d("", "" + e.message)
        }
    }
}