package com.jtigernova.discoverrestaurants.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Restaurant
 */
@Parcelize
data class Restaurant(
    val id: String?,
    val name: String?,
    val description: String?,
    val cover_img_url: String?,
    val average_rating: String?,
    val url: String?,
    val status: String?,
    val delivery_fee: Float?
) : Parcelable