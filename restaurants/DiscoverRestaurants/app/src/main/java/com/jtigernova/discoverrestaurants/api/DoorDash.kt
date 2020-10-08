package com.jtigernova.discoverrestaurants.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jtigernova.discoverrestaurants.model.Restaurant
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

@Suppress("BlockingMethodInNonBlockingContext")
class DoorDash(private val scope: CoroutineScope) {

    companion object {
        const val api = "https://api.doordash.com/v2/restaurant/"
    }

    private fun net(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(
            //switch to background context/thread
            context = Dispatchers.IO,
            start = CoroutineStart.DEFAULT, block = block
        )
    }

    suspend fun getRestaurants(lat: Float, lng: Float): ArrayList<Restaurant> {
        var res = arrayListOf<Restaurant>()

        net {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("$api?lat=$lat&lng=$lng")
                .get()
                .build()

            val response = client.newCall(request).execute().body?.string()

            response?.let {
                val tType = TypeToken.get(Array<Restaurant>::class.java).type
                val tRestaurants = Gson().fromJson<Array<Restaurant>>(
                    response,
                    tType
                )

                res = ArrayList(tRestaurants.toList())
            }
        }.join()

        return res
    }
}