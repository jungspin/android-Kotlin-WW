package com.cos.weartogo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

private const val TAG = "TestActivity"
class TestActivity : AppCompatActivity() {
    private var mContext: Context = this

    private var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        getMyLocation()

    }

    // 좌표 구하기 =======================================================
    private fun getMyLocation(): Location? {
        Log.d(TAG, "getMyLocation: 함수 때려짐")
        var currentLocation: Location? = null
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "getMyLocation: 권한 부여 되지 않음")
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            getMyLocation() //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {
            Log.d(TAG, "getMyLocation: 권한 부여됨")

            // 수동으로 위치 구하기

            val locationProvider = LocationManager.NETWORK_PROVIDER
            currentLocation = locationManager!!.getLastKnownLocation(locationProvider)
            if (currentLocation != null) {
                val lng = currentLocation.longitude
                val lat = currentLocation.latitude
                Log.d(TAG, "getMyLocation: $lat, $lng")
            }
        }
        return currentLocation
    }

//    private fun setPermission() {
//        Log.d(TAG, "setPermission: ")
//        val permissionListener: PermissionListener = object : PermissionListener {
//            override fun onPermissionGranted() {
//                // 권한 허용 시
//                Log.d(TAG, "onPermissionGranted: 권한 허용")
//            }
//
//            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
//                Log.d(TAG, "onPermissionGranted: 권한 거부")
//            }
//
//
//        }
//        TedPermission.with(this)
//            .setPermissionListener(permissionListener)
//            .setRationaleMessage("위치 접근 권한 설정이 필요합니다")
//            .setDeniedMessage("거부 하시면 앱을 사용할 수 없습니다")
//            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//            .check()
//    }

}