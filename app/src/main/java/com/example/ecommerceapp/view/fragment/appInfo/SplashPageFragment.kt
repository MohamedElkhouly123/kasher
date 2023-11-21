package com.example.ecommerceapp.view.fragment.appInfo

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadUserData2
import com.example.ecommerceapp.data.model.ClientFireBaseToken
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getRegisterResponse.UserData
import com.example.ecommerceapp.databinding.FragmentRateBinding
import com.example.ecommerceapp.databinding.FragmentSplashPageBinding
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import java.util.Locale

class SplashPageFragment : BaseFragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSplashPageBinding
    private var root: View?=null
    var sideAnimation: Animation? = null
    private var fireBaseToken: String?=null
    private var bundle: Bundle= Bundle()
    private var clientData: UserData?=null
    private var appLink: String? =null
    private var appLinkAction: String?=null
    var TAG = "SplashScrean"

    //    @BindView(R.id.fragment_splash_logo_image)
    var fragmentSplashLogoImage: ImageView? = null



    private var isTransactionSafe = false
    private var isStart = false
    var prevStarted: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clientData = SharedPreferencesManger.LoadUserData2(requireActivity())
        //        showToast(SplashCycleActivity.this, menuFragment+"");
        fireBaseToken = SharedPreferencesManger.LoadData(
            requireActivity(),
            SharedPreferencesManger.USER_FireBase_TOKEN
        )
        // Inflate the layout for this fragment
        binding = FragmentSplashPageBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appLinkIntent = requireActivity().intent
        appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data
        if (appLinkData != null) {
            appLink = appLinkData.toString()
//            showToast(this,  appLink)
        }
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.hidenav()
        initionAnimation()
        setAnimation()
        isTransactionSafe=true
        StartSplash()
    }

    override fun onBack() {
        requireActivity().finish()
    }

    fun StartSplash() {
        Handler().postDelayed({
            if (isTransactionSafe) {
                try {
//                    if (clientData != null && LoadBoolean(this@SplashCycleActivity, REMEMBER_ME)) {

                    if (fireBaseToken == null) {
                        var generalDataSendedModel = GeneralDataSendedModel()
                        generalDataSendedModel!!.activity=requireActivity()
                        ClientFireBaseToken( generalDataSendedModel)
                    }
                    BaseActivity.languageToLoad =
                        "ar"
                    val locale =
                        Locale(BaseActivity.languageToLoad)
                    Locale.setDefault(locale)
                    val config =
                        Configuration()
                    config.locale = locale
                    val res = resources
                    val dm = res.displayMetrics
                    res.updateConfiguration(config, dm)
                    onConfigurationChanged(config)
                    requireActivity().resources
                        .updateConfiguration(config, res.displayMetrics)
                    res.displayMetrics
                    config.locale = locale
                    navController.navigate(R.id.homeFragment2)
//                    val intent = Intent(requireContext(), HomeCycleActivity::class.java)
//                    if(appLink!=null) {
//                        bundle.putSerializable("appLink", appLink)
//                        intent.putExtras(bundle)
//                    }
//                    startActivity(intent)
//                    }else{
//                        startActivity(Intent(this, UserCycleActivity::class.java))
//                    }
                    isStart = false
//                    requireActivity().finish()
                } catch (e: Exception) {
                    Log.d(TAG, "Exception __error " + e.message)
                } finally {
                }

            }
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
//        if (isStart) {
            StartSplash()
//        }
    }

//    public override fun onPostResume() {
//        super.onPostResume()
//        isTransactionSafe = true
//    }


    public override fun onPause() {
        super.onPause()
        isTransactionSafe = false
    }

    override fun onStop() {
        super.onStop()
        isTransactionSafe=true
        isStart = true
    }

    private fun setAnimation() {
        fragmentSplashLogoImage?.animation = sideAnimation
    }

    private fun initionAnimation() {
        try {
            fragmentSplashLogoImage=binding.fragmentSplashLogoImage
            sideAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.side_slide)
        } catch (e: Exception) {
            Log.d("", "" + e.message)
        }
    }
}