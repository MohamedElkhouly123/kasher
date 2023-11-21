package com.example.ecommerceapp.view.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.R
import com.example.ecommerceapp.utils.HelperMethod.showCookieMsg
import com.example.ecommerceapp.utils.netWork.InternetState
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.activity.UserCycleActivity
import org.aviran.cookiebar2.CookieBar
import java.util.*

open class BaseFragment : Fragment() {
    var baseActivity: BaseActivity? = null
    var homeCycleActivity: HomeCycleActivity? = null
    var userCycleActivity: UserCycleActivity? = null

    private var activity: Context? = null
    fun setUpActivity() {
        baseActivity = getActivity() as BaseActivity?
        baseActivity?.baseFragment = this
        try {
            homeCycleActivity = getActivity() as HomeCycleActivity?
        } catch (e: Exception) {
        }
    }

    fun setUpUserActivity() {
        baseActivity = getActivity() as BaseActivity?
        baseActivity?.baseFragment = this
        try {
            userCycleActivity = getActivity() as UserCycleActivity?
        } catch (e: Exception) {
        }
    }

    open fun onBack() {

        baseActivity!!.superBackPressed()
    }

    override fun onStart() {
        super.onStart()
        setUpActivity()
        requireActivity().window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

    }

    fun refreshLanguage() {
        // your language
        val locale = Locale(BaseActivity.languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().resources.updateConfiguration(
            config,
            requireActivity().resources.displayMetrics
        )
    }

    override fun onAttach(context: Context) {
        val locale = Locale(BaseActivity.languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().resources.updateConfiguration(
            config,
            requireActivity().resources.displayMetrics
        )
        super.onAttach(context)
        activity = context
        if (!InternetState.isConnected(requireContext())) {
//            showCookieMsg(
//                getString(R.string.warning), getString(R.string.error_inter_net), getActivity(), R.color.red2,
//                CookieBar.TOP
//            )
        }
        refreshLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpActivity()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun onTouchEvent(event: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }
}