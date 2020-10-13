package com.jtigernova.discoverrestaurants.ui.restaurants

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jtigernova.discoverrestaurants.model.IRestaurant
import com.jtigernova.discoverrestaurants.repository.IRestaurantRepository
import com.jtigernova.discoverrestaurants.ui.base.BaseViewModel

class RestaurantsViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    appRepo: IRestaurantRepository
) : BaseViewModel(savedStateHandle) {
    private val lat: Double = getOrError("lat")
    private val lng: Double = getOrError("lng")

    val restaurantsData: MutableLiveData<LiveData<List<IRestaurant>>> = MutableLiveData()
    val loadingData = MutableLiveData<Boolean>()

    init {
        appRepo.scope = viewModelScope

        restaurantsData.value = appRepo.getRestaurants(lat = lat, lng = lng)
    }
}