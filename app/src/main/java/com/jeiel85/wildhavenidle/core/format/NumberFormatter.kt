package com.jeiel85.wildhavenidle.core.format

import java.util.Locale
import kotlin.math.abs

object NumberFormatter {
    fun compact(value: Double): String {
        val absolute = abs(value)
        return when {
            absolute >= 1_000_000.0 -> String.format(Locale.US, "%.1fM", value / 1_000_000.0)
            absolute >= 1_000.0 -> String.format(Locale.US, "%.1fK", value / 1_000.0)
            else -> String.format(Locale.US, "%.0f", value)
        }
    }

    fun perSecond(value: Double): String = "${compact(value)}/sec"
}
