package com.jtigernova.discoverrestaurants.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jtigernova.discoverrestaurants.model.IRestaurant

@Dao
interface DbRestaurantDao {
    @Insert(onConflict = REPLACE)
    fun save(data: DbRestaurant)

    @Query("SELECT * FROM dbrestaurant WHERE id = :id")
    suspend fun get(id: String): DbRestaurant

    @Query("SELECT * FROM dbrestaurant")
    suspend fun get(): List<DbRestaurant>

    @Query("SELECT EXISTS(SELECT 1 FROM dbrestaurant LIMIT 1)")
    suspend fun any(): Boolean
}