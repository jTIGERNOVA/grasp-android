package com.jtigernova.discoverrestaurants.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 */
@Parcelize
data class Restaurant(
    val id: String?,
    val name: String?,
    val description: String?,
    val cover_img_url: String?,
    val status: String?,
    val delivery_fee: Float?
) : Parcelable