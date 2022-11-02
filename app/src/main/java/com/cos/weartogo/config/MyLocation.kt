package com.cos.weartogo.config

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.cos.weartogo.data.LatLng
import kotlin.math.ln

open class MyLocation(private val mContext: Context) {

    @SuppressLint("MissingPermission")
    open fun getMyLocation(): LatLng? {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        var lat = myLocation?.latitude
        var lng = myLocation?.longitude
        if (lat != null && lng != null) {
            return LatLng(lat, lng)
        } else {
            myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            lat = myLocation?.latitude
            lng = myLocation?.longitude
            if (lat != null && lng != null) {
                return LatLng(lat, lng)
            }
        }
        return null
    }
}