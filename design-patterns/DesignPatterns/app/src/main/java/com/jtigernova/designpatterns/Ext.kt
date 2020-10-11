package com.jtigernova.designpatterns

import android.util.Log

fun d(tag: String, msg: String) {
    if (BuildConfig.DEBUG)
        Log.d(tag, msg)
}

fun i(tag: String, msg: String) {
    if (BuildConfig.DEBUG)
        Log.i(tag, msg)
}

fun e(tag: String, msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(tag, msg)
}

fun String.log() {
    if (BuildConfig.DEBUG)
        Log.d("General", this)
}

fun String.logError() {
    if (BuildConfig.DEBUG)
        Log.e("General", this)
}