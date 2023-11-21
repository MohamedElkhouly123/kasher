package com.example.ecommerceapp.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.utils.HelperMethod.getAppLanguage
import com.example.ecommerceapp.utils.HelperMethod.showToast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Create this Class from tutorial :
 * http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial
 *
 *
 * For Geocoder read this : http://stackoverflow.com/questions/472313/android-reverse-geocoding-getfromlocation
 */
class GPSTracker( generalDataSendedModel: GeneralDataSendedModel?) :
    Activity(), LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
//    private val userData: UserDataMain
private val mContext: Context?=generalDataSendedModel?.context
var activity: Activity?=generalDataSendedModel?.activity
    var generalDataSendedModel=generalDataSendedModel
    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    // flag for GPS Status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS Tracking is enabled
    var isGPSTrackingEnabled = false
    var isGPSPermission = true
    var location2: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    // How many Geocoder should return our GPSTracker
    var geocoderMaxResults = 1
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private var provider_info: String? = null
//    private var mSocket: Socket? = null
//    private var loctionSocketSendItem: LoctionSocketSendItem? = null
    private var mLocationPermissionsGranted = false
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null

    init {
//        userData = LoadUserData(activity)
    }

    fun startTrace() {
        locationPermission
        askLocationPermission()
        getLocation()
        //        checkSettingsAndStartLocationUpdates();
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.i(
                        "MainActivity",
                        "Location: " + location.latitude + " " + location.longitude
                    )
                    emitChanget(location)
                }
            }
        }
    }

    fun startRequestLocation() {
        locationPermission
        askLocationPermission()
        getLocation()
        //        checkSettingsAndStartLocationUpdates();
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.i(
                        "MainActivity",
                        "Location: " + location.latitude + " " + location.longitude
                    )
                    emitChanget(location)
                }
            }
        }
    }


    /**
     * Try to get my current location by GPS or Network Provider
     */
    fun getLocation() {
        try {
            locationManager = mContext?.getSystemService(LOCATION_SERVICE) as LocationManager?

            //getting GPS status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //getting network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                isGPSTrackingEnabled = true

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */provider_info = LocationManager.GPS_PROVIDER
            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                isGPSTrackingEnabled = true

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */provider_info = LocationManager.NETWORK_PROVIDER
            } else {
                provider_info = null
            }

            // Application can use GPS or Network Provider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                    ), 10
                )
                isGPSPermission = false
            } else if (provider_info != null) {
                locationManager!!.requestLocationUpdates(
                    provider_info!!,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
                if (locationManager != null) {
//                    if (location2==null||(location2?.latitude==0.0&&location2?.longitude==0.0)) {
//                    }
                    if (ActivityCompat.checkSelfPermission(
                            activity!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        var client2 = LocationServices.getFusedLocationProviderClient(activity!!)
                        client2.getLastLocation()
                            .addOnSuccessListener(activity!!, {
                                try {
                                    location2 = it
                                    generalDataSendedModel?.from="GpsTracer"
                                    generalDataSendedModel?.gpsOpened=true
                                    generalDataSendedModel?.addressSelectedItem?.lang=location2!!.longitude
                                    generalDataSendedModel?.addressSelectedItem?.lat=location2!!.latitude
                                    Log.e(TAG, "Impossible to connect to LocationManager1   "+location2!!.longitude )
                                    generalDataSendedModel?.makeChangeInFragment?.doChanges(generalDataSendedModel!!)
                                    Log.e(TAG, "Impossible to connect to LocationManager11   "+ generalDataSendedModel?.addressSelectedItem?.lang)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Impossible to connect to LocationManager2   "+e )
                                }
                            })
                    }else{
                        location2 = locationManager!!.getLastKnownLocation(provider_info!!)
                        Log.e(TAG, "Impossible to connect to LocationManager3   "+location2!!.longitude )

                    }
                    updateGPSCoordinates()
                }
            }
        } catch (e: NullPointerException) {
            //e.printStackTrace();
            Log.e(TAG, "Impossible to connect to LocationManager", e)
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                run {
                    if (grantResults.size > 0) {
                        var i = 0
                        while (i < grantResults.size) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                mLocationPermissionsGranted = false
                                return
                            }
                            i++
                        }
                        mLocationPermissionsGranted = true
                        //initialize our map
                    }
                }
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
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
                    return
                }
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) locationManager!!.requestLocationUpdates(
                    provider_info!!,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
            }
            10 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) locationManager!!.requestLocationUpdates(
                provider_info!!,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                this
            )
        }
    }

    /**
     * Update GPSTracker latitude and longitude
     */
    fun updateGPSCoordinates() {
        if (location2 != null) {
            latitude = location2!!.latitude
            longitude = location2!!.longitude
        }
    }

    /**
     * GPSTracker latitude getter and setter
     *
     * @return latitude
     */
    fun getLatitude(): Double {
        if (location2 != null) {
            latitude = location2!!.latitude
        }
        return latitude
    }

    /**
     * GPSTracker longitude getter and setter
     *
     * @return
     */
    fun getLongitude(): Double {
        if (location2 != null) {
            longitude = location2!!.longitude
        }
        return longitude
    }

    /**
     * GPSTracker isGPSTrackingEnabled getter.
     * Check GPS/wifi is enabled
     */
    fun getIsGPSTrackingEnabled(): Boolean {
        return if (!isGPSPermission) {
            false.also { isGPSTrackingEnabled = it }
        } else {
            isGPSTrackingEnabled
        }
    }

    /**
     * Stop using GPS listener
     * Calling this method will stop using GPS in your app
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GPSTracker)
        }
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
        }
    }

    /**
     * Function to show settings alert dialog
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(
            mContext
        )

        //Setting Dialog Title
        alertDialog.setTitle(R.string.GPSAlertDialogTitle)

        //Setting Dialog Message
        alertDialog.setMessage(R.string.GPSAlertDialogMessage)


        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.action_settings,
            DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                mContext?.startActivity(intent)
            })

        //On pressing cancel button
        alertDialog.setNegativeButton(R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }

    /**
     * Get list of address by latitude and longitude
     *
     * @return null or List<Address>
    </Address> */
    fun getGeocoderAddress(context: Context?): List<Address>? {
        if (location2 != null) {
            val geocoder = Geocoder(context!!, Locale.ENGLISH)
            try {
                return geocoder.getFromLocation(latitude, longitude, geocoderMaxResults)
            } catch (e: IOException) {
                //e.printStackTrace();
            }
        }
        return null
    }

    /**
     * Try to get AddressLine
     *
     * @return null or addressLine
     */
    fun getAddressLine(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.size > 0) {
            val address = addresses[0]
            address.getAddressLine(0)
        } else {
            null
        }
    }

    /**
     * Try to get Locality
     *
     * @return null or locality
     */
    fun getLocality(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.size > 0) {
            val address = addresses[0]
            address.locality
        } else {
            null
        }
    }

    /**
     * Try to get Postal Code
     *
     * @return null or postalCode
     */
    fun getPostalCode(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.size > 0) {
            val address = addresses[0]
            address.postalCode
        } else {
            null
        }
    }

    /**
     * Try to get CountryName
     *
     * @return null or postalCode
     */
    fun getCountryName(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.size > 0) {
            val address = addresses[0]
            address.countryName
        } else {
            null
        }
    }

    override fun onLocationChanged(location: Location) {
        emitChanget(location)
    }

    private fun emitChanget(location: Location) {
//        if ("worker".equals(type, ignoreCase = true) || "worker_place".equals(
//                type,
//                ignoreCase = true
//            ) || "place".equals(type, ignoreCase = true)
//        ) {
//            longitude = location.longitude
//            latitude = location.latitude
//            try {
//                mSocket = IO.socket(BASE_URL4)
//                loctionSocketSendItem = LoctionSocketSendItem(
//                    java.lang.String.valueOf(userData.getId()),
//                    latitude.toString(),
//                    longitude.toString()
//                )
//                val gson = Gson()
//                try {
//                    val obj = JSONObject(
//                        gson.toJson(
//                            loctionSocketSendItem
//                        )
//                    )
//                    mSocket.emit("currentLocation", obj)
//                    //                                showToast(activity, "lang: " + longitude + "lat: " + latitude);
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//                //                mSocket.on("message", onNewMessage);
//                mSocket.connect()
//                //
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }



    override fun onLocationChanged(locations: List<Location>) {}
    override fun onFlushComplete(requestCode: Int) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    val locationPermission: Unit
        get() {

            if (ContextCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        activity?.applicationContext!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionsGranted = true
                } else {
                    ActivityCompat.requestPermissions(
                        activity!!,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

    fun checkSettingsAndStartLocationUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!).build()
        val client = LocationServices.getSettingsClient(activity!!)
        val locationSettingsResponseTask = client.checkLocationSettings(request)
        locationSettingsResponseTask.addOnSuccessListener { //Settings of device are satisfied and we can start location updates
            startLocationUpdates()
        }
        locationSettingsResponseTask.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(activity!!, 1001)
                } catch (ex: SendIntentException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun startLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest!!.interval = 120000 // two minute interval
        locationRequest!!.fastestInterval = 120000
        locationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
            return
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {}
    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    companion object {

        private var opened2: Boolean=false

        // The minimum distance to change updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 // 1 minute
                ).toLong()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1252

        // Get Class Name
        private val TAG = GPSTracker::class.java.name
        private var address: String? = null
        fun getAddressWithDetails2(
            generalDataSendedModel: GeneralDataSendedModel
        ): String? {
//Set Address
            try {
//                Log.i("centerLat222", ""+generalDataSendedModel?.addressSelectedItem?.lat +"   "+generalDataSendedModel?.addressSelectedItem?.lang)

                val geocoder = Geocoder(generalDataSendedModel?.activity!!, Locale.getDefault())
                val addresses = geocoder.getFromLocation(generalDataSendedModel?.addressSelectedItem?.lat!!, generalDataSendedModel?.addressSelectedItem?.lang!!, 5)
                if (addresses != null && addresses.size > 0) {
//                    Log.i("centerLat2", ""+generalDataSendedModel?.addressSelectedItem?.lat +"   "+generalDataSendedModel?.addressSelectedItem?.lang)
                    var appLanguage = getAppLanguage()
                    address =
                        addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                }
            }catch (e:Exception){}
            return address
        }
                    //    Just Use this method and pass your lat, long.
        fun getAddressWithDetails(
            generalDataSendedModel: GeneralDataSendedModel
        ): GeneralDataSendedModel {
//Set Address
//                Log.i("centerLat222", ""+generalDataSendedModel?.addressSelectedItem?.lat +"   "+generalDataSendedModel?.addressSelectedItem?.lang)

                            try {
//                Log.i("centerLat222", ""+generalDataSendedModel?.addressSelectedItem?.lat +"   "+generalDataSendedModel?.addressSelectedItem?.lang)

                                val geocoder = Geocoder(
                                    generalDataSendedModel?.activity!!,
                                    Locale.getDefault()
                                )
                                val addresses = geocoder.getFromLocation(
                                    generalDataSendedModel?.addressSelectedItem?.lat!!,
                                    generalDataSendedModel?.addressSelectedItem?.lang!!,
                                    5
                                )
                                if (addresses != null && addresses.size > 0) {
//                    Log.i("centerLat2", ""+generalDataSendedModel?.addressSelectedItem?.lat +"   "+generalDataSendedModel?.addressSelectedItem?.lang)
                                    var appLanguage = getAppLanguage()
                                    address =
                                        addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    val locality = addresses[0].locality
                                    val state = addresses[0].adminArea
                                    val country = addresses[0].countryName
                                    val thoroughfare = addresses[0].subThoroughfare
                                    val featureName =
                                        addresses[0].featureName // Only if available else return NULL
                                    var arrOfStr: Array<String>? = null
                                    var street: String? = null
                                    var area: String? = null
                                    var matcher = featureName.uppercase()
                                        .contains("[A-Z]".toRegex()) && featureName.uppercase()
                                        .contains("[0-9]".toRegex())
                                    if (address!!.contains(country)) {
                                        address = address!!.replace(country, "")
//                        Log.d(TAG, "getAddress:  area"+area+" coun  "+country)
                                    }
                                    if (address!!.contains(state)) {
                                        address = address!!.replace(state, "")
//                        Log.d(TAG, "getAddress:  area"+state+" coun  "+address)
                                    }
                                    if (address!!.contains(featureName) && ("ar".equals(appLanguage) || matcher) && !isProbablyArabic(
                                            featureName
                                        )
                                    ) {
                                        address = address!!.replace(featureName, "")
                                        Log.d(
                                            TAG,
                                            "getAddress:  area" + featureName + " coun22  " + address
                                        )
                                    }
                                    if (address!!.contains(featureName) && matcher) {
                                        address = address!!.replace(featureName, "")
                                    }
                                    if ("ar".equals(appLanguage)) {
                                        arrOfStr = address!!.trim().split("،").toTypedArray()

                                    } else {
                                        arrOfStr = address!!.trim().split(",").toTypedArray()
                                    }
                                    var arrLength = arrOfStr.size
                                    street = arrOfStr!![0].trim()
                                    area = arrOfStr[1].trim()
//                    if (isArabicAndEnglish(address!!)) {
//                        if (!isArabicAndEnglish(street)) {
//                            if (!isProbablyArabic(street) || street.trim().length == 0) {
//                                street = arrOfStr!![1].trim()
//                                area = arrOfStr[2].trim()
//                            }
//                        } else {
//                            if (!isArabicAndEnglish(area)) {
//                                if ((!isProbablyArabic(area) &&"ar".equals(appLanguage))|| area.trim().length == 0) {
//                                    area = arrOfStr[2].trim()
//                                }
//                            }
//                        }
//                        if(isArabicAndEnglish(street)&&street.contains(",")){
//                            arrOfStr = street!!.trim().split(",").toTypedArray()
//                            if (arrOfStr.size==2) {
//                                street = arrOfStr!![0].replace(",", "").trim()
//                            }else{
//                                street = street.replace(",", "")
//                            }
//                        }
//                        if(isArabicAndEnglish(street)&&street.contains("،")){
//                            arrOfStr = street!!.trim().split("،").toTypedArray()
//                            if (arrOfStr.size==2) {
//                                street = arrOfStr!![0].replace("،", "").trim()
//                            }else{
//                                street = street.replace("،", "")
//                            }
//                        }
//                        if(isArabicAndEnglish(area)&&area.contains(",")){
//                            arrOfStr = street!!.trim().split(",").toTypedArray()
//                            if (arrOfStr.size==2) {
//                                area = arrOfStr!![0].replace(",", "").trim()
//                            }else{
//                                area =area.replace(",", "")
//                            }
//                        }
//                        if(isArabicAndEnglish(area)&&area.contains("،")){
//                            arrOfStr = street!!.trim().split("،").toTypedArray()
//                            if (arrOfStr.size==2) {
//                                area = arrOfStr!![0].replace("،", "").trim()
//                            }else{
//                                area =area.replace("،", "")
//                            }
//                        }
//                    }else{
//                        if(arrOfStr.size>4&&!isProbablyArabic(address!!)) {
//                        area = arrOfStr[1].trim()+" ، "+arrOfStr[2].trim()
//                        }
//                        }

                                    if (arrLength > 4) {
                                        area = arrOfStr[1].trim() + " ، " + arrOfStr[2].trim()
                                    }
                                    if (street.contains(",") || street.contains("،")) {
                                        if (street.contains(",")) {
                                            arrOfStr = street!!.trim().split(",").toTypedArray()
                                        }
                                        if (street.contains("،")) {
                                            arrOfStr = street!!.trim().split("،").toTypedArray()
                                        }
                                        Log.d(TAG, "getAddress:  area" + street)
                                        street = ""
                                        for (i in 0 until arrOfStr.size) {
                                            var matcher = arrOfStr[i].uppercase()
                                                .contains("[A-Z]".toRegex()) && arrOfStr[i].uppercase()
                                                .contains("[0-9]".toRegex())
                                            if (!matcher) {
                                                if (!street!!.contains(arrOfStr[i])) {
                                                    if (street?.trim()!!.length > 0) {
                                                        street = street + " " + arrOfStr[i]
                                                    } else {
                                                        street = arrOfStr[i]
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (area.contains(",") || area.contains("،")) {
                                        if (area.contains(",")) {
                                            arrOfStr = area!!.trim().split(",").toTypedArray()
                                        }
                                        if (area.contains("،")) {
                                            arrOfStr = area!!.trim().split("،").toTypedArray()
                                        }
                                        area = ""
                                        for (i in 0 until arrOfStr.size) {
                                            var matcher = arrOfStr[i].uppercase()
                                                .contains("[A-Z]".toRegex()) && arrOfStr[i].uppercase()
                                                .contains("[0-9]".toRegex())
                                            if (!matcher) {
                                                if (!area!!.contains(arrOfStr[i])) {
                                                    if (area?.trim()!!.length > 0) {
                                                        area = area + " " + arrOfStr[i]
                                                    } else {
                                                        area = arrOfStr[i]
                                                    }
                                                }
                                            }
                                        }
                                    }

//                    if((featureName.chars().anyMatch(Character::isUpperCase)&&(featureName.equals(street)||featureName.equals(area)))||street.trim().length==0||area.trim().length==0){
//                        address = addresses[1].getAddressLine(0)
//                        address!!.replace(",","")
//                        val arrOfStr: Array<String> = address!!.trim().split("،").toTypedArray()
//                        street = arrOfStr[0].trim()
//                        if(arrOfStr.size>4) {
//                            area = arrOfStr[1].trim()+" ، "+arrOfStr[2].trim()
//                            if(arrOfStr[1].trim().length==0){
//                                area = arrOfStr[1].trim()
//                            }
//                            if(arrOfStr[2].trim().length==0){
//                                area = arrOfStr[2].trim()
//                            }
//                        }else{
//                            area = arrOfStr[1].trim()
//                        }
//                        }
//                    isProbablyArabic(street!!)
//                    isProbablyEnglish(address!!)
//                    for (i in 0 until arrOfStr.size) {
//                        Log.d("=Adress=", arrOfStr[i])
//                    }
                                    generalDataSendedModel?.addressSelectedItem?.street = street
                                    generalDataSendedModel?.addressSelectedItem?.state = state
                                    generalDataSendedModel?.addressSelectedItem?.area = area
                                    generalDataSendedModel?.addressSelectedItem?.country = country
                                    var address2: String? = ""
                                    if (street!!.trim().length > 0) {
                                        address2 = street + " ، "
                                    }
                                    Log.d(TAG, "getAddress:  area" + address2 + " coun  " + street)
                                    if (area!!.trim().length > 0 && address2?.trim()!!.length > 0) {
                                        address2 = address2 + area + " ، "
                                    }
                                    if (area!!.trim().length > 0 && address2?.trim()!!.length == 0) {
                                        address2 = area + " ، "
                                    }
                                    Log.d(TAG, "getAddress:  area" + address2 + " coun  " + street)
                                    address2 = address2 + state + " ، " + country
                                    generalDataSendedModel?.addressSelectedItem?.address = address2
//                    Log.d(TAG, "getAddress:  address " + generalDataSendedModel?.addressSelectedItem?.address)
//                    Log.d(TAG, "getAddress:  city$locality")
//                    Log.d(TAG, "getAddress:  state$state")
//                    Log.d(TAG, "getAddress:  country$country")
//                    Log.d(TAG, "getAddress:  featureName"+featureName)
//                    Log.d(TAG, "getAddress:  locale"+ isProbablyEnglish(address!!).toString() )
//                    Log.d(TAG, "getAddress:  locale"+addresses[2].getAddressLine(0) )
//                    Log.d(TAG, "getAddress:  locale"+addresses[3].getAddressLine(0) )
//                    for (i in 0 until addresses.size) {
//                            Log.d("=Adress=", addresses[i].getAddressLine(0))
//                    }
                                                    }
                            } catch (e: Exception) {
                                Log.d(TAG, "getAddress:  featureName" + e)
                                e.printStackTrace()
                            }
                            return generalDataSendedModel
                    }

        fun isProbablyArabic(s:String): Boolean {
            var i = 0
            while (i < s.length) {
                val c = s.codePointAt(i)
                if (c >= 0x0600 && c <= 0x06E0) {
                    Log.d(TAG, "getAddress:  locale" +"yes")
                    return true
                }
                i += Character.charCount(c)
            }
            Log.d(TAG, "getAddress:  locale" +"no")
            return false
        }
        fun isArabicAndEnglish(s:String): Boolean {
            var i = 0
            var typeValueAR: Boolean=false
            var typeValueEN: Boolean=false
            while (i < s.length) {
                val c = s.codePointAt(i)
                if (c >= 0x0600 && c <= 0x06E0) {
                    typeValueAR= true
                }else{
                    typeValueEN=true
                }
                i += Character.charCount(c)
            }
            Log.d(TAG, "getAddress:  locale" +" two ")
            return typeValueAR && typeValueEN
        }
        fun isProbablyEnglish(s:String): Boolean {
            var i = 0
            while (i < s.length) {
                val c = s.codePointAt(i)
                if (!(c >= 0x0600 && c <= 0x06E0))
                {
                    Log.d(TAG, "getAddress:  locale" +" en")
                    return true
                }
                i += Character.charCount(c)
            }
            return false
        }

        fun requestDeviceLocationSettings(generalDataSendedModel: GeneralDataSendedModel?) {
            opened2=false
            var activity = generalDataSendedModel?.activity
            var opened: Boolean=false
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            var locationManager =
                activity?.getSystemService(LOCATION_SERVICE) as LocationManager
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val client: SettingsClient = LocationServices.getSettingsClient(activity!!)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener { locationSettingsResponse ->
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                val state = locationSettingsResponse.locationSettingsStates
                val label =
                    "GPS >> (Present: ${state?.isGpsPresent}  | Usable: ${state?.isGpsUsable} ) \n\n" +
                            "Network >> ( Present: ${state?.isNetworkLocationPresent} | Usable: ${state?.isNetworkLocationUsable} ) \n\n" +
                            "Location >> ( Present: ${state?.isLocationPresent} | Usable: ${state?.isLocationUsable} )"
//                showToast(activity,"ssss")
                getLocationThenZoom(generalDataSendedModel)




            }


            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    opened2=true
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult()

                        if("map".equals(generalDataSendedModel?.type)){
                            if(!generalDataSendedModel?.homeCycleActivity?.gpsCanceled!!) {
//                                showToast(activity, " " + "searchData.getData().size()")
                                exception.startResolutionForResult(
                                    activity!!,
                                    100
                                )
                                generalDataSendedModel?.homeCycleActivity?.gpsCanceled=true
                            }else {
                                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    generalDataSendedModel?.tryAgainCall?.tryAgainCall(
                                        generalDataSendedModel
                                    )
                                }else{
                                    getLocationThenZoom(generalDataSendedModel)
                                }
                                generalDataSendedModel!!.mapWaitTextView?.visibility= View.VISIBLE
                            }

                        }else{
                            if(!opened) {
                                exception.startResolutionForResult(
                                    activity!!,
                                    100
                                )
                                opened=true
                            }
                        }

                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        showToast(activity,sendEx.message.toString())
                    }
                }
            }

        }

         fun getLocationThenZoom(generalDataSendedModel: GeneralDataSendedModel?) {
             generalDataSendedModel!!.mapWaitTextView?.visibility=View.VISIBLE
             var activity = generalDataSendedModel?.activity
             var gpsTracker = GPSTracker(generalDataSendedModel)
             generalDataSendedModel?.type2="updateMap"
             var zoomNotDone = true
            gpsTracker.startRequestLocation()
            var client2 = LocationServices.getFusedLocationProviderClient(activity!!)

            object : CountDownTimer(70000, 5000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (zoomNotDone) {
//                        progressStartFun(generalDataSendedModel!!)
                        if (ActivityCompat.checkSelfPermission(
                                activity!!,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
//                            showToast(activity,"ssss")
                            client2.getLastLocation()
                                .addOnSuccessListener(activity!!, {
                                    try {
//                                        showToast(
//                                            activity,
//                                            label + "lat" + it.latitude + "lang" + it.longitude
//                                        )
                                        generalDataSendedModel?.longituate = it.longitude
                                        generalDataSendedModel?.latituate = it.latitude

                                        zoomNotDone = false
                                        generalDataSendedModel?.tryAgainCall?.tryAgainCall(
                                            generalDataSendedModel
                                        )
                                    } catch (e: Exception) {
                                    }
                                })
                        }
                    }
                }

                override fun onFinish() {

                }
            }.start()
             object : CountDownTimer(10000, 10000) {
                 override fun onTick(millisUntilFinished: Long) {
                     if (zoomNotDone) {
//                         progressStartFun(generalDataSendedModel!!)
                     }
                 }
                 override fun onFinish() {
                 }
             }.start()
        }
    }
}