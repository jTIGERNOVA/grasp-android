package com.jtigernova.discoverrestaurants.ui.restaurants

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.repository.IRestaurantRepository
import com.jtigernova.discoverrestaurants.ui.base.BaseViewModel

class RestaurantsViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    appRepo: IRestaurantRepository
) : BaseViewModel(savedStateHandle) {
    private val lat: Float = getOrError("lat")
    private val lng: Float = getOrError("lng")

    var restaurantsData: MutableLiveData<LiveData<List<Restaurant>>> = MutableLiveData()

    init {
        appRepo.scope = viewModelScope

        restaurantsData.value = appRepo.getRestaurants(lat = lat, lng = lng)
    }
}