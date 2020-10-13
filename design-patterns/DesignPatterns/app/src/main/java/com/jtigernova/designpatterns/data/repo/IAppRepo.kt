package com.jtigernova.designpatterns.data.repo

import androidx.lifecycle.LiveData
import com.jtigernova.designpatterns.data.model.AppPreferences
import com.jtigernova.designpatterns.data.model.Dog

interface IAppRepo {
    fun getPreferences(): AppPreferences

    fun signIn(email: String, password: String): Boolean

    fun register(email: String, password: String): Boolean

    fun getDog(): LiveData<Dog>
}