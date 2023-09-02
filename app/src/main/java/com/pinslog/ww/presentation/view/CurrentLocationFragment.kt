package com.pinslog.ww.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.gms.location.LocationServices
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.DynamicLink.SocialMetaTagParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.*
import com.pinslog.ww.R
import com.pinslog.ww.presentation.view.adapter.ForecastAdapter
import com.pinslog.ww.base.BaseFragment
import com.pinslog.ww.databinding.FragmentCurrentLocationBinding
import com.pinslog.ww.databinding.ItemWearingInfoBinding
import com.pinslog.ww.util.*
import com.pinslog.ww.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * @since 2022-11-01
 */
@AndroidEntryPoint
class CurrentLocationFragment : BaseFragment<FragmentCurrentLocationBinding>() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var forecastAdapter: ForecastAdapter
    private lateinit var currentWearingInfo: ItemWearingInfoBinding
    private lateinit var myLocation: MyLocation

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCurrentLocationBinding {
        binding = FragmentCurrentLocationBinding.inflate(inflater, container, false)
        currentWearingInfo = binding.mainCurrentWearinfoRoot
        return binding
    }

    override fun initSetting() {
        forecastAdapter = ForecastAdapter()
        binding.mainForecastRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.mainForecastRv.adapter = forecastAdapter

        binding.mainSwipeRefreshRoot.setColorSchemeResources(R.color.main)
    }

    override fun initListener() {
        binding.mainShareBtn.setOnClickListener(shareBtnClickListener)
        binding.mainLookInfoBtn.setOnClickListener(wearingInfoClickListener)
        binding.mainSwipeRefreshRoot.setOnRefreshListener {
            forecastAdapter.clearItems()
            checkPermission()
            binding.mainSwipeRefreshRoot.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewModel() {
        binding.weatherViewModel = weatherViewModel
        binding.lifecycleOwner = this

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val geocoder = Geocoder(mContext)
        weatherViewModel.getCurrentLocation(locationManager, fusedLocationProviderClient, geocoder)
    }

    override fun doNextAfterGranted() {
        myLocation = MyLocation(mContext)
        // 좌표
        val latLng = myLocation.getCurrentLatLng()
        val lat = latLng.lat
        val lng = latLng.lng
        // 위치 이름 설정
//        val address = myLocation.latLngToAddress(latLng.lat, latLng.lng)
//        binding.mainCurrentLocation.text = address

        weatherViewModel.getForecastLatLng(lat, lng)
    }

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
        createDynamicLink()
    }

    /**
     * 동적 링크를 생성합니다.
     */
    private fun createDynamicLink(){
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://www.pinslog.com/"))
            .setDomainUriPrefix("https://pinslog.page.link")
            .setAndroidParameters(AndroidParameters.Builder().build())
            .setSocialMetaTagParameters(
                SocialMetaTagParameters.Builder()
                    .setTitle("Wear Weather")
                    .setDescription("기온별 옷차림 안내 어플")
                    .build())
            .buildShortDynamicLink()
            .addOnSuccessListener {
                val shortLink = it.shortLink!!
                createShareSheet(shortLink)
            }
            .addOnFailureListener {
                Toast.makeText(mContext, "일시적 문제로 데이터를 공유할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * 공유할 내용을 생성합니다.
     *
     * @param dynamicLink 동적 링크
     */
    private fun createShareSheet(dynamicLink: Uri){
        val content = """
            ${binding.mainCurrentLocation.text}의 현재 기온은 ${binding.mainCurrentTemp.text}°.
            ${binding.mainCurrentWearinfoRoot.itemWearingDescription.text}를 추천드려요.
            자세한 정보가 궁금하다면? $dynamicLink
        """.trimIndent()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }



}