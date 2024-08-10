package com.pinslog.ww.util

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val CURRENT_TIME_PATTERN = "yyyy년 MM월 dd일 E요일 HH시 mm분"

fun Long.toDate(pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale("ko", "KR"))
    val date = Date(this)
    return sdf.format(date).toString()
}

/**
 * 클릭 시 물결효과를 없애기 위함
 * - see : https://velog.io/@jmseb3/compose-ripple-%ED%9A%A8%EA%B3%BC-%EC%A0%9C%EA%B1%B0
 */
@SuppressLint("UnnecessaryComposedModifier")
inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit = {},
): Modifier = composed {
    this.clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }

}
