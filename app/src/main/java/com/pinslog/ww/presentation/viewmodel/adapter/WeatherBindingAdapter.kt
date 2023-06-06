package com.pinslog.ww.presentation.viewmodel.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
* WeatherViewModel 바인딩 어댑터
* @author jungspin
* @since 2023/06/06 3:23 PM
*/
@BindingAdapter("imageRes")
fun imageLoad(imageView: ImageView, resId: Int){
    imageView.setImageResource(resId)
}
