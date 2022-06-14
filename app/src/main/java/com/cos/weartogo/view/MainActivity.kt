package com.cos.weartogo.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cos.weartogo.R
import com.cos.weartogo.config.CustomLocation
import com.cos.weartogo.config.WeatherAPI
import com.cos.weartogo.databinding.ActivityMainBinding
import com.cos.weartogo.viewmodel.MainViewModel
import kotlin.math.floor


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mContext: Context = this
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mLocation: CustomLocation
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        initSetting()
        initData()
        initLr()

        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private val wearingInfoListener = View.OnClickListener {
        binding.mainWearingInfo.itemWearingDescription.bringToFront()
        binding.mainWearingInfo.itemWearing1.visibility = View.INVISIBLE
        binding.mainWearingInfo.itemWearing2.visibility = View.INVISIBLE
        binding.mainWearingInfo.itemWearing3.visibility = View.INVISIBLE
        binding.mainWearingInfo.itemWearingDescription.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun initLr() {
        binding.mainWearingInfo.itemWearing1.setOnClickListener(wearingInfoListener)
        binding.mainWearingInfo.itemWearing2.setOnClickListener(wearingInfoListener)
        binding.mainWearingInfo.itemWearing3.setOnClickListener(wearingInfoListener)
        binding.mainWearingInfo.itemWearingDescription.setOnClickListener {
            binding.mainWearingInfo.itemWearing1.visibility = View.VISIBLE
            binding.mainWearingInfo.itemWearing2.visibility = View.VISIBLE
            binding.mainWearingInfo.itemWearing3.visibility = View.VISIBLE
            binding.mainWearingInfo.itemWearingDescription.visibility = View.INVISIBLE
        }

//
//        binding.btnSearch.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, SearchActivity::class.java)
//            getResult.launch(intent)
//        })
//
//        getResult = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == AppCompatActivity.RESULT_OK) {
//                val data = result.data?.getStringExtra("data")
//                if (data != null) {
//                    val qLat = mLocation.addressToLatLng(data)?.latitude
//                    val qLng = mLocation.addressToLatLng(data)?.longitude
//
//                    if(qLat != null && qLng != null){
//                        // 위치 이름 설정
//                        val addr = mLocation.latLngToAddress(qLat, qLng)
//                        var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
//                        if (address.contains("null")){
//                            address = address.replace("null", "")
//                            binding.tvLocation.text = address
//                        }
//                        binding.tvLocation.text = address
//                        mainViewModel.getWeatherLatLng(qLat, qLng, WeatherAPI.KEY, WeatherAPI.LANG)
//                        mainViewModel.getValue.observe(requireActivity(), Observer {
//                            if(it != null) {
//
//                                // 현재 온도
//                                val temp = getRealTemp(it.main.feels_like)
//                                binding.tvTemp.text = "$temp °C"
//                                // 옷 정보 설정
//                                setInfo(temp)
//                                // 날씨 아이콘 설정
//                                setCodeToImg(it.weather[0].id)
//                                // 날씨 설명
//                                binding.tvDescription.text = it.weather[0].description
//                            }
//                        })
//
//                    } else {
//                        // gps == null
//                        Toast.makeText(mContext, "일시적으로 위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }

    private fun initSetting() {
        mLocation = CustomLocation(mContext)
        // 뷰모델 프로바이더를 통해 뷰모델 가져오기
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.mainWearingInfo.itemWearingDescription.visibility = View.INVISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        // 좌표로 날씨 받아오기
        val lat = mLocation.getMyLocation()?.latitude
        val lng = mLocation.getMyLocation()?.longitude

        if (lat != null && lng != null) {
            // 위치 이름 설정
            val addr = mLocation.latLngToAddress(lat, lng)
            var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
            if (address.contains("null")) {
                address = address.replace("null", "")
                binding.mainCurrentLocation.text = address
            } else {
                binding.mainCurrentLocation.text = address
            }
            mainViewModel.getWeatherLatLng(lat, lng, WeatherAPI.KEY, WeatherAPI.LANG)
            mainViewModel.getValue.observe(this) {
                if (it != null) {

                    // 현재 온도
                    val temp = getRealTemp(it.main.temp)
                    binding.mainCurrentTemp.text = "$temp °C"
                    // 옷 정보 설정
                    setInfo(temp)
                    // 날씨 아이콘 설정
                    binding.mainWeatherImg.setImageResource(setCodeToImg(it.weather[0].id))
                    // 날씨 설명
                    binding.mainWeatherDescription.text = it.weather[0].description
                }
            }
        }
    }


    private fun getRealTemp(temp: Double): Double {
        val c = temp - 273.15
        return floor(c)
    }

    private fun setCodeToImg(code: Int): Int {
        var resourceId = 0
        when (code) {
            in 200..232 -> {
                resourceId = R.drawable.ic_thunder
            }
            in 300..321 -> {
                resourceId = R.drawable.ic_small_rainy
            }
            in 500..531 -> {
                resourceId = R.drawable.ic_rainy_2
            }
            in 522..600 -> {
                resourceId = R.drawable.ic_snowy
            }
            in 700..781 -> {
                resourceId = R.drawable.ic_dusty
            }
            800 -> {
                resourceId = R.drawable.ic_clear
            }
            801 -> {
                resourceId = R.drawable.ic_small_cloudy
            }
            802, 803 -> {
                resourceId = R.drawable.ic_cloudy_2
            }
            804 -> {
                resourceId = R.drawable.ic_more_cloudy
            }
        }
        return resourceId
    }

    private fun setInfo(temp: Double) {
        var wearDescription = ""
        var wearInfoList: ArrayList<Int> = arrayListOf()
        if (temp >= 28.0) {
            wearDescription = getString(R.string.description_28)
            wearInfoList = arrayListOf(
                R.drawable.ic_sleeve,
                R.drawable.ic_half_pants,
                R.drawable.ic_half_sleeve
            )
        }
        when (temp) {
            in 23.0..27.0 -> {
                wearDescription = getString(R.string.description_23)
                wearInfoList = arrayListOf(
                    R.drawable.ic_half_sleeve,
                    R.drawable.ic_half_pants,
                    R.drawable.ic_light_shirt
                )
            }
            in 20.0..22.0 -> {
                wearDescription = getString(R.string.description_20)
                wearInfoList = arrayListOf(
                    R.drawable.ic_light_cardigan,
                    R.drawable.ic_cotton_pants,
                    R.drawable.ic_long_sleeve
                )
            }
            in 17.0..19.0 -> {
                wearDescription = getString(R.string.description_17)
                wearInfoList =
                    arrayListOf(R.drawable.ic_light_knit, R.drawable.ic_jean, R.drawable.ic_mtm)
            }
            in 12.0..16.0 -> {
                wearDescription = getString(R.string.description_12)
                wearInfoList =
                    arrayListOf(R.drawable.ic_jacket_2, R.drawable.ic_jean, R.drawable.ic_cardigan)
            }
            in 9.0..11.0 -> {
                wearDescription = getString(R.string.description_9)
                wearInfoList =
                    arrayListOf(R.drawable.ic_jacket_2, R.drawable.ic_coat, R.drawable.ic_knit)
            }
            in 5.0..8.0 -> {
                wearDescription = getString(R.string.description_5)
                wearInfoList =
                    arrayListOf(R.drawable.ic_knit, R.drawable.ic_coat, R.drawable.ic_jacket)
            }
        }
        if (temp <= 4.0) {
            wearDescription = getString(R.string.description_4)
            wearInfoList =
                arrayListOf(R.drawable.ic_safari, R.drawable.ic_hat, R.drawable.ic_muffler)
        }
        binding.mainWearingInfo.itemWearingDescription.text = wearDescription
        binding.mainWearingInfo.itemWearing1.setImageResource(wearInfoList[0])
        binding.mainWearingInfo.itemWearing2.setImageResource(wearInfoList[1])
        binding.mainWearingInfo.itemWearing3.setImageResource(wearInfoList[2])
    }

}