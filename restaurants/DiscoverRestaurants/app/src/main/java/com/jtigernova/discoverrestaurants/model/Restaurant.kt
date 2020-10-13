package com.jtigernova.discoverrestaurants.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface IRestaurant {
    val id: String?
    val name: String?
    val description: String?
    val cover_img_url: String?
    val average_rating: String?
    val url: String?
    val status: String?
    val delivery_fee: Float?

    fun asParcelable(): Parcelable? = null
}

/**
 * Restaurant
 */
@Parcelize
data class Restaurant(
<<<<<<< Updated upstream
    val id: String?,
    val name: String?,
    val description: String?,
    val cover_img_url: String?,
    val status: String?,
    val delivery_fee: Float?
) : Parcelable
=======
    override val id: String?,
    override val name: String?,
    override val description: String?,
    override val cover_img_url: String?,
    override val average_rating: String?,
    override val url: String?,
    override val status: String?,
    override val delivery_fee: Float?
) : IRestaurant, Parcelable {

    override fun asParcelable(): Parcelable? = this
}
>>>>>>> Stashed changes
