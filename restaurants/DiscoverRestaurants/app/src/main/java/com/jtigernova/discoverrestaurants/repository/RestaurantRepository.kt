package com.jtigernova.discoverrestaurants.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.repository.api.IRestaurantApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantRepository(
    private val api: IRestaurantApi
) : IRestaurantRepository {
    override var scope: CoroutineScope? = null
        set(v) {
            api.scope = v
            field = v
        }

    override fun getRestaurants(lat: Float, lng: Float): LiveData<List<Restaurant>> {
        val data = MutableLiveData<List<Restaurant>>()

        runOnMain {
            data.value = api.getRestaurants(lat = lat, lng = lng)
            Log.e(">>", "found: ${data.value!!.size}")
        }

        return data
    }

    private fun runOnMain(block: suspend CoroutineScope.() -> Unit) {
        scope?.launch(
            context = Dispatchers.Main,
            start = CoroutineStart.DEFAULT, block = block
        )
    }
}