package com.cos.weartogo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.cos.weartogo.data.LatLngDTO
import java.lang.Exception

private const val TAG = "CustomLocation"
class CustomLocation(private val mContext: Context) {

    private var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2



    var currentLocation: Location? = null

    // 좌표 구하기 =======================================================
    fun getMyLocation(): Location? {
        Log.d(TAG, "getMyLocation: 함수 때려짐")

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
            //getMyLocation()
        } else {
            Log.d(TAG, "getMyLocation: 권한 부여됨")

            // 수동으로 위치 구하기

            locationManager = mContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            val locationProvider = LocationManager.NETWORK_PROVIDER
            currentLocation = locationManager!!.getLastKnownLocation(locationProvider)
            if (currentLocation != null) {
                val lng = currentLocation!!.longitude
                val lat = currentLocation!!.latitude
                Log.d(TAG, "getMyLocation: $lat, $lng")
            }
        }
        return currentLocation
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
                Log.d(TAG, "addrToLatLng: 해당 되는 주소 정보는 없습니다")
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
}