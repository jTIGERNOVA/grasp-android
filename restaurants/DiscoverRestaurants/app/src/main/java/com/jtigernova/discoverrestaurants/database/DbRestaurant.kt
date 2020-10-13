package com.jtigernova.discoverrestaurants.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jtigernova.discoverrestaurants.model.IRestaurant

@Entity
data class DbRestaurant(
    @PrimaryKey override val id: String,
    override val name: String?,
    override val description: String?,
    override val cover_img_url: String?,
    override val average_rating: String?,
    override val url: String?,
    override val status: String?,
    override val delivery_fee: Float?
) : IRestaurant {
}