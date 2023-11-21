package com.example.ecommerceapp.view.fragment.HomeCycle

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentMapBinding
import com.example.ecommerceapp.utils.interfaces.ApiInterface
import com.example.ecommerceapp.utils.directionHelper.FetchURL
import com.example.ecommerceapp.utils.directionHelper.TaskLoadedCallback
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.textfield.TextInputEditText

class MapFragmentParent : BaseFragment(), OnMapReadyCallback, TaskLoadedCallback {
    private lateinit var binding: FragmentMapBinding
    private var root: View?=null
    var navController: NavController? = null


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
    var markerPoints: ArrayList<LatLng> = ArrayList<LatLng>()
    var apiInterface: ApiInterface? = null
    private val polyLineList: List<LatLng>? = null
    private val polylineOptions: PolylineOptions? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        try {
            navController = Navigation.findNavController(root!!)
            place1 = MarkerOptions().position(LatLng(27.658143, 85.3199503)).title("Location 1")
            place2 = MarkerOptions().position(LatLng(27.667491, 85.3208583)).title("Location 2")
            initMap()
        } catch (e: Exception) {
        }
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
    val locationPermission: Unit
        get() {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
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
            .findFragmentById(R.id.mapNearBy) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    @OnClick(R.id.add_current_location, R.id.back_btn)
//    fun onClick(view: View) {
//        when (view.id) {
//            R.id.add_current_location -> //                getDeviceLocation();
//                FetchURL(requireContext()).execute(
//                    getUrl(
//                        place1!!.position,
//                        place2!!.position,
//                        "driving"
//                    ), "driving"
//                )
//            R.id.back_btn -> {}
//        }
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.addMarker(place2!!)
        //        origion=new LatLng(30.7333,76.7794);
//        dest=new LatLng(28.7041,77.1025);
//        getDirection("30.7333"+","+"76.7794","28.7041"+","+"77.1025");
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
            mMap!!.isMyLocationEnabled = true
            googleMap.setOnCameraChangeListener { cameraPosition ->
                Log.i("centerLat", cameraPosition.target.latitude.toString())
                Log.i("centerLong", cameraPosition.target.longitude.toString())
                langlat2 = cameraPosition.target
                lang = langlat2!!.longitude
                lat = langlat2!!.latitude
                distance(lat, lang, lat2, lang2)
                Toast.makeText(
                    getActivity(),
                    "" + distance(lat, lang, lat2, lang2) * 1.6,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        mMap!!.setOnMapClickListener { latLng ->
            if (markerPoints.size > 1) {
                markerPoints.clear()
                mMap!!.clear()
            }

            // Adding new item to the ArrayList
            markerPoints.add(latLng!!)

            // Creating MarkerOptions
            val options = MarkerOptions()

            // Setting the position of the marker
            options.position(latLng)
            if (markerPoints.size == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else if (markerPoints.size == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }

            // Add new marker to the Google Map Android API V2
            mMap!!.addMarker(options)

            // Checks, whether start and end locations are captured
            if (markerPoints.size >= 2) {
                val origin = markerPoints[0] as LatLng
                val dest = markerPoints[1] as LatLng

                // Getting URL to the Google Directions API
            }
        }
    }

    private fun getDeviceLocation() {
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        try {
            val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } else {
                if (mLocationPermissionsGranted) {
                        mFusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                                val currentLocation = task.result as Location?
                                if (currentLocation != null) {
//                                showToast(MapsActivity.this, " yes ");
                                    moveCamera(
                                        LatLng(currentLocation.latitude, currentLocation.longitude),
                                        DEFAULT_ZOOM
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    getActivity(),
                                    "unable to get current location",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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
            Toast.makeText(requireContext(), "sssssssssssssssssss", Toast.LENGTH_SHORT).show()
            return data
        }
    }

    companion object {
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
        private const val DEFAULT_ZOOM = 15f
    }

    override fun onTaskDone(vararg values: Any?) {
        TODO("Not yet implemented")
        if (currentPolyline != null) currentPolyline!!.remove()
        currentPolyline = mMap!!.addPolyline(values[0] as PolylineOptions)
    }
}