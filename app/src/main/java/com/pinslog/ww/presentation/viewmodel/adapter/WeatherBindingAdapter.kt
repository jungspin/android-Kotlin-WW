package com.pinslog.ww.presentation.viewmodel.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.presentation.model.HourlyForecast
import com.pinslog.ww.presentation.view.adapter.ForecastAdapter
import com.pinslog.ww.presentation.view.adapter.HourlyForecastAdapter

/**
 * WeatherViewModel 바인딩 어댑터
 * @author jungspin
 * @since 2023/06/06 3:23 PM
 */
@BindingAdapter("imageRes")
fun imageLoad(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("app:items")
fun setForecastList(recyclerView: RecyclerView, items: List<ForecastDO?>?) {
    items?.let {
        val recyclerAdapter = recyclerView.adapter as ForecastAdapter
        recyclerAdapter.dataList = it.toMutableList()
        recyclerAdapter.notifyDataSetChanged()
    }
}

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("app:hourlyItems")
fun setHourlyForecastList(recyclerView: RecyclerView, items: Map<String, HourlyForecast>?){
    items?.let { map ->
        val recyclerAdapter = recyclerView.adapter as HourlyForecastAdapter
        recyclerAdapter.dataList = map.map { it.value }.toMutableList()
        recyclerAdapter.notifyDataSetChanged()
    }
}

@BindingAdapter("popViewVisible")
fun setPopViewVisible(view: ConstraintLayout, pop: Double) {
    if (pop == 0.0) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}
