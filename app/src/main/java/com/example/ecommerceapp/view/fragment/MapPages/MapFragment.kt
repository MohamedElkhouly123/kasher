package com.example.ecommerceapp.view.fragment.MapPages

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentMapBinding
import com.example.ecommerceapp.databinding.FragmentMoreBinding
import com.example.ecommerceapp.utils.GPSTracker.Companion.getAddressWithDetails
import com.example.ecommerceapp.utils.GPSTracker.Companion.getAddressWithDetails2
import com.example.ecommerceapp.utils.GPSTracker.Companion.requestDeviceLocationSettings
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.DatePickerFragment.Companion.canToggleGPS
import com.example.ecommerceapp.utils.HelperMethod.DatePickerFragment.Companion.turnGPSOn
import com.example.ecommerceapp.utils.HelperMethod.showCookieMsg
import com.example.ecommerceapp.utils.HelperMethod.showToast
import com.example.ecommerceapp.utils.KeyboardUtil
import com.example.ecommerceapp.utils.directionHelper.TaskLoadedCallback
import com.example.ecommerceapp.utils.interfaces.ApiInterface
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.aviran.cookiebar2.CookieBar
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.fixedRateTimer


class MapFragment : BaseFragment(), OnMapReadyCallback, TaskLoadedCallback, TryAgainOncall,LocationListener  {
    private lateinit var binding: FragmentMapBinding
    private lateinit var destLatLng: LatLng
    private lateinit var originLatLng: LatLng
    private var currentLong: Double=1.1265546884
    private var currentLat: Double=1.4545545452
    private var address: String?=null
    private var getAddress: Boolean=false
    private lateinit var fields: List<Place.Field>
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var updateFirstOpen: Boolean?=true
    private var root: View?=null
    private var firstUsed: Boolean?=true
    private var gpsSettingIsOpened2: Boolean=false
    private var dialog2: AlertDialog?=null
    private var first: Boolean=true
    private var timer: Timer?=null
    private var gpsSettingIsOpened: Boolean = false
    private  var locationManager: LocationManager?=null
    var handler: Handler? = null
    private var addressItemData: AddressesForRoom?=null
    private var from: String? = null
    private var dataBase: DataBaseKotlin? = null
    private var currentLocation: Location? = null
    var navController: NavController? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var mLocationPermissionsGranted = false
    private var mMap: GoogleMap? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var langlat2: LatLng? = null
    private var lang = 0.0
    private val lang2 = 45.079162
    private var lat = 0.0
    private val lat2 = 23.885942
    private val origion: LatLng? = null
    private val dest: LatLng? = null
    private var place1: MarkerOptions? = null
    private var place2: MarkerOptions? = null
    private var currentPolyline: Polyline? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    var markerPoints: ArrayList<LatLng> = ArrayList<LatLng>()
    var apiInterface: ApiInterface? = null
    private val polyLineList: List<LatLng>? = null
    private val polylineOptions: PolylineOptions? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            requireActivity().window.statusBarColor = Color.TRANSPARENT
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (this.arguments != null) {
            from = this.requireArguments().getString("from")
            addressItemData = this.requireArguments().getSerializable("addressItemData") as AddressesForRoom?
            generalBundelDataToSend = this.requireArguments().getSerializable("generalBundelDataToSend") as GeneralBundelDataToSend?

        }
        binding = FragmentMapBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
//        KeyboardUtil(requireActivity(), root!!)
        KeyboardUtil(requireActivity(), binding.fragmentMapScroll!!)
        initUi(root!!)
        originLatLng = LatLng(30.557625783683946, 31.00741311907768) // Replace with the actual origin latitude and longitude
        destLatLng = LatLng(30.480535555800724, 31.237464845180515)
//        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl("https://maps.googleapis.com/")
//                .build();
//        apiInterface=retrofit.create(ApiInterface.class);
    }

//    val runnableCode = object: Runnable {
//        override
        fun run2() {
            try {
//                homeCycleActivity?.openCheckGps()
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&!gpsSettingIsOpened) {
//                    dialog2= showDialoge(requireActivity(), "",
//                        getString(R.string.for_better_experience_turnon_gps),
//                        getString(R.string.ok),
//                        getString(R.string.Skip),
//                        { dialog: DialogInterface?, ok: Int -> openCheckGps() }
//                    ) { dialog: DialogInterface, no: Int ->
//                       dialog.dismiss() }
//                    gpsSettingIsOpened=true
//                }
//                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&!gpsSettingIsOpened) {
//                    DEFAULT_ZOOM=15f
//                    goToCurrentLocation(true)
//                    dialog2?.dismiss()
//                    gpsSettingIsOpened=true
//                }
//                if (currentLocation!=null&&gpsSettingIsOpened2) {
//                    zoomToLocation(currentLocation?.getLatitude()!!, currentLocation?.getLongitude()!!)
//                    gpsSettingIsOpened2=false
//                }

//                showToast(activity, "yes");
//                handler?.postDelayed(this, 200)
            }catch (e:Exception){}
        }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    data?.let {
//                        val place = Autocomplete.getPlaceFromIntent(data)
//                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
//                    }
//                }
//                AutocompleteActivity.RESULT_ERROR -> {
//                    // TODO: Handle the error.
//                    data?.let {
//                        val status = Autocomplete.getStatusFromIntent(data)
//                        Log.i(TAG, status.statusMessage ?: "")
//                    }
//                }
//                Activity.RESULT_CANCELED -> {
//                    // The user canceled the operation.
//                }
//            }
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    private fun openCheckGps() {
        if (!(locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER))!!) {
            if (canToggleGPS(requireActivity())){
                turnGPSOn(requireActivity())
            }else {
                requireActivity().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
//                    showToast(activity, "no");
        }    }
//    }

    override fun onResume() {
        super.onResume()
        setUpActivity()
        openGpsDialog()
//        if (currentLocation!=null&&gpsSettingIsOpened2) {
//            zoomToLocation(currentLocation?.getLatitude()!!, currentLocation?.getLongitude()!!)
//            gpsSettingIsOpened2=false
//        }

    }

    private fun openGpsDialog() {
        binding.fragmentMapLoadCurrentLocationWaitTv?.visibility=View.GONE
        binding.fragmentMapLoadCurrentLocationLy?.visibility=View.VISIBLE
        if(firstUsed!!){
            homeCycleActivity?.gpsCanceled=false
            firstUsed=false
        }
        gpsSettingIsOpened=false
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.mapWaitTextView= binding.fragmentMapLoadCurrentLocationWaitTv
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.type="map"
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel?.gpsCanceled=false
        generalDataSendedModel?.homeCycleActivity=homeCycleActivity
        requestDeviceLocationSettings(generalDataSendedModel)

    }

    private fun initUi(root: View) {
//        handler = Handler(Looper.myLooper()!!)
//        handler!!.post(runnableCode)
        dataBase = DataBaseKotlin.getInstance(requireActivity())



        timer= fixedRateTimer("timer",false,0,1000){
            requireActivity().runOnUiThread {
                run2()
            }
        }
        navController = Navigation.findNavController(root)
        homeCycleActivity?.hidenav()
        place1 = MarkerOptions().position(LatLng(27.658143, 85.3199503)).title("Location 1")
        place2 = MarkerOptions().position(LatLng(27.667491, 85.3208583)).title("Location 2")
        try {
            val mapFragment = getChildFragmentManager()
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

//        ToastCreator.onCreateErrorToast(getActivity(), getString(R.string.location_required));
//        toolbarSubView.setVisibility(View.VISIBLE);
//        toolbarSubView.setVisibility(View.VISIBLE);
//        toolbarTitle.setText(getString(R.string.title_activity_maps));
//        backBtn.setOnClickListener(onBackPressed());
            locationPermission

            if ("edit_location".equals(from)) {
                binding.fragmentMapTitleNameTil.editText?.setText(addressItemData?.title)
            }else{
                addressItemData= AddressesForRoom()
            }
            onClick(root)
        } catch (e: Exception) {
            Log.d("requireActivity()", ""+e.message+"  "+e.stackTraceToString())
            showCookieMsg(
                getString(R.string.warning),
                getString(R.string.Incorrect_location),
                getActivity(),
                R.color.red2,
                CookieBar.TOP
            )
        }
//        root!!.fragment_advertisement_search_Et.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(
//                s: CharSequence, start: Int, before: Int,
//                count: Int
//            ) {
//
////                                             Your code .........
//            }
//
//            override fun afterTextChanged(text: Editable) {
//                if (text.trim().length>1) {
////                    getLocationByText2(text.toString())
//
//            }
//        }
//        })
//
        binding.fragmentAdvertisementSearchEt2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            Log.d("actionId", "OnKeyListener: " + keyCode + " - " + KeyEvent.KEYCODE_ENTER)
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                searchLocation(root!!)
                return@OnKeyListener true
            }
            false
        })
        //        try {
//            if (!Places.isInitialized()) {
//                Places.initialize(
//                    requireContext()!!.applicationContext,
//                    getString(R.string.google_maps_eccommerce)
//                )
//            }
//            fields = listOf(Place.Field.ID, Place.Field.NAME)
//
//            root!!.fragment_advertisement_search_Et.setOnClickListener {
//                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                    .build(requireContext())
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
//            }
//        } catch (e: java.lang.Exception) {
//        }
        try {
            if (!Places.isInitialized()) {
                Places.initialize(
                    requireContext()!!.applicationContext,
                    getString(R.string.google_maps_eccommerce)
                )
            }
            // Initialize the AutocompleteSupportFragment.
            val autocompleteFragment =
                getChildFragmentManager().findFragmentById(R.id.fragment_advertisement_search_Et) as AutocompleteSupportFragment?
            autocompleteFragment!!.requireView().background =
                ContextCompat.getDrawable(requireContext()!!, R.drawable.shapauto2)
            autocompleteFragment.setPlaceFields(
                Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG
                )
            )

            autocompleteFragment!!.setCountry("EG")
            Log.i(TAG, "An error occurred: " + "Places.")
//            autocompleteFragment!!.setMenuVisibility(false)
            autocompleteFragment!!.setOnPlaceSelectedListener(object :
                PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    // TODO: Get info about the selected place.
                    val newLatLng: LatLng = place.getLatLng()
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(newLatLng))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(17f))
                }

                override fun onError(status: Status?) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            })
        } catch (e: java.lang.Exception) {
        }
    }


    private fun searchLocation2(root: View) {
        val location = binding.fragmentAdvertisementSearchEt2.text.toString()
//        if (location != null || location != ""||root!!.fragment_advertisement_search_Et.text.toString().trim()?.length!!>0) {
//        }else{
//            showToast(requireActivity(), "please enter search text first")
//        }
    }

    private fun getLocationByText2(location: String) {
        Log.d("=Adress=", location)

        var addressList: List<Address>? = null
        val geocoder = Geocoder(requireContext())
        try {
            addressList = geocoder.getFromLocationName(location, 5)
//            Log.d("=Adress=", addressList!![0].getAddressLine(0))
            for (i in 0 until addressList!!.size) {
                Log.d("=Adress=", addressList[i].subAdminArea)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
//            val address: Address = addressList!![0]
//            val latLng = LatLng(address.getLatitude(), address.getLongitude())
//            zoomToLocation(address.getLatitude(), address.getLongitude())
        }catch (e:Exception){
            showToast(requireActivity(), "no results please enter other words")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=" + getString(
            R.string.google_maps_eccommerce
        )
    }

    //    private void getDirection(String origin ,String destination){
    //        apiInterface.getDirection("deriving","less_deriving",origin,destination,
    //                getString(R.string.google_maps_key)).subscribeOn(Schedulers.io())
    //                .subscribe(new SingleSubscriber<Result>() {
    //                    @Override
    //                    public void onSuccess(Result value) {
    //                        polyLineList=new ArrayList<>();
    //                        List<Route> routeList=value.getRoutes();
    //                        for(Route route:routeList){
    //                            String polyline=route.getOverViewPolyLine().getPoints();
    //                            polyLineList.addAll(decodepoly(polyline));
    //
    //                        }
    //                        polylineOptions=new PolylineOptions();
    //                        polylineOptions.color(getActivity().getResources().getColor(R.color.app_color));
    //                        polylineOptions.width(8);
    //                        polylineOptions.startCap(new ButtCap());
    //                        polylineOptions.jointType(JointType.ROUND);
    //                        polylineOptions.addAll(polyLineList);
    //                        mMap.addPolyline(polylineOptions);
    //                        LatLngBounds.Builder builder=new LatLngBounds.Builder();
    //                        builder.include(dest);
    //
    //                    }
    //
    //                    @Override
    //                    public void onError(Throwable error) {
    //
    //                    }
    //                });
    //
    //    }
    //    private List<LatLng> decodepoly(String encoded){
    //        List<LatLng> poly =new ArrayList<>();
    //        int index =0,len=encoded.length();
    //        int lat=0,lng=0;
    //        while (index<len){
    //            int b,shift=0,result=0;
    //            do{
    //                b=encoded.charAt(index++)-63;
    //                result|=(b & 0x1f) <<shift;
    //                shift +=5;
    //            }while (b>=0x20);
    //            int dlat=((result & 1) !=0? ~(result >> 1) : (result >> 1));
    //            lat +=dlat;
    //            shift=0;
    //            result=0;
    //            do{
    //                b=encoded.charAt(index++)-63;
    //                result |=(b & 0x1f) << shift;
    //                shift +=5;
    //            }while (b>=0x20);
    //            int dlng=((result & 1) !=0 ? ~(result >> 1):(result >>1));
    //            lng+=dlng;
    //            LatLng p =new LatLng((((double) lat/1E5)),(((double) lng/1E5)));
    //            poly.add(p);
    //
    //        }
    //        return poly;
    //    }

    fun searchLocation(root: View?) {
        val location = binding.fragmentAdvertisementSearchEt2.text.toString()
        if (location != null || location != ""||binding.fragmentAdvertisementSearchEt2.text.toString().trim()?.length!!>0) {
          getLocationByText(location)
        }else{
            showToast(requireActivity(), "please enter search text first")
        }
    }

    private fun getLocationByText(location: String) {
        var addressList: List<Address>? = null
        val geocoder = Geocoder(requireContext())
        try {
            addressList = geocoder.getFromLocationName(location, 10)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val address: Address = addressList!![0]
            val latLng = LatLng(address.getLatitude(), address.getLongitude())
            zoomToLocation(address.getLatitude(), address.getLongitude())

//            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
//            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }catch (e:Exception){
            showToast(requireActivity(), "no results please enter other words")
        }
    }

    val locationPermission: Unit
        get() {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (ContextCompat.checkSelfPermission(
                    requireContext().getApplicationContext(),
                    FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        requireContext().getApplicationContext(),
                        COURSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionsGranted = true
                    initMap()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }


    private fun initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun onClick(root: View) {
        var clicked=false

        binding.fragmentMapSaveCenterLocationBtn.setOnClickListener {
//            try {
                if (first) {
                    first=false
                if (lang == 0.0 && lat == 0.0) {
                    val latLng = mMap!!.cameraPosition.target
                    if (latLng != null && latLng.longitude != 0.0 && latLng.longitude != 0.0) {
                        lang = latLng.longitude
                        lat = latLng.longitude
                    }
                }
                    if ("edit_location".equals(from)) {
                        generalDataSendedModel?.addressSelectedItem?.itemId = addressItemData?.itemId!!
                    }
                    Log.i("centerLat", ""+lat +"   "+lang)

                    generalDataSendedModel?.addressSelectedItem?.lat = lat
                        generalDataSendedModel?.addressSelectedItem!!.lang = lang
                    Log.i("centerLat", ""+generalDataSendedModel?.addressSelectedItem?.lat +"   "+generalDataSendedModel?.addressSelectedItem?.lang)

//                    }
                    generalDataSendedModel = getAddressWithDetails(generalDataSendedModel!!)

                    if(binding.fragmentMapTitleNameEtx.text?.trim()?.length!!>1) {
//                        showToast(getActivity(), "success2 "+generalDataSendedModel?.addressSelectedItem!!.lang+generalDataSendedModel?.addressSelectedItem!!.lat
//                        )

//                        Executors.newSingleThreadExecutor()
//                            .execute {

//                            addressForRoomItem.selected=true
//                                addressItemData?.lat = lat
//                                addressItemData?.lang = lang
//                                addressItemData?.address = generalDataSendedModel?.addressSelectedItem?.address
//                                addressItemData?.street = homeCycleActivity?.addressSelectedItem?.street
//                                addressItemData?.area = homeCycleActivity?.addressSelectedItem?.area
//                                addressItemData?.country = homeCycleActivity?.addressSelectedItem?.country
//                                addressItemData?.state = homeCycleActivity?.addressSelectedItem?.state
                                addressItemData=generalDataSendedModel?.addressSelectedItem
                                addressItemData?.title =
                                    binding.fragmentMapTitleNameEtx.text?.toString()
//                                homeCycleActivity?.addressSelectedItem=addressItemData!!
                                if ("edit_location".equals(from)) {
                                    dataBase!!.addNewOrderItemDao()!!.update(addressItemData!!)
                                    Log.d(TAG, "getAddress:  addresssuccess2" +addressItemData!!.lang+addressItemData!!.address)
                                } else {
                                    homeCycleActivity?.addressSelectedItem?.selected = false

                                    SharedPreferencesManger.SaveData(
                                        requireActivity()!!,
                                        SharedPreferencesManger.SELECTED,
                                        "no"
                                    )

                                    for (i in 0 until homeCycleActivity?.addressList!!.size) {
                                        homeCycleActivity?.addressList!![i].selected = false
                                        var addressItem = homeCycleActivity?.addressList!![i]
                                        dataBase!!.addNewOrderItemDao()!!.update(addressItem)
                                    }

                                    addressItemData?.selected = true
                                    Log.d(TAG, "getAddress:  addresssuccess2" +homeCycleActivity?.addressSelectedItem?.selected+"  "+ addressItemData?.selected)

                                    dataBase!!.addNewOrderItemDao()!!.insert(addressItemData!!)
                                }
//                                runBlocking(Dispatchers.Main) {
//                                    showToast(requireActivity(), generalBundelDataToSend?.from+"")
                                    if (!"edit_location".equals(from)&&"haveList".equals(generalBundelDataToSend?.from)) {
                                        navController?.popBackStack()
                                    }
                                        onBack()
                                }else {
                        first=true
                        Validation.validationLength(
                            binding.fragmentMapTitleNameTil,
                            getString(R.string.invalid_title),
                            2
                        )
                    }
                                first=true
//                            }
                    } else {
                    first=true
                    Validation.validationLength(
                        binding.fragmentMapTitleNameTil,
                        getString(R.string.invalid_title),
                        2
                    )
                    showToast(requireActivity(), "please enter title text first")
                    }
//                }
//            }catch (e:Exception){
//                first=true
//            }
            }
        binding.addCurrentLocation.setOnClickListener {
            DEFAULT_ZOOM= mMap?.getCameraPosition()?.zoom!!
            goToCurrentLocation(false)
        //                getDeviceLocation();
//               FetchURL(requireContext()).execute(
//                   getUrl(
//                       place1!!.position,
//                       place2!!.position,
//                       "driving"
//                   ), "driving"
//               )
//               mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 16F))

//                run2()
//                zoomToLocation(currentLocation?.getLatitude()!!, currentLocation?.getLongitude()!!)

        }
        binding.backBtn.setOnClickListener {
            super.onBack()
        }
        binding.changeMapType.setOnClickListener {
            if(clicked) {
                binding.changeMapTypeImage.setImageResource(R.drawable.earth1)
                mMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
                clicked=false
            }else {
                binding.changeMapTypeImage.setImageResource(R.drawable.earth_app_color)
                mMap?.setMapType(GoogleMap.MAP_TYPE_HYBRID)
                clicked=true
            }
        }
    }

    private fun goToCurrentLocation(afterOpenGps: Boolean) {
        if (currentLocation==null|| (currentLocation?.getLatitude()!! == 0.0&&currentLocation?.getLongitude()!! == 0.0)){
            if(locationManager==null){
                locationManager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                gpsSettingIsOpened=false
//                run2()
                firstUsed=true
                openGpsDialog()
            }else {
                goToMyLocation()
//                if (afterOpenGps) {
//                    object : CountDownTimer(5000, 5000) {
//                        override fun onTick(millisUntilFinished: Long) {
//                        }
//
//                        override fun onFinish() {
//                        }
//                    }.start()
//                }else{
//                    goToMyLocation()
//
//                }
            }

        }else{
            gpsSettingIsOpened=false
            zoomToLocation(currentLocation?.getLatitude()!!, currentLocation?.getLongitude()!!)
        }
    }

    private fun goToMyLocation() {
        locationPermission
        getDeviceLocation()
//        showToast(getActivity(), " " + "searchData.getData().size()")
    }

    private fun zoomToLocation(latitude: Double, longitude: Double) {
        try {
            mMap!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        latitude,
                        longitude
                    ), DEFAULT_ZOOM
                )
            )
        } catch (e: Exception) {
            Toast.makeText(
                getContext(),
                "" + e,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.fragmentMapLoadCurrentLocationLy?.visibility=View.GONE
    }



//    private fun getDirections(origin: LatLng, destination: LatLng): DirectionsResult {
//        val directions = DirectionsApi.newRequest(getGeoContext())
//            .origin(origin)
//            .destination(destination)
//            .mode(TravelMode.DRIVING)
//            .avoid(RouteRestriction.TOLLS)
//            .await()
//        return directions
//    }
//
//    // Get the GeoApiContext with the API key
//    private fun getGeoContext(): GeoApiContext {
//        val geoContext = GeoApiContext.Builder()
//            .apiKey("YOUR_API_KEY")
//            .build()
//        return geoContext
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap!!.addMarker(place2!!)
//
//        // Add origin and destination markers
//        mMap!!.addMarker(MarkerOptions().position(originLatLng).title("Origin"))
//        mMap!!.addMarker(MarkerOptions().position(destLatLng).title("Destination"))
//
//        // Add a polyline to show the route
//        val polylineOptions = PolylineOptions()
//            .add(originLatLng)
//            .add(destLatLng)
//            .color(Color.RED)
//            .width(5f)
//        mMap!!.addPolyline(polylineOptions)
//
//        // Move the camera to show the entire route
//        val bounds = LatLngBounds.builder()
//            .include(originLatLng)
//            .include(destLatLng)
//            .build()
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        //        origion=new LatLng(30.7333,76.7794);
//        dest=new LatLng(28.7041,77.1025);
//        getDirection("30.7333"+","+"76.7794","28.7041"+","+"77.1025");

        // Add origin and destination markers
        mMap!!.addMarker(MarkerOptions().position(originLatLng).title("Origin"))
        mMap!!.addMarker(MarkerOptions().position(destLatLng).title("Destination"))

        // Get the directions from the origin to the destination
//        val directions = getDirections(originLatLng, destLatLng)
//
//        // Decode the polyline points and add them to the map
//        val polylineOptions = PolylineOptions()
//            .color(Color.RED)
//            .width(5f)
//        val points = PolyUtil.decode(directions.polyline?.points)
//        for (point in points) {
//            polylineOptions.add(point)
//        }
//        mMap!!.addPolyline(polylineOptions)

        // Move the camera to show the entire route
        val bounds = LatLngBounds.builder()
            .include(originLatLng)
            .include(destLatLng)
            .build()
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        if (mLocationPermissionsGranted) {
            getDeviceLocation()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap!!.isMyLocationEnabled = false
            googleMap.setOnCameraChangeListener { cameraPosition ->
                try {
                    Executors.newSingleThreadExecutor()
                        .execute {
                            runBlocking (Dispatchers.Main) {
                                binding.fragmentMapAddressTv.setText(
                                    getString(R.string.map_location_is_loading)
                                )
                                binding.fragmentMapAddressProgressLoading.visibility =
                                    View.VISIBLE
                            }
                            getAddress = false
                            try {
                                    if (((cameraPosition.target.latitude.toString()
                                            .contains(currentLat!!.toString().substring(0,7)) &&
                                                cameraPosition.target.longitude.toString()
                                                    .contains(currentLong!!.toString().substring(0,7)))
                                        )
                                    ) {
                                        runBlocking (Dispatchers.Main) {
                                            binding.addCurrentLocationImage.setImageResource(R.drawable.ic_round_my_location_24px_map)
                                        }
                                    } else {
                                        runBlocking (Dispatchers.Main) {
                                            if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                                binding.addCurrentLocationImage.setImageResource(R.drawable.ic_baseline_location_searching_24)
                                            }
                                        }
                                    }


                                Log.i(
                                    "centerLat",
                                    cameraPosition.target.latitude.toString() + "      " + currentLat!!.toString().substring(0,7)
                                )
                                Log.i(
                                    "centerLong",
                                    cameraPosition.target.longitude.toString() + "       " + currentLong!!.toString().substring(0,7)
                                )
//                                }
                            }catch (e:Exception){
                                Log.i(
                                    "centerLong",
                                    e.toString()
                                )
                            }
                            langlat2 = cameraPosition.target
                            lang = langlat2!!.longitude
                            lat = langlat2!!.latitude
//                    distance(lat, lang, lat2, lang2)
                            generalDataSendedModel?.addressSelectedItem?.lat = lat
                            generalDataSendedModel?.addressSelectedItem!!.lang = lang

                        }
//                    getAddressWithDetails(generalDataSendedModel!!)


                }catch (e: Exception){ }
            }
        }
        mMap!!.setOnCameraIdleListener {

            getAddress = true
            Executors.newSingleThreadExecutor().execute {
                try {
//                                    Log.d(
//                                        TAG,
//                                        "onCameraIdle: map changes stop" + getAddressWithDetails2(
//                                            generalDataSendedModel!!
//                                        )
//                                    )
                    setAddressValues()
//                                    address = getAddressWithDetails2(
//                                        generalDataSendedModel!!
//                                    )!!.trim()

                } catch (e: Exception) {
                    try {
                        if (address == null) {
                            generalDataSendedModel!!.addressSelectedItem!!.lat = currentLat
                            generalDataSendedModel!!.addressSelectedItem!!.lang = currentLong
                            setAddressValues()
                        } else {
                            if (homeCycleActivity!!.addressSelectedItem != null) {
                                generalDataSendedModel!!.addressSelectedItem!!.lat =
                                    homeCycleActivity!!.addressSelectedItem.lat
                                generalDataSendedModel!!.addressSelectedItem!!.lang =
                                    homeCycleActivity!!.addressSelectedItem.lang
                                setAddressValues()
                            }
//                                       else {
//                                           generalDataSendedModel!!.addressSelectedItem!!.lat=currentLat
//                                           generalDataSendedModel!!.addressSelectedItem!!.lang=currentLong
//                                           setAddressValues()
//                                       }
                        }
                    } catch (e: java.lang.Exception) {
                    }
                }

//                !!.substring(50)+"..."

            }
        }
        mMap!!.setOnMapClickListener { latLng ->
//            if (markerPoints.size > 1) {
//                markerPoints.clear()
//                mMap!!.clear()
//            }
//
//            // Adding new item to the ArrayList
//            markerPoints.add(latLng!!)
//
//            // Creating MarkerOptions
//            val options = MarkerOptions()
//
//            // Setting the position of the marker
//            options.position(latLng)
//            if (markerPoints.size == 1) {
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            } else if (markerPoints.size == 2) {
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//            }
//
//            // Add new marker to the Google Map Android API V2
//            mMap!!.addMarker(options)
//
//
//
//            // Checks, whether start and end locations are captured
//            if (markerPoints.size >= 2) {
//                val origin = markerPoints[0] as LatLng
//                val dest = markerPoints[1] as LatLng
//
//                // Getting URL to the Google Directions API
//                val downloadTask: DownloadTask =
//                    DownloadTask()
//
//                // Start downloading json data from Google Directions API
//                downloadTask.execute()
//            }
        }


    }

    private fun setAddressValues() {
        address = getAddressWithDetails(
            generalDataSendedModel!!
        )!!.addressSelectedItem!!.address!!.trim()
        runBlocking(Dispatchers.Main) {
            binding.fragmentMapAddressTv.setText(
                address
            )
            binding.fragmentMapAddressProgressLoading.visibility =
                View.GONE
        }
    }

    private fun getDeviceLocation() {
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        try {
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                run2()
            } else {

                if (mLocationPermissionsGranted) {
                    mFusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentLocation = task.result as Location?
                        if ("edit_location".equals(from)) {
//                            showToast(requireActivity(), " yes "+addressItemData?.lang)

                            val mapLocation =
                                LatLng(addressItemData?.lat!!, addressItemData?.lang!!)
                            zoomToLocation(addressItemData?.lat!!, addressItemData?.lang!!)

//                            moveCamera(mapLocation, DEFAULT_ZOOM)
//                            mMap?.addMarker(
//                                MarkerOptions()
//                                    .position(mapLocation)
//                                    .title("User Current Location")
//                            )
                        } else {
                            if (currentLocation != null) {

//                                showToast(MapsActivity.this, " yes ");
//                                moveCamera(
//                                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
//                                    DEFAULT_ZOOM
//                                )

                                gpsSettingIsOpened=false
                                gpsSettingIsOpened2=false
                                zoomToLocation(currentLocation?.getLatitude()!!, currentLocation?.getLongitude()!!)
                            } else {
                                if(homeCycleActivity?.addressSelectedItem?.lat !=0.0&&homeCycleActivity?.addressSelectedItem?.lang !=0.0) {
                                    zoomToLocation(
                                        homeCycleActivity?.addressSelectedItem?.lat!!,
                                        homeCycleActivity?.addressSelectedItem?.lang!!
                                    )
                                }
                                gpsSettingIsOpened2=true
//                                Toast.makeText(
//                                    activity,
//                                    "unable to get current location",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        }
                    }
                }
            } else {

                }
                }
            } catch (e: SecurityException) {
        }
    }

    private fun moveCamera(latLng: LatLng, defaultZoom: Float) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom))
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

//    override fun onTaskDone(vararg values: Any) {
//
//    }

    private inner class DownloadTask :
        AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            val data = ""
            try {
//            Toast.makeText(requireContext(), "sssssssssssssssssss", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){}

            return data
        }
    }

    companion object {
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        const val LOCATION_PERMISSION_REQUEST_CODE = 1234
        private var DEFAULT_ZOOM = 15f
    }

    override fun onTaskDone(vararg values: Any?) {
        TODO("Not yet implemented")
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        if ("updateMap".equals(generalDataSendedModel.type2)){
            DEFAULT_ZOOM=15f
            HelperMethod.dismissProgressDialog()
            if ("edit_location".equals(from)&&updateFirstOpen!!) {
                zoomToLocation(addressItemData?.lat!!, addressItemData?.lang!!)
                updateFirstOpen=false
            }else {
                zoomToLocation(
                    generalDataSendedModel?.latituate!!,
                    generalDataSendedModel?.longituate!!
                )
            }
            currentLat=generalDataSendedModel?.latituate!!
            currentLong=generalDataSendedModel?.longituate!!
            binding.addCurrentLocationImage.setImageResource(R.drawable.ic_round_my_location_24px_map)
        }
        else {
            binding.addCurrentLocationImage.setImageResource(R.drawable.location_error)
            if ("edit_location".equals(from)&&updateFirstOpen!!) {
                zoomToLocation(addressItemData?.lat!!, addressItemData?.lang!!)
                updateFirstOpen=false
            }else {
                DEFAULT_ZOOM = 6f
                getLocationByText("السعودية")
            }
        }
    }


    override fun onLocationChanged(loc: Location) {

//        Toast.makeText(
//            requireContext(),
//            "Location changed: Lat: " + loc.latitude + " Lng: "
//                    + loc.longitude, Toast.LENGTH_SHORT
//        ).show()
//        val longitude = "Longitude: " + loc.longitude
//        Log.v(TAG, longitude)
//        val latitude = "Latitude: " + loc.latitude
//        Log.v(TAG, latitude)

    }

//    override fun onTaskDone(vararg values: Any?) {
//        TODO("Not yet implemented")
//        if (currentPolyline != null) currentPolyline!!.remove()
//        currentPolyline = mMap!!.addPolyline(values[0] as PolylineOptions)
//    }
}