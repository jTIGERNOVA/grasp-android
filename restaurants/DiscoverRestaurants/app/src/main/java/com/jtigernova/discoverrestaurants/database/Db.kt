package com.jtigernova.discoverrestaurants.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase

//learn more about Room at: https://developer.android.com/training/data-storage/room
@Database(entities = [DbRestaurant::class], version = 1, exportSchema = false)
abstract class Db : RoomDatabase() {
    companion object {
        const val NAME = "discover"
    }

    abstract fun restaurantDao(): DbRestaurantDao
}