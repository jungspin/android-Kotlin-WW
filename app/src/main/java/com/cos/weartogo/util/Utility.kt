package com.cos.weartogo.util

import kotlin.math.floor

object Utility {

    fun getRealTemp(temp: Double): String {
        val c = temp - 273.15
        return floor(c).toString()
    }
}