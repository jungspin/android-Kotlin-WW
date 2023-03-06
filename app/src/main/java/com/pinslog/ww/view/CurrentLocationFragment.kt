package com.pinslog.ww.view

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.pinslog.ww.R
import com.pinslog.ww.adapter.ForecastAdapter
import com.pinslog.ww.base.BaseFragment
import com.pinslog.ww.databinding.FragmentCurrentLocationBinding
import com.pinslog.ww.databinding.ItemWearingInfoBinding
import com.pinslog.ww.model.LatLng
import com.pinslog.ww.util.MyLocation
import com.pinslog.ww.util.Utility
import com.pinslog.ww.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @since 2022-11-01
 */
private const val TAG = "CurrentLocationFragment"
@AndroidEntryPoint
class CurrentLocationFragment : BaseFragment<FragmentCurrentLocationBinding>() {

    private val weatherViewModel: WeatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var myLocation: MyLocation
    private lateinit var forecastAdapter: ForecastAdapter
    private lateinit var currentWearingInfo: ItemWearingInfoBinding

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCurrentLocationBinding {
        binding = FragmentCurrentLocationBinding.inflate(inflater, container, false)
        currentWearingInfo = binding.mainCurrentWearinfoRoot
        return binding
    }

    override fun initSetting() {
        myLocation = MyLocation(mContext)
        forecastAdapter = ForecastAdapter()
        binding.mainForecastRv.adapter = forecastAdapter

        binding.mainSwipeRefreshRoot.setColorSchemeResources(R.color.main)
    }

    override fun initListener() {
        binding.mainShareBtn.setOnClickListener(shareBtnClickListener)
        binding.mainLookInfoBtn.setOnClickListener(wearingInfoClickListener)
        binding.mainSwipeRefreshRoot.setOnRefreshListener {
            forecastAdapter.clearItems()
            initData()
            binding.mainSwipeRefreshRoot.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewModel() {
        // 뷰모델 프로바이더를 통해 뷰모델 가져오기
        weatherViewModel.getValue.observe(this) {
            if (it != null) {
                // 현재 온도
                val weatherInfo = it.main
                val temp = Utility.getRealTemp(weatherInfo.temp)
                binding.mainCurrentTemp.text = "${temp}"

                // 현재 시간
                val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분", Locale.KOREAN)
                val date = dateFormat.format(Calendar.getInstance().time)
                binding.mainCurrentTime.text = "$date"

                // 옷 정보 설정
                val wearInfo = Utility.getWearingInfo(mContext, temp)
                currentWearingInfo.itemWearingDescription.text = wearInfo.infoDescription
                val infoList = wearInfo.wearingList

                currentWearingInfo.itemWearing1.setImageResource(infoList[0])
                currentWearingInfo.itemWearing2.setImageResource(infoList[1])
                currentWearingInfo.itemWearing3.setImageResource(infoList[2])

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

    // TODO splash 로 옮기기
    @SuppressLint("SetTextI18n")
    override fun initData() {
        // 좌표
        val latLng = myLocation.getCurrentLatLng()
        val lat = latLng.lat
        val lng = latLng.lng
        // 위치 이름 설정
        val address = myLocation.latLngToAddress(latLng.lat, latLng.lng)
        binding.mainCurrentLocation.text = address

        weatherViewModel.getCurrentWeatherLatLng(lat, lng)
        weatherViewModel.getForecastLatLng(lat, lng)
    }

    /**
     * 따로 빼두는 것이 좋을까?
     */
    private val wearingInfoClickListener = View.OnClickListener {
        if (currentWearingInfo.itemInfoRoot.visibility != View.VISIBLE) {
            binding.mainLookInfoBtn.text = "닫기"
            TransitionManager.beginDelayedTransition(binding.mainCurrentCardView, AutoTransition())
            currentWearingInfo.itemInfoRoot.visibility = View.VISIBLE
        } else {
            binding.mainLookInfoBtn.text = "오늘 뭐입지?"
            TransitionManager.beginDelayedTransition(
                currentWearingInfo.itemInfoRoot,
                AutoTransition()
            )
            currentWearingInfo.itemInfoRoot.visibility = View.GONE
        }
    }

    private val shareBtnClickListener = View.OnClickListener {
        //TODO 수정하기
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://pinslog.page.link/muUh")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


}