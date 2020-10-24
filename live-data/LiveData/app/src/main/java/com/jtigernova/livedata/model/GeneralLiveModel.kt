package com.jtigernova.livedata.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneralLiveModel : ViewModel() {

    private val user: MutableLiveData<GeneralState> = MutableLiveData()

    val userData: LiveData<GeneralState> = user

//    val nameChange = Transformations.map(user, {
//        it.user.name
//    })

    fun freshUser(name: String) {
        user.value = GeneralState(user = User(name = name))
    }

    fun setUserName(name: String) {
        user.value = user.value?.setUserName(name)
    }
}