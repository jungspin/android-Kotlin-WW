package com.pinslog.ww.config

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


private const val TAG = "CustomLocation"
class CustomLocation(private val mContext: Context): AppCompatActivity() {

    private var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2


    var currentLocation: Location? = null

    /**
     * 현재 좌표를 구하는 메소드
     */
    fun getMyLocation(): Location? {
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 동의하지 않은 경우
            ActivityCompat.requestPermissions(
                mContext as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            //getMyLocation()
        } else {
            // 동의한 경우
            // 수동으로 위치 구하기
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

            currentLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (currentLocation != null) {
                val lng = currentLocation!!.longitude
                val lat = currentLocation!!.latitude
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 10F, gpsLocationListener)
            } else {
                currentLocation = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val lng = currentLocation!!.longitude
                val lat = currentLocation!!.latitude
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 10F, gpsLocationListener)
            }
        }
        return currentLocation
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getMyLocation()
                } else {
                    Toast.makeText(mContext, "앱을 사용하기 위해서는 위치 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }

        }
    }


    fun updateLocation(): Location?{
        Log.d(TAG, "updateLocation: 함수때려짐")
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "updateLocation: 권한 부여 되지 않음")
            ActivityCompat.requestPermissions(
                mContext as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        } else {
            Log.d(TAG, "updateLocation: 권한 부여됨")

            // 수동으로 위치 구하기
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10F, gpsLocationListener)
            //locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10F, gpsLocationListener)

        }
        return currentLocation

    }

    private var gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "New Latitude: $latitude New Longitude: $longitude"
        }
        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }



    fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mContext as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            getMyLocation()
        }else{
            locationManager = mContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            val location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location
            val lng = location.longitude
            val lat = location.latitude


        }
    }


}