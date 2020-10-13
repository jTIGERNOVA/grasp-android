package com.jtigernova.discoverrestaurants.modules

import com.jtigernova.discoverrestaurants.repository.IRestaurantRepository
import com.jtigernova.discoverrestaurants.repository.RestaurantRepository
import com.jtigernova.discoverrestaurants.repository.api.DoorDash
import com.jtigernova.discoverrestaurants.repository.api.IRestaurantApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
object AppModule {
    @Provides
    fun provideRestaurantApi(): IRestaurantApi {
        return DoorDash()
    }

    @Provides
    fun provideRestaurantRepository(): IRestaurantRepository {
        return RestaurantRepository(provideRestaurantApi())
    }
}