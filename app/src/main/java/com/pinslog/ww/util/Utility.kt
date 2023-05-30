package com.pinslog.ww.util

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.core.animation.doOnEnd
import com.pinslog.ww.R
import com.pinslog.ww.model.WearInfo

// TODO lazy load 되게 만들기
object Utility {

    /**
     * 켈빈을 섭씨로 변환합니다.
     */
    fun getRealTempAsString(temp: Double): String {
        val c = temp - 273.15
        return c.toInt().toString()
    }

    fun getRealTemp(temp: Double): Int {
        return (temp - 273.15).toInt()
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
    fun getWearingInfo(temp: Double): WearInfo {
        var wearDescription = ""
        var wearInfoList: ArrayList<Int> = arrayListOf()
        val temp = temp.toDouble()
        if (temp >= 28.0) {
            //wearDescription = mContext.getString(R.string.description_28)
            wearDescription = "민소매, 반팔, 반바지, 원피스"
            wearInfoList = arrayListOf(
                R.drawable.ic_sleeve,
                R.drawable.ic_half_pants,
                R.drawable.ic_half_sleeve
            )
        }
        when (temp) {
            in 23.0..27.0 -> {
                //wearDescription = getDescription(mContext, R.string.description_23)
                wearDescription = "반팔, 얇은 셔츠, 반바지, 면바지"
                wearInfoList = arrayListOf(
                    R.drawable.ic_half_sleeve,
                    R.drawable.ic_half_pants,
                    R.drawable.ic_light_shirt
                )
            }
            in 20.0..22.0 -> {
                //wearDescription = getDescription(mContext, R.string.description_20)
                wearDescription = "얇은 가디건, 긴팔, 면바지, 청바지"
                wearInfoList = arrayListOf(
                    R.drawable.ic_light_cardigan,
                    R.drawable.ic_cotton_pants,
                    R.drawable.ic_long_sleeve
                )
            }
            in 17.0..19.0 -> {
                //wearDescription = getDescription(mContext, R.string.description_17)
                wearDescription = "얇은 니트, 맨투맨, 가디건, 청바지"
                wearInfoList =
                    arrayListOf(R.drawable.ic_light_knit, R.drawable.ic_jean, R.drawable.ic_mtm)
            }
            in 12.0..16.0 -> {
                //wearDescription = getDescription(mContext, R.string.description_12)
                wearDescription = "자켓, 가디건, 야상, 스타킹, 청바지, 면바지"
                wearInfoList =
                    arrayListOf(R.drawable.ic_jacket_2, R.drawable.ic_jean, R.drawable.ic_cardigan)
            }
            in 9.0..11.0 -> {
                //wearDescription = getDescription(mContext, R.string.description_9)
                wearDescription = "자켓, 트렌치코트, 야상, 니트, 청바지, 스타킹"
                wearInfoList =
                    arrayListOf(R.drawable.ic_jacket_2, R.drawable.ic_coat, R.drawable.ic_knit)
            }
            in 5.0..8.0 -> {
                //wearDescription = getDescription(mContext, R.string.description_5)
                wearDescription = "코트, 가죽자켓, 히트텍, 니트, 레깅스"
                wearInfoList =
                    arrayListOf(R.drawable.ic_knit, R.drawable.ic_coat, R.drawable.ic_jacket)
            }
        }
        if (temp <= 4.0) {
            //wearDescription = getDescription(mContext, R.string.description_4)
            wearDescription = "패딩, 두꺼운 코트, 목도리, 기모제품"
            wearInfoList =
                arrayListOf(R.drawable.ic_safari, R.drawable.ic_hat, R.drawable.ic_muffler)
        }
        return WearInfo(wearDescription, wearInfoList)
    }

    /**
     * 의복 설명 문자열을 반환합니다.
     */
    private fun getDescription(mContext: Context, id: Int): String {
        return mContext.getString(id)
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