package com.jtigernova.discoverrestaurants.view

import android.icu.text.NumberFormat
import android.os.Build

/**
 * App extensions
 */

/**
 * Presents a number as currency. Uses the current currency on the device
 */
fun Float?.toMoney(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        NumberFormat.getCurrencyInstance().format(this ?: 0F)
    } else {
        java.text.NumberFormat.getCurrencyInstance().format(this ?: 0F)
    }
}