package com.example.ecommerceapp.view.fragment.MapPages
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentMapBinding
import com.example.ecommerceapp.view.fragment.BaseFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

class MapFragmentTest : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private var root: View?=null
    private lateinit var mMap: GoogleMap
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        root=binding.root
        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        binding.fragmentAdvertisementSearchEt2.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
        try {
            if (!Places.isInitialized()) {
                Places.initialize(
                    requireContext()!!.applicationContext,
                    getString(R.string.google_maps_eccommerce)
                )
            }

            // Initialize the AutocompleteSupportFragment.
            val autocompleteFragment =
                childFragmentManager.findFragmentById(R.id.fragment_advertisement_search_Et) as AutocompleteSupportFragment?
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
        return root
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Initialize the AutocompleteSupportFragment.
//        val autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
//                    as AutocompleteSupportFragment
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: ${place.name}, ${place.id}")
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: $status")
//            }
//        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}