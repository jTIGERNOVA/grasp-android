package com.jtigernova.designpatterns.data.api

import com.jtigernova.designpatterns.data.model.Dog

interface IDogApi {
    suspend fun getDog(): Dog?
}