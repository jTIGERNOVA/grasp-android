package com.jtigernova.discoverrestaurants.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jtigernova.discoverrestaurants.model.IRestaurant
import com.jtigernova.discoverrestaurants.repository.api.IRestaurantApi
import com.jtigernova.discoverrestaurants.repository.database.DbRestaurantDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantRepository(
    private val api: IRestaurantApi,
    private val restaurantDao: DbRestaurantDao
) : IRestaurantRepository {
    override var scope: CoroutineScope? = null
        set(v) {
            api.scope = v
            field = v
        }

    override fun getRestaurants(lat: Double, lng: Double): LiveData<List<IRestaurant>> {
        val data = MutableLiveData<List<IRestaurant>>()

        runOnMain {
            //TODO cache based on approximate gps coordinates
            if (restaurantDao.any()) {
                data.value = restaurantDao.get()
            } else {
                data.value = api.getRestaurants(lat = lat, lng = lng)
            }
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