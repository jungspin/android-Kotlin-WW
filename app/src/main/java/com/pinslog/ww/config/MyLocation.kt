package com.pinslog.ww.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.*
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.pinslog.ww.model.LatLng

private const val TAG = "MyLocation"

class MyLocation(private val mContext: Context) {
    var currentLocation: Location? = null
    var latLng = LatLng(0.0, 0.0)

    /**
     * 현재 좌표를 구합니다.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLatLng(): LatLng {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // gps 활성화 여부 체크
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mContext, "GPS를 활성화해주세요", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
        } else {
            // 이전 저장 위치 받아오기
            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                // null 이면 requestLocationUpdates 호출
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    10f,
                    locationChangeListener
                )
                // network provider 로 대략적인 위치정보 받아오기
                val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (networkLocation == null) {
                    Toast.makeText(mContext, "현재 위치 정보를 가져올 수 없어요", Toast.LENGTH_SHORT).show()
                } else {
                    latLng.lat = networkLocation.latitude
                    latLng.lng = networkLocation.longitude
                }
            } else {
                latLng.lat = location.latitude
                latLng.lng = location.longitude
            }
        }
        return latLng
    }

    private var locationChangeListener: LocationListener =
        LocationListener { location ->
            latLng.lat = location.latitude
            latLng.lng = location.longitude
            Log.d(TAG, ": ${location.latitude}, ${location.longitude}")
        }


    /**
     * 위도 및 경도를 주소로 변환합니다.
     * @param lat 위도
     * @param lng 경도
     * @return 변환된 주소값
     */
    fun latLngToAddress(lat: Double, lng: Double): String {
        var list: List<Address> = mutableListOf()
        val geocoder = Geocoder(mContext)

        try {
            list = geocoder.getFromLocation(lat, lng, 10)
            //Log.d(TAG, "latLngToAddress: ${list}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (list.isNotEmpty()) {
            val addr = list[0]
            val address = "${addr.adminArea} ${addr.locality} ${addr.thoroughfare}"
            if (address.contains("null")) {
                address.replace("null", "")
            } else {
                address
            }
        } else {
            Toast.makeText(mContext, "해당 되는 주소 정보는 없습니다", Toast.LENGTH_SHORT).show()
            ""
        }

    }

    /**
     * 사용자가 입력한 주소값의 위도 및 경도를 받아옵니다
     * @param addr 주소값
     * @return 해당 주소값의 위도 및 경도
     */
    fun addressToLatLng(addr: String): Location? {
        var list: List<Address>? = null
        val geocoder = Geocoder(mContext)

        try {
            // addr : 지역 이름, maxResults : 읽을 개수
            list = geocoder.getFromLocationName(addr, 10)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (list != null) {
            if (list.isEmpty()) {
                Toast.makeText(mContext, "주소를 정확히 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                currentLocation!!.latitude = list[0].latitude
                currentLocation!!.longitude = list[0].longitude
            }
        }
        return currentLocation
    }


}