package com.pinslog.ww.config

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import com.pinslog.ww.model.LatLng

open class MyLocation(private val mContext: Context) {
    var currentLocation: Location? = null

    /**
     * 현재 좌표를 구합니다.
     */
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


}