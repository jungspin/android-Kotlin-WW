package com.cos.weartogo.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cos.weartogo.R
import com.cos.weartogo.config.CustomLocation
import com.cos.weartogo.config.WeatherAPI
import com.cos.weartogo.databinding.FragmentMainBinding
import com.cos.weartogo.view.SearchActivity
import com.cos.weartogo.viewmodel.MainViewModel
import kotlin.math.floor

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mContext: Context

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mLocation : CustomLocation
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mLocation = CustomLocation(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)

        initSetting()
        initData()
        initLr()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initLr() {
        binding.ivInfo1.setOnClickListener(View.OnClickListener {
            binding.tvShowDescription.isVisible = true

            binding.tvInfoClick.isVisible = false
        })
        binding.ivInfo2.setOnClickListener(View.OnClickListener {
            binding.tvShowDescription.isVisible = true

            binding.tvInfoClick.isVisible = false
        })
        binding.ivInfo3.setOnClickListener(View.OnClickListener {
            binding.tvShowDescription.isVisible = true

            binding.tvInfoClick.isVisible = false
        })

        binding.btnSearch.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, SearchActivity::class.java)
            getResult.launch(intent)
        })

        getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data?.getStringExtra("data")
                if (data != null) {
                    val qLat = mLocation.addrToLatLng(data)?.latitude
                    val qLng = mLocation.addrToLatLng(data)?.longitude

                    if(qLat != null && qLng != null){
                        // 위치 이름 설정
                        val addr = mLocation.latLngToAddr(qLat, qLng)
                        var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
                        if (address.contains("null")){
                            address = address.replace("null", "")
                            binding.tvLocation.text = address
                        }
                        binding.tvLocation.text = address
                        mainViewModel.getWeatherLatLng(qLat, qLng, WeatherAPI.KEY, WeatherAPI.LANG)
                        mainViewModel.getValue.observe(requireActivity(), Observer {
                            if(it != null) {

                                // 현재 온도
                                val temp = getRealTemp(it.main.feels_like)
                                binding.tvTemp.text = "$temp °C"
                                // 옷 정보 설정
                                setInfo(temp)
                                // 날씨 아이콘 설정
                                setCodeToImg(it.weather[0].id)
                                // 날씨 설명
                                binding.tvDescription.text = it.weather[0].description
                            }
                        })

                    } else {
                        // gps == null
                        Toast.makeText(mContext, "일시적으로 위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initSetting() {
        binding.tvShowDescription.isVisible = false
        binding.tvInfoClick.isVisible = true
        // 뷰모델 프로바이더를 통해 뷰모델 가져오기
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]


    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        // 좌표로 날씨 받아오기


        if (mLocation != null) {
            val lat = mLocation.getMyLocation()?.latitude
            var lng = mLocation.getMyLocation()?.longitude

            if(lat != null && lng != null){
                // 위치 이름 설정
                val addr = mLocation.latLngToAddr(lat, lng)
                var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
                if (address.contains("null")){
                    address = address.replace("null", "")
                    binding.tvLocation.text = address
                } else {
                    binding.tvLocation.text = address
                }
                mainViewModel.getWeatherLatLng(lat, lng, WeatherAPI.KEY, WeatherAPI.LANG)
                mainViewModel.getValue.observe(requireActivity(), Observer {
                    if(it != null) {

                        // 현재 온도
                        val temp = getRealTemp(it.main.feels_like)
                        binding.tvTemp.text = "$temp °C"
                        // 옷 정보 설정
                        setInfo(temp)
                        // 날씨 아이콘 설정
                        setCodeToImg(it.weather[0].id)
                        // 날씨 설명
                        binding.tvDescription.text = it.weather[0].description



                    }
                })
            }
        } else {
            // gps == null
            Toast.makeText(mContext, "일시적으로 위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
        }
    }



    private fun getRealTemp(temp: Double): Double {
        val c = temp - 273.15
        return floor(c)
    }

    private fun setCodeToImg(code : Int){
        when (code){
            in 200..232 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_thunder)
            }
            in 300..321 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_small_rainy)
            }
            in 500..531->{
                binding.ivWeather.setImageResource(R.drawable.ic_rainy_2)
            }
            in 600..522->{
                binding.ivWeather.setImageResource(R.drawable.ic_snowy)
            }
            in 700..781->{
                binding.ivWeather.setImageResource(R.drawable.ic_dusty)
            }
            800 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_clear)
            }
            801 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_small_cloudy)
            }
            802, 803->{
                binding.ivWeather.setImageResource(R.drawable.ic_cloudy_2)
            }
            804 -> {
                binding.ivWeather.setImageResource(R.drawable.ic_more_cloudy)
            }
        }
    }

    private fun setInfo(temp: Double){
        if (temp >= 28.0){
            binding.tvShowDescription.text = getString(R.string.description_28)
            binding.ivInfo1.setImageResource(R.drawable.ic_sleeve)
            binding.ivInfo2.setImageResource(R.drawable.ic_half_pants)
            binding.ivInfo3.setImageResource(R.drawable.ic_half_sleeve)
        }
        when (temp) {
            in 23.0..27.0 ->{
                binding.tvShowDescription.text = getString(R.string.description_23)
                binding.ivInfo1.setImageResource(R.drawable.ic_half_sleeve)
                binding.ivInfo2.setImageResource(R.drawable.ic_half_pants)
                binding.ivInfo3.setImageResource(R.drawable.ic_light_shirt)
            }
            in 20.0..22.0 ->{
                binding.tvShowDescription.text = getString(R.string.description_20)
                binding.ivInfo1.setImageResource(R.drawable.ic_light_cardigan)
                binding.ivInfo2.setImageResource(R.drawable.ic_cotton_pants)
                binding.ivInfo3.setImageResource(R.drawable.ic_long_sleeve)
            }
            in 17.0..19.0 -> {
                binding.tvShowDescription.text = getString(R.string.description_17)
                binding.ivInfo1.setImageResource(R.drawable.ic_light_knit)
                binding.ivInfo2.setImageResource(R.drawable.ic_jean)
                binding.ivInfo3.setImageResource(R.drawable.ic_mtm)
            }
            in 12.0..16.0 ->{
                binding.tvShowDescription.text = getString(R.string.description_12)
                binding.ivInfo1.setImageResource(R.drawable.ic_jacket_2)
                binding.ivInfo2.setImageResource(R.drawable.ic_jean)
                binding.ivInfo3.setImageResource(R.drawable.ic_cardigan)
            }
            in 9.0..11.0 ->{
                binding.tvShowDescription.text = getString(R.string.description_9)
                binding.ivInfo1.setImageResource(R.drawable.ic_jacket_2)
                binding.ivInfo2.setImageResource(R.drawable.ic_coat)
                binding.ivInfo3.setImageResource(R.drawable.ic_knit)
            }
            in 5.0..8.0 ->{
                binding.tvShowDescription.text = getString(R.string.description_5)
                binding.ivInfo1.setImageResource(R.drawable.ic_knit)
                binding.ivInfo2.setImageResource(R.drawable.ic_coat)
                binding.ivInfo3.setImageResource(R.drawable.ic_jacket)
            }
        }
        if (temp <= 4.0){
            binding.tvShowDescription.text = getString(R.string.description_4)
            binding.ivInfo1.setImageResource(R.drawable.ic_safari)
            binding.ivInfo2.setImageResource(R.drawable.ic_hat)
            binding.ivInfo3.setImageResource(R.drawable.ic_muffler)
        }
    }
}