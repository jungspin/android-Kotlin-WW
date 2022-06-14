package com.cos.weartogo.config

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat


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

    /**
     * 위도 및 경도를 주소로 변환합니다.
     * @param lat 위도
     * @param lng 경도
     * @return 변환된 주소값
     */
    fun latLngToAddress(lat: Double, lng: Double) : Address?{
        var list: List<Address>? = null
        val geocoder = Geocoder(mContext)

        try {
            list = geocoder.getFromLocation(lat, lng, 10)
        } catch (e: Exception){
            e.printStackTrace()
        }
        if(list != null){
            if (list.isEmpty()) {
                Toast.makeText(mContext, "해당 되는 주소 정보는 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                return list[0]
            }
        }
       return null
    }

    /**
     * 사용자가 입력한 주소값의 위도 및 경도를 받아옵니다
     * @param addr 주소값
     * @return 해당 주소값의 위도 및 경도
     */
    fun addressToLatLng(addr: String): Location?{
        var list: List<Address>? = null
        val geocoder = Geocoder(mContext)

        try {
            // addr : 지역 이름, maxResults : 읽을 개수
            list = geocoder.getFromLocationName(addr, 10)

        } catch (e : Exception){
            e.printStackTrace()
        }

        if(list != null){
            if (list.isEmpty()) {
                Toast.makeText(mContext, "주소를 정확히 입력해주세요", Toast.LENGTH_SHORT ).show()
            } else {
                currentLocation!!.latitude = list[0].latitude
                currentLocation!!.longitude = list[0].longitude
            }
        }
        return currentLocation
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