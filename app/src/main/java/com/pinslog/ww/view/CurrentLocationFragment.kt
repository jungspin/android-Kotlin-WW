package com.pinslog.ww.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.pinslog.ww.R
import com.pinslog.ww.adapter.ForecastAdapter
import com.pinslog.ww.base.BaseFragment
import com.pinslog.ww.config.CustomLocation
import com.pinslog.ww.config.MyLocation
import com.pinslog.ww.databinding.FragmentCurrentLocationBinding
import com.pinslog.ww.model.WearInfo
import com.pinslog.ww.util.Utility
import com.pinslog.ww.viewmodel.WeatherViewModel

/**
 * @since 2022-11-01
 */
private const val TAG = "CurrentLocationFragment"

class CurrentLocationFragment : BaseFragment<FragmentCurrentLocationBinding>() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var mLocation: CustomLocation
    private var lat: Double? = 0.0
    private var lng: Double? = 0.0
    private lateinit var forecastAdapter: ForecastAdapter

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCurrentLocationBinding {
        binding = FragmentCurrentLocationBinding.inflate(inflater, container, false)
        return binding
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
    override fun initListener() {
        binding.mainWearingInfo.itemWearing1.setOnClickListener(wearingInfoListener)
        binding.mainWearingInfo.itemWearing2.setOnClickListener(wearingInfoListener)
        binding.mainWearingInfo.itemWearing3.setOnClickListener(wearingInfoListener)
        binding.mainWearingInfo.itemWearingDescription.setOnClickListener {
            binding.mainWearingInfo.itemWearing1.visibility = View.VISIBLE
            binding.mainWearingInfo.itemWearing2.visibility = View.VISIBLE
            binding.mainWearingInfo.itemWearing3.visibility = View.VISIBLE
            binding.mainWearingInfo.itemWearingDescription.visibility = View.INVISIBLE
        }
    }

    override fun initSetting() {
        mLocation = CustomLocation(mContext)
        binding.mainWearingInfo.itemWearingDescription.visibility = View.INVISIBLE

        forecastAdapter = ForecastAdapter()
        binding.mainForecastRv.adapter = forecastAdapter
    }

    @SuppressLint("SetTextI18n")
    override fun initViewModel() {
        // 뷰모델 프로바이더를 통해 뷰모델 가져오기
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        weatherViewModel.getValue.observe(this) {
            if (it != null) {
                // 현재 온도
                val temp = Utility.getRealTemp(it.main.temp)
                binding.mainCurrentTemp.text = "$temp °C"
                // 옷 정보 설정
                val wearInfo = Utility.getWearingInfo(mContext, temp)
                val wearInfoBinding = binding.mainWearingInfo
                wearInfoBinding.itemWearingDescription.text = wearInfo.infoDescription
                val infoList = wearInfo.wearingList

                wearInfoBinding.itemWearing1.setImageResource(infoList[0])
                wearInfoBinding.itemWearing2.setImageResource(infoList[1])
                wearInfoBinding.itemWearing3.setImageResource(infoList[2])

                // 날씨 아이콘 설정
                binding.mainWeatherImg.setImageResource(Utility.setCodeToImg(it.weather[0].id))
                // 날씨 설명
                binding.mainWeatherDescription.text = it.weather[0].description
            }
        }

        weatherViewModel.getForecastValue.observe(this) { data ->
            if (data != null) {
                forecastAdapter.setItems(data)
            }
        }
    }

    // TODO splash 로 옮기
    @SuppressLint("SetTextI18n")
    override fun initData() {
        // 좌표로 날씨 받아오기
        val myLocation = MyLocation(mContext)
        lat = myLocation.getMyLocation()?.lat
        lng = myLocation.getMyLocation()?.lng

        if (lat != null && lng != null) {
            // 위치 이름 설정
            val addr = myLocation.latLngToAddress(lat!!, lng!!)
            var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
            if (address.contains("null")) {
                address = address.replace("null", "")
                binding.mainCurrentLocation.text = address
            } else {
                binding.mainCurrentLocation.text = address
            }
        } else {
            // TODO 위치 받아올 수 없음 알리기
            // 좌표로 날씨 받아오기 (임시)
            lat = 35.1518
            lng = 129.0658
        }
        weatherViewModel.getCurrentWeatherLatLng(lat!!, lng!!)
        weatherViewModel.getForecastLatLng(lat!!, lng!!)


    }

    fun setInfo(textView : TextView, imageView: ImageView, wearInfo: WearInfo){
        textView.text = wearInfo.infoDescription
        val infoList = wearInfo.wearingList
        imageView.setImageResource(infoList[0])
        imageView.setImageResource(infoList[1])
        imageView.setImageResource(infoList[2])
    }



}