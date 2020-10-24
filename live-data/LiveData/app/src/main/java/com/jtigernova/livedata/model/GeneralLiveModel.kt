package com.jtigernova.livedata.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneralLiveModel : ViewModel() {

    private val state: MutableLiveData<GeneralState> = MutableLiveData()
    val userData: LiveData<GeneralState> = state

    fun freshUser(name: String) {
        state.value = GeneralState(user = User(name = name))
    }

    fun setUserName(name: String) {
        state.value = state.value?.setUserName(name)
    }
}