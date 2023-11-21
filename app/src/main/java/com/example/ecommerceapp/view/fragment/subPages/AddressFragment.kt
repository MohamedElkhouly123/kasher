package com.example.ecommerceapp.view.fragment.subPages

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.AddressesAdapter
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentAddressBinding
import com.example.ecommerceapp.databinding.FragmentBinBinding
import com.example.ecommerceapp.utils.GPSTracker
import com.example.ecommerceapp.utils.HelperMethod.showToast
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.fixedRateTimer


class AddressFragment : BaseFragment(), MakeChangeInFragment, TryAgainOncall,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private lateinit var binding: FragmentAddressBinding
    private var root: View? = null
    private var gpsOpened2: Boolean?=false
    private lateinit var timer: Timer
    private lateinit var locationManager: LocationManager
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var locationNotSaved: Boolean?=true
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var fragmentAddressCardsShadowImg: ImageView?=null
    private var fragmentAddressSavedAddressesTv: TextView?=null
    private var fragmentAddressCardsAddTitleTv: TextView?=null
    private var fragmentAddressCardsRvLy: LinearLayout?=null
    private var noResultTv: TextView?=null
    private var type: String? = null
    private var adressesList= mutableListOf<AddressesForRoom>()
    private var dataBase: DataBaseKotlin? = null

    private lateinit var adressesAdapter: AddressesAdapter
    private var FragmentAddressesCardsRv: RecyclerView?=null
    private lateinit var bundle: Bundle
    private var gpsChangesReceiver: BroadcastReceiver? = null
    private lateinit var gpsTracker: GPSTracker


    private var gLayout: GridLayoutManager? = null
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()

    }

    private fun initListener() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this.arguments != null) {
            type = this.requireArguments().getString("type")
            generalBundelDataToSend = this.requireArguments().getSerializable("generalBundelDataToSend") as GeneralBundelDataToSend?

//            adressesList =
//                (this.arguments!!.getSerializable("adressesList") as MutableList<AddressesForRoom>?)!!

        }
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        initData(root!!)
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
    }

    private fun initData(root: View) {

        bundle = Bundle()
        dataBase = DataBaseKotlin.getInstance(requireContext()!!)
        homeCycleActivity?.hidenav()
        buildGoogleApiClient()
        fragmentAddressCardsRvLy=binding.fragmentAddressCardsRvLy
        FragmentAddressesCardsRv=binding.fragmentAddressCardsRv
        fragmentAddressCardsAddTitleTv=binding.fragmentAddressCardsAddTitleTv
        fragmentAddressSavedAddressesTv = binding.fragmentAddressSavedAddressesTv
        fragmentAddressCardsShadowImg = binding.fragmentAddressCardsShadowImg

        noResultTv=binding.notFoundResultError.noResultErrorTitle
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
//        generalDataSendedModel!!.fragment_sr_refresh=binding.re
        generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
        generalDataSendedModel!!.makeChangeInFragment=this
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel!!.maxPage=maxPage
        generalDataSendedModel!!.type=type
        if ("home".equals(type)) {
            binding.fragmentAddAddressMainTitleTv.text=getText(R.string.delivery_place)
            fragmentAddressSavedAddressesTv?.text=getString(R.string.select_delevery_location)
        }

//        gpsChangesReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                checkGpsThenDoChanges()
//            }
//        }
//        requireContext().registerReceiver(gpsChangesReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        onClick(root)
        initAddressesRv()
    }
    override fun onStop() {
        // notice here that I keep a reference to the task being executed as a class member:
//        try{
//            timer.cancel()
//        }catch (e:Exception){}
        super.onStop()
    }
    private fun checkGpsThenDoChanges() {
        try {
//            showToast(requireActivity(), "ok"+ generalDataSendedModel?.gpsOpened)
            if ("GpsTracer".equals(generalDataSendedModel?.from)) {
//                 try{
//                     timer.cancel()
//                 }catch (e:Exception){}
                currentLocationSelected()
//                    showToast(requireActivity(), "ok" +   homeCycleActivity?.addressSelectedItem?.lang)
            }else {
                startTrace()
//                    gpsTracker = GPSTracker(generalDataSendedModel)
//                    gpsTracker.startTrace()
//                    if (generalDataSendedModel?.addressSelectedItem?.lang == 0.0 && generalDataSendedModel?.addressSelectedItem?.lat == 0.0) {
////                    generalDataSendedModel?.addressSelectedItem!!.title=
//                    } else {
//                        showToast(
//                            requireActivity(),
//                            "ok2" + homeCycleActivity?.addressSelectedItem?.lang
//                        )
//                        currentLocationSelected()
//                    }
            }
        }catch (e:Exception){
//            showToast(requireActivity(), "ok" + e.toString())
        }
    }

    private fun currentLocationSelected() {

//        showToast(requireActivity(), ""+generalDataSendedModel?.addressSelectedItem?.address +"ok1")
        homeCycleActivity?.addressSelectedItem =
            generalDataSendedModel?.addressSelectedItem!!
        SharedPreferencesManger.SaveData(
            requireActivity(),
            SharedPreferencesManger.SELECTED,
            "yes"
        )
        generalDataSendedModel = GPSTracker.getAddressWithDetails(
            generalDataSendedModel!!
        )
//
        for (i in 0 until adressesList.size) {
            adressesList[i].selected = false
            dataBase!!.addNewOrderItemDao()!!.update(adressesList[i])
//            showToast(requireActivity(), "no" +   gpsTracker?.getLongitude())
        }
        generalDataSendedModel?.gpsOpened = false


        onBack()
    }


    private fun initAddressesRv() {
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentAddressesCardsRv!!.layoutManager = gLayout
        adressesAdapter = AddressesAdapter(adressesList, generalDataSendedModel)
        generalDataSendedModel?.addressAdapter=adressesAdapter
        FragmentAddressesCardsRv!!.adapter = adressesAdapter
        //            showToast(getActivity(), "success adapter");
        adressesList.clear()
        if (adressesList.size == 0) {
            getAllAddressesList()
        }
    }

    override fun onDestroy() {
        try {
            timer?.cancel()
        }catch (e :Exception){}
        super.onDestroy()

//        if (gpsChangesReceiver != null) {
//            requireContext().unregisterReceiver(gpsChangesReceiver)
//        }
    }

    override fun onDestroyView() {
        try {
            timer?.cancel()
        }catch (e :Exception){}
        super.onDestroyView()
    }

    private fun getAllAddressesList() {
        Executors.newSingleThreadExecutor()
            .execute {
                reInit()
                adressesList.addAll(dataBase!!.addNewOrderItemDao()!!.allAddressesItems as ArrayList<AddressesForRoom>)
                homeCycleActivity?.addressList!!.clear()
                homeCycleActivity?.addressList!!.addAll(adressesList)
                try {
                    if ("home".equals(type)) {
                        homeCycleActivity?.addressSelectedItem?.title = getString(R.string.current_location)
//                        if ("home".equals(type)&&homeCycleActivity?.addressSelectedItem!!.address!=null&&homeCycleActivity?.addressSelectedItem!!.address?.trim()!!.length!=0) {
                        generalDataSendedModel?.type2="add"
                        adressesList.add(0, homeCycleActivity?.addressSelectedItem!!)
                    }
                    Log.i(ContentValues.TAG+""+adressesList?.size, adressesList[0].address.toString())
                }catch (e:Exception){}
                try{
                    if(adressesList.size>0){
                        fragmentAddressCardsRvLy?.visibility=View.VISIBLE
                        noResultTv?.visibility=View.GONE
                        fragmentAddressCardsAddTitleTv?.visibility=View.GONE
                        adressesAdapter.notifyDataSetChanged()
                    }else{
                        doChanges(generalDataSendedModel!!)
                        noResultTv?.visibility=View.VISIBLE
                    }
                }catch (e:Exception){}
            }
    }

    private fun reInit() {

        adressesList = mutableListOf<AddressesForRoom>()
        adressesAdapter = AddressesAdapter(
            adressesList,
            generalDataSendedModel
        )
        FragmentAddressesCardsRv!!.adapter = adressesAdapter

    }


    override fun onResume() {
        super.onResume()
        var locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&& locationNotSaved == true) {
            mGoogleApiClient.connect()
        }

    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    fun onClick(root: View) {

        binding.backBtn.setOnClickListener {
            super.onBack()
        }
        binding.fragmentAddAddressBtu.setOnClickListener {
            bundle.putString("mark", "show_all_category")
            bundle.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
            Navigation.findNavController(root!!).navigate(R.id.mapFragment, bundle)
        }
    }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {
        this.generalDataSendedModel=generalDataSendedModel
        if(adressesAdapter.adressesForRoomList.size==0){
            fragmentAddressCardsRvLy?.visibility=View.GONE
            fragmentAddressCardsAddTitleTv?.visibility=View.VISIBLE
            fragmentAddressCardsShadowImg?.visibility=View.GONE
        }

        locationManager = activity?.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        this.generalDataSendedModel?.addressSelectedItem!!.selected = true
        gpsOpened2 = this.generalDataSendedModel?.gpsOpened2
        try {
            timer = fixedRateTimer("timer", false, 0, 5000) {
                try {
                    requireActivity().runOnUiThread {
//
//                    }
                        adressesAdapter.startLoading()

                        if (generalDataSendedModel?.gpsOpened == true) {
                            checkGpsThenDoChanges()
                        } else {
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                if (gpsOpened2 == true) {
//                                showToast(
//                                    requireActivity(),
//                                    "" + generalDataSendedModel?.gpsOpened + "ok" + generalDataSendedModel?.gpsOpened
//                                )
                                    startTrace()
                                }
                            }else{
                                adressesAdapter.stopLoading()

                            }
                        }
                    }
                } catch (e: Exception) {

                }
            }
        } catch (e: Exception) {}

//        this.generalDataSendedModel?.gpsOpened2=false
//        gpsOpened2=false
//        this.generalDataSendedModel?.gpsOpened == false

    }

    override fun onBack() {
//        try{
//            timer.cancel()
//        }catch (e:Exception){}
        super.onBack()
    }

    private fun startTrace() {

        gpsTracker = GPSTracker(generalDataSendedModel)
        gpsTracker.startTrace()
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
    }

    override fun onConnected(p0: Bundle?) {
        displayLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
//        showToast(activity,"onConnectionSuspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
//        showToast(activity,"onConnectionFailed")

    }

    private fun displayLocation() {
       if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           var mLastLocation =  LocationServices.FusedLocationApi
               .getLastLocation(mGoogleApiClient)
           //        showToast(activity,"ssss")

           if (mLastLocation != null) {
               val latitude: Double = mLastLocation.getLatitude()
               val longitude: Double = mLastLocation.getLongitude()
//            showToast(activity,"  "+latitude+"    "+longitude)
               locationNotSaved=false
           } else {
//            showToast(activity,"(Couldn't get the location. Make sure location is enabled on the device)")
           }
               return
        } else {

        }


    }
}