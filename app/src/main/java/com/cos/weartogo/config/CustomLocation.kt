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

    // 좌표 구하기 =======================================================
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
            Log.d(TAG, "getMyLocation: 권한 부여 되지 않음")
            ActivityCompat.requestPermissions(
                mContext as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            getMyLocation()
        } else {
            Log.d(TAG, "getMyLocation: 권한 부여됨")

            // 수동으로 위치 구하기
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

            currentLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (currentLocation != null) {
                val lng = currentLocation!!.longitude
                val lat = currentLocation!!.latitude
                Log.d(TAG, "GPS_PROVIDER: $lat, $lng")
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10F, gpsLocationListener)
            } else {
                currentLocation = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val lng = currentLocation!!.longitude
                val lat = currentLocation!!.latitude
                Log.d(TAG, "NETWORK_PROVIDER: $lat, $lng")
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10F, gpsLocationListener)
            }
        }
        return currentLocation
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult: ")
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
            Log.d(TAG, "onLocationChanged: $msg")

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    }


    // 위도 및 경도를 주소로 변환하기 ===================================
    fun latLngToAddr(lat: Double, lng: Double) : Address?{
        var list: List<Address>? = null
        var geocoder = Geocoder(mContext)

        try {
            list = geocoder.getFromLocation(lat, lng, 10)
        } catch (e: Exception){
            e.printStackTrace()
            Log.d(TAG, "latLngToAddr: 서버에서 주소 변환 에러 발생")
        }
        if(list != null){
            if (list.isEmpty()) {
                Log.d(TAG, "$lat, $lng: 해당 되는 주소 정보는 없습니다")

            } else {
                Log.d(TAG, "$lat, $lng: ${list[0].toString()}")
                return list?.get(0)
            }
        }
       return null
    }

    // 사용자가 입력한 주소값의 위도 및 경도를 받아오기 ====================
    fun addrToLatLng(addr: String): Location?{
        Log.d(TAG, "addrToLatLng: 때려짐")

        var list: List<Address>? = null
        var geocoder = Geocoder(mContext)

        try {

            // addr : 지역 이름, maxResults : 읽을 개수
            list = geocoder.getFromLocationName(addr, 10)

        } catch (e : Exception){
            e.printStackTrace()
            Log.d(TAG, "addrToLatLng: 서버 위도 경도 변환 에러 발생 ") // 토스트 띄워줘야겠지
        }

        if(list != null){
            if (list.isEmpty()) {
                Log.d(TAG, "addrToLatLng: 해당 되는 위도 경도 정보는 없습니다")
                Toast.makeText(mContext, "주소를 정확히 입력해주세요", Toast.LENGTH_SHORT ).show()
            } else {
                Log.d(TAG, "$addr: ${list[0].latitude}") // 위도
                Log.d(TAG, "$addr: ${list[0].longitude}") // 경도

                currentLocation!!.latitude = list[0].latitude
                currentLocation!!.longitude = list[0].longitude

                Log.d(TAG, "addrToLatLng: ${currentLocation!!.latitude}")

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
            Log.d(TAG, "getMyLocation: 권한 부여 되지 않음")
            ActivityCompat.requestPermissions(
                mContext as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            getMyLocation()
        }else{
            locationManager = mContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            var location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location
            if(location == null){
                // gps 를 이용한 좌표조회 실패시 네트워크로 위치 조회
                location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            }
            if (location != null){
                val lng = location!!.longitude
                val lat = location!!.latitude
                Log.d(TAG, "getMyLocation: $lat, $lng")


            }
        }
    }


}