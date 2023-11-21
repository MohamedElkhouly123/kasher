package com.example.ecommerceapp.utils

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.ActivityCompat

class GpsServiece : Service() {
    private var listener: LocationListener? = null
    private var locationManager: LocationManager? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val i = Intent("location_update")
                i.putExtra("coordinates", location.longitude.toString() + " " + location.latitude)
                sendBroadcast(i)
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
            override fun onProviderEnabled(s: String) {}
            override fun onProviderDisabled(s: String) {
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            }
        }
        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
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
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0f, listener!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null) {
            locationManager!!.removeUpdates(listener!!)
        }
    }
}