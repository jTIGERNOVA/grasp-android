package com.jtigernova.discoverrestaurants.repository

import androidx.lifecycle.LiveData
import com.jtigernova.discoverrestaurants.model.Restaurant
import kotlinx.coroutines.CoroutineScope

interface IRestaurantRepository {
    var scope: CoroutineScope?

    fun getRestaurants(lat: Double, lng: Double): LiveData<List<Restaurant>>
}