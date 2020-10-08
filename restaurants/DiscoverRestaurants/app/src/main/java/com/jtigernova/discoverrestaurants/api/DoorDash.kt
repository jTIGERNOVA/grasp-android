package com.jtigernova.discoverrestaurants.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jtigernova.discoverrestaurants.model.Restaurant
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Door Dash api.
 *
 * @param scope Containing scope for all api calls
 */
//suppress warning because warning does not acknowledge coroutines as background processing
@Suppress("BlockingMethodInNonBlockingContext")
class DoorDash(private val scope: CoroutineScope) {

    companion object {
        /**
         * Path to Door Dash api
         */
        const val api = "https://api.doordash.com/v2/restaurant/"
    }

    /**
     * Using the current scope, runs a coroutine after switching to a background context
     *
     * @param block suspend function to run
     */
    private fun net(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(
            //switch to background context/thread
            context = Dispatchers.IO,
            start = CoroutineStart.DEFAULT, block = block
        )
    }

    /**
     * Gets restaurants close to a given coordinates
     *
     * @param lat Latitude
     * @param lng Longitude
     */
    suspend fun getRestaurants(lat: Float, lng: Float): ArrayList<Restaurant> {
        var res = arrayListOf<Restaurant>()

        //run in background
        net {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("$api?lat=$lat&lng=$lng")
                .get()
                .build()

            val response = client.newCall(request).execute().body?.string()

            response?.let {
                //parse response json
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