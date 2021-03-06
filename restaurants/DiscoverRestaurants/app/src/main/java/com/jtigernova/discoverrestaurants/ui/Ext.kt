package com.jtigernova.discoverrestaurants.ui

import android.icu.text.NumberFormat
import android.os.Build
import android.util.Log
import com.jtigernova.discoverrestaurants.BuildConfig

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

fun String.log() {
    if (BuildConfig.DEBUG)
        Log.d("General", this)
}