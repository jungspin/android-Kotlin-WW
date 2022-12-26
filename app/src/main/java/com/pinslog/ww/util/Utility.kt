package com.pinslog.ww.util

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.core.animation.doOnEnd
import com.pinslog.ww.R
import com.pinslog.ww.model.WearInfo
import java.lang.Exception
import kotlin.math.floor
import kotlin.math.round

// TODO lazy load 되게 만들기
object Utility {

    /**
     * 켈빈을 섭씨로 변환합니다.
     */
    fun getRealTemp(temp: Double): String {
        val c = temp - 273.15
        return c.toInt().toString()
    }

    /**
     * 날씨 아이콘의 id를 반환합니다.
     */
    fun setCodeToImg(code: Int): Int {
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

    /**
     * 의복정보를 반환합니다.
     */
    fun getWearingInfo(mContext: Context, tempString: String): WearInfo {
        var wearDescription = ""
        var wearInfoList: ArrayList<Int> = arrayListOf()
        val temp = tempString.toDouble()
        if (temp >= 28.0) {
            wearDescription = mContext.getString(R.string.description_28)
            wearInfoList = arrayListOf(
                R.drawable.ic_sleeve,
                R.drawable.ic_half_pants,
                R.drawable.ic_half_sleeve
            )
        }
        when (temp) {
            in 23.0..27.0 -> {
                wearDescription = getDescription(mContext, R.string.description_23)
                wearInfoList = arrayListOf(
                    R.drawable.ic_half_sleeve,
                    R.drawable.ic_half_pants,
                    R.drawable.ic_light_shirt
                )
            }
            in 20.0..22.0 -> {
                wearDescription = getDescription(mContext, R.string.description_20)
                wearInfoList = arrayListOf(
                    R.drawable.ic_light_cardigan,
                    R.drawable.ic_cotton_pants,
                    R.drawable.ic_long_sleeve
                )
            }
            in 17.0..19.0 -> {
                wearDescription = getDescription(mContext, R.string.description_17)
                wearInfoList =
                    arrayListOf(R.drawable.ic_light_knit, R.drawable.ic_jean, R.drawable.ic_mtm)
            }
            in 12.0..16.0 -> {
                wearDescription = getDescription(mContext, R.string.description_12)
                wearInfoList =
                    arrayListOf(R.drawable.ic_jacket_2, R.drawable.ic_jean, R.drawable.ic_cardigan)
            }
            in 9.0..11.0 -> {
                wearDescription = getDescription(mContext, R.string.description_9)
                wearInfoList =
                    arrayListOf(R.drawable.ic_jacket_2, R.drawable.ic_coat, R.drawable.ic_knit)
            }
            in 5.0..8.0 -> {
                wearDescription = getDescription(mContext, R.string.description_5)
                wearInfoList =
                    arrayListOf(R.drawable.ic_knit, R.drawable.ic_coat, R.drawable.ic_jacket)
            }
        }
        if (temp <= 4.0) {
            wearDescription = getDescription(mContext, R.string.description_4)
            wearInfoList =
                arrayListOf(R.drawable.ic_safari, R.drawable.ic_hat, R.drawable.ic_muffler)
        }
        return WearInfo(wearDescription, wearInfoList)
//        binding.mainWearingInfo.itemWearingDescription.text = wearDescription
//        binding.mainWearingInfo.itemWearing1.setImageResource(wearInfoList[0])
//        binding.mainWearingInfo.itemWearing2.setImageResource(wearInfoList[1])
//        binding.mainWearingInfo.itemWearing3.setImageResource(wearInfoList[2])
    }

    /**
     * 의복 설명 문자열을 반환합니다.
     */
    private fun getDescription(mContext: Context, id: Int): String {
        return "${mContext.getString(id)}"
    }

    /**
     * 카드가 뒤집히는 애니메이션을 적용합니다.
     */
    fun flipWearingInfo(mContext: Context, visibleView: View, invisibleView: View) {
        try {
            visibleView.visibility = View.VISIBLE
            val flipOutAnimatorSet =
                AnimatorInflater.loadAnimator(mContext, R.animator.flip_out) as AnimatorSet
            flipOutAnimatorSet.setTarget(invisibleView)

            val flipInAnimatorSet =
                AnimatorInflater.loadAnimator(mContext, R.animator.flip_in) as AnimatorSet
            flipInAnimatorSet.setTarget(visibleView)
            flipOutAnimatorSet.start()
            flipInAnimatorSet.start()
            flipInAnimatorSet.doOnEnd {
                invisibleView.visibility = View.GONE
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }


}