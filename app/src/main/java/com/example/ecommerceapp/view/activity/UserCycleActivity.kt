package com.example.ecommerceapp.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.ActivitySplashCycleBinding
import com.example.ecommerceapp.databinding.ActivityUserCycleBinding
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.replaceFragment
import com.example.ecommerceapp.utils.netWork.InternetState
import com.example.ecommerceapp.view.fragment.userCycle.LoginFragment

import org.aviran.cookiebar2.CookieBar

class UserCycleActivity : BaseActivity() {
    private lateinit var binding: ActivityUserCycleBinding
    private var counter: CountDownTimer?=null
    private var loadingStarted: Boolean=false
    private var mConnReceiver: BroadcastReceiver? = null
    var noWifi: LinearLayout? = null
    private var generalDataSendedModelPub: GeneralDataSendedModel = GeneralDataSendedModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCycleBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        replaceFragment(supportFragmentManager, R.id.user_frame, LoginFragment())
        noWifi = binding.noInternetConection.noWifiAndServerError
        binding.noInternetConection.noWifiTryAgainBtn.setOnClickListener {
            checkInternet(true)
        }
        mConnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
//                checkInternet(false)
            }
        }
            registerReceiver(mConnReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.statusBarColor = Color.TRANSPARENT
//            window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    private fun checkInternet(clicked: Boolean) {
        if (!InternetState.isConnected(this)) {
            if(clicked) {
                HelperMethod.showCookieMsg(
                    getString(R.string.warning),
                    getString(R.string.error_inter_net),
                    this@UserCycleActivity,
                    R.color.red2,
                    CookieBar.TOP
                )
            }
            noWifi!!.visibility=View.VISIBLE
        }else{
            generalDataSendedModelPub?.tryAgainCall?.tryAgainCall(generalDataSendedModelPub)
            noWifi!!.visibility=View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mConnReceiver != null) {
            unregisterReceiver(mConnReceiver)
        }
    }

    private fun showServerError(generalDataSendedModel: GeneralDataSendedModel) {
        binding.noInternetConection.noWifiAndServerErrorImg.setImageResource(R.drawable.server_error)
//        this.no_wifi_and_server_error_img.visibility=View.GONE
//        this.no_wifi_and_server_error_img2.visibility=View.VISIBLE
        binding.noInternetConection.noWifiAndServerErrorTv1.text=getString(R.string.server_error)
        binding.noInternetConection.noWifiAndServerErrorTv2.text=getString(R.string.server_error3)
//        this.no_wifi_and_server_error_tv2.visibility=View.GONE
        noWifi!!.visibility=View.VISIBLE
    }
    private fun hideServerError() {
        noWifi!!.visibility=View.GONE
    }
    fun showLoading() {
        binding.reloadPage.homeLoadingWait.visibility=View.VISIBLE
        loadingStarted=true
        loadingText()
    }

    fun hideLoading() {
        binding.reloadPage.homeLoadingWait.visibility=View.GONE
        counter?.cancel()
        loadingStarted=true
    }

    private fun loadingText() {
//        home_waiting_load_tv.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

        var i = 1
        if(loadingStarted!!) {
            counter = object : CountDownTimer(1000000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (i == 1) {
                        binding.reloadPage.homeWaitingLoadTv.setText(" . ")
                        i = 2
                    } else if (i == 2) {
                        binding.reloadPage.homeWaitingLoadTv.setText(" .. ")
                        i = 3
                    } else if (i == 3) {
                        binding.reloadPage.homeWaitingLoadTv.setText(" ... ")
                        i = 1
                    }
                }

                override fun onFinish() {
                    if(loadingStarted!!) {
                        loadingText()
                    }
                }
            }.start()
        }
    }

    fun checkInternetFun(generalDataSendedModel: GeneralDataSendedModel) {
        generalDataSendedModelPub=generalDataSendedModel
    }
}