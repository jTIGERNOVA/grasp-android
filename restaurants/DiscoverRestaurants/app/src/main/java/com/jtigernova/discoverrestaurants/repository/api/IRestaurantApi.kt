package com.jtigernova.discoverrestaurants.repository.api

import com.jtigernova.discoverrestaurants.model.Restaurant
import kotlinx.coroutines.CoroutineScope

interface IRestaurantApi {
    var scope: CoroutineScope?

    suspend fun getRestaurants(lat: Double, lng: Double): List<Restaurant>
}