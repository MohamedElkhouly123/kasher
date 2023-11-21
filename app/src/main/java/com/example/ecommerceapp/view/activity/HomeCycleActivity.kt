package com.example.ecommerceapp.view.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadBoolean
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadData
import com.example.ecommerceapp.data.local.SharedPreferencesManger.LoadLanguage
import com.example.ecommerceapp.data.local.SharedPreferencesManger.REMEMBER_ME
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SELECTED
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.ActivityHomeCycleBinding
import com.example.ecommerceapp.utils.ConnectionLiveData
import com.example.ecommerceapp.utils.Dialogs.DialogLogin
import com.example.ecommerceapp.utils.GpsServiece
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.directionHelper.TaskLoadedCallback
import com.example.ecommerceapp.utils.netWork.InternetState
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.google.android.gms.location.FusedLocationProviderClient

import me.ibrahimsn.lib.SmoothBottomBar
import org.aviran.cookiebar2.CookieBar
import java.util.*


class HomeCycleActivity : BaseActivity(),
    TaskLoadedCallback {
    val userDataAuth: Boolean=false
    private lateinit var binding: ActivityHomeCycleBinding
    private var loadingStarted: Boolean?=false
    private var counter: CountDownTimer?=null
    var gpsCanceled: Boolean?=false
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var bundle: Bundle?=null
    private var appLink: String?=null
    private var extras: Bundle?=null
    var addressList= mutableListOf<AddressesForRoom>()
    var generalDataSendedModelPub: GeneralDataSendedModel=GeneralDataSendedModel()
    private var mConnReceiver: BroadcastReceiver? = null
    private var currentLocationIsSelected: String?=null
    var addressSelectedItem: AddressesForRoom=AddressesForRoom()
//    var addressSelectedItem2: AddressesForRoom=AddressesForRoom()

    //    @BindView(R.id.nav_view)
    var navView: SmoothBottomBar? = null
    var noWifiAndServerError: LinearLayout? = null
    var homeActivityLayoutParent: ConstraintLayout? = null
    var navController: NavController? = null
    var cld: ConnectionLiveData? = null
    private var mLocationPermissionsGranted = false
    private val mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    //    init {
//        System.loadLibrary("api-keys")
//    }
//    external fun getKeys() : String
//    init {
//        System.loadLibrary("fubar")
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        languageToLoad = LoadLanguage(this, "LANGUAGE")
        currentLocationIsSelected = LoadData(this, SELECTED)
        extras = intent.extras
        if (extras != null) {
//            type = extras.getString("type")
            appLink = extras?.getString("appLink")
//            showToast(this, appLink)
            //The key argument here must match that used in the other activity
        }
        if (languageToLoad == null||languageToLoad?.length==0) {
            languageToLoad = "ar"
        }
        generalDataSendedModelPub.appLanguage= languageToLoad as String
        if("yes".equals(currentLocationIsSelected)) {
            addressSelectedItem.selected = true
        }else{
            addressSelectedItem.selected = false
        }
        super.onCreate(savedInstanceState)

        binding = ActivityHomeCycleBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        navView=findViewById(R.id.nav_view)
        initUi()
    }


    private fun initUi() {

        noWifiAndServerError=binding.noInternetConection.noWifiAndServerError
        binding.noInternetConection.noWifiTryAgainBtn.setOnClickListener {
            checkInternet(true)

        }
        checkNetworkConnection()
        locationPermission
        mConnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                checkFromLogin()
                checkInternet(false)
//                showServerError(generalDataSendedModelPub)
            }
        }
        registerReceiver(mConnReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        if (!runtime_permissions()) {
            val intent = Intent(application, GpsServiece::class.java)
            startService(intent)
            if (broadcastReceiver == null) {
                broadcastReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        Toast.makeText(
                            context,
                            "" + intent.extras!!["coordinates"],
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            registerReceiver(broadcastReceiver, IntentFilter("location_update"))
        }
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale

        setupSmoothBottomMenu()
//        val navHostFragmentWorker =
//            supportFragmentManager.findFragmentById(R.id.home_activity_fragment_normal) as NavHostFragment?
//        setupWithNavController(navView!!, navHostFragmentWorker?.navController!!)





        this@HomeCycleActivity.resources.updateConfiguration(
            config,
            this@HomeCycleActivity.resources.displayMetrics
        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.statusBarColor = Color.TRANSPARENT
//            window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun showServerError(generalDataSendedModel: GeneralDataSendedModel) {
        binding.noInternetConection.noWifiAndServerErrorImg.setImageResource(R.drawable.server_error)
//        this.no_wifi_and_server_error_img.visibility=View.GONE
//        this.no_wifi_and_server_error_img2.visibility=View.VISIBLE
        binding.noInternetConection.noWifiAndServerErrorTv1.text=getString(R.string.server_error)
        binding.noInternetConection.noWifiAndServerErrorTv2.text=getString(R.string.server_error3)
//        this.no_wifi_and_server_error_tv2.visibility=View.GONE
        noWifiAndServerError!!.visibility=View.VISIBLE
    }
    private fun hideServerError() {
        noWifiAndServerError!!.visibility=View.GONE
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

    private fun checkFromLogin() {
        var clientData = SharedPreferencesManger.LoadUserData2(this@HomeCycleActivity)
        if (clientData != null && LoadBoolean(this@HomeCycleActivity, REMEMBER_ME)&&generalDataSendedModelPub?.startLogin!!) {
            generalDataSendedModelPub?.tryAgainCall?.tryAgainCall(generalDataSendedModelPub)
            generalDataSendedModelPub?.startLogin=false
        }
    }

    fun checkInternet(clicked: Boolean) {
        if (!InternetState.isConnected(this)) {
            if(clicked) {
                HelperMethod.showCookieMsg(
                    getString(R.string.warning),
                    getString(R.string.error_inter_net),
                    this@HomeCycleActivity,
                    R.color.red2,
                    CookieBar.TOP
                )
            }
            noWifiAndServerError!!.visibility=View.VISIBLE
        }else{
            generalDataSendedModelPub?.tryAgainCall?.tryAgainCall(generalDataSendedModelPub)
            noWifiAndServerError!!.visibility=View.GONE
        }
    }

    fun setupSmoothBottomMenu() {

        val navHostFragmentWorker =
            supportFragmentManager.findFragmentById(R.id.home_activity_fragment_normal) as NavHostFragment?
//        setupActionBarWithNavController(navHostFragmentWorker?.navController!!)
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.normal_bottom_nav_menu)
        val menu = popupMenu.menu
        navView!!.setupWithNavController(menu, navHostFragmentWorker?.navController!!)
        navController = findNavController(this, R.id.home_activity_fragment_normal)
        navController!!.addOnDestinationChangedListener { _, destination, _ ->
//            if(destination.id == R.id.ordersDetailsFragment) {
//                if (isInorderDetails){
//                    navController!!.navigate(R.id.homeFragment)
//                    isInorderDetails=false
//                }
//                Log.d("d", "wwwwwwwwwwwwww")
//
//            } else {
//
//            }
        }
        if (appLink != null && appLink?.length!! >0) {
            val arrOfStr: Array<String> = appLink?.split("/")?.toTypedArray()!!
            var linkId = arrOfStr[arrOfStr.size - 1]
            linkId = linkId.replace("7e237eueefjsdjksf932ek64d5", "")
//            showToast(this, linkId+"");
            bundle = Bundle()
            generalBundelDataToSend = GeneralBundelDataToSend()
            generalBundelDataToSend?.from="link"
            generalBundelDataToSend?.linkId=linkId
            bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
            if (appLink?.contains("profile")!! || appLink?.contains("courseData")!!) {
                navController?.navigate(R.id.productDetailsFragment, bundle)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(application)
        cld!!.observe(this, { aBoolean ->
            if (aBoolean) {
                homeActivityLayoutParent?.visibility = View.VISIBLE
                noWifiAndServerError?.visibility = View.GONE
            } else {
                homeActivityLayoutParent?.visibility = View.GONE
                noWifiAndServerError?.visibility = View.VISIBLE
            }
        })
    }

    private val locationPermission: Unit
        private get() {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        this.applicationContext,
                        COURSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionsGranted = true
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

    fun hidenav() {
        try {
            navView?.visibility = View.GONE
            binding.homeActivityView.visibility = View.GONE
        }catch (e:Exception){}
    }

    fun iNVISIBLE() {
        navView!!.visibility = View.INVISIBLE
    }

    fun showenav() {
        try{
            if (navView != null) {
                navView!!.visibility = View.VISIBLE
                binding.homeActivityView.visibility = View.VISIBLE
            }
        }catch (e:Exception){}
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
    }

//    override fun onResume() {
//        super.onResume()
//    }

    private fun runtime_permissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
        if (mConnReceiver != null) {
            unregisterReceiver(mConnReceiver)
        }
    } //    @Override

    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        if(requestCode == 100){
    //            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
    //                Intent intent=  new Intent(getApplication(), GpsServiece.class);
    //                startService(intent);
    //            }else {
    //                runtime_permissions();
    //            }
    //        }
    //    }
    companion object {
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
        private const val DEFAULT_ZOOM = 15f
    }

    override fun onTaskDone(vararg values: Any?) {
        TODO("Not yet implemented")
    }

    fun checkUserAuthAndInternetFun(generalDataSendedModel: GeneralDataSendedModel) {
        generalDataSendedModelPub=generalDataSendedModel
        if (generalDataSendedModel.startLogin == true){
            val dialog = DialogLogin(generalDataSendedModel)
            dialog.show(this!!.supportFragmentManager, "")
        }else{
            checkInternet(true)
        }
    }

    fun openCheckGps() {
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&!gpsSettingIsOpened) {
//            requireActivity().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//            gpsSettingIsOpened=true
////                    showToast(activity, "no");
//        }
    }
//    override fun onTaskDone(vararg values: Any) {}

}