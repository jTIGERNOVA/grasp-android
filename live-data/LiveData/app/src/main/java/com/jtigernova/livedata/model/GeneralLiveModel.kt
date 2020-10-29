package com.jtigernova.livedata.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneralLiveModel : ViewModel() {

    private val state: MutableLiveData<GeneralState> = MutableLiveData()
    val userData: LiveData<GeneralState> = state

    private val mMock: MutableLiveData<String> = MutableLiveData()
    val mockData: LiveData<String> = mMock

    fun refresh(name: String, gender: String = "F") {
        state.value = GeneralState(user = User(name = name, gender = gender))
    }

    fun setUserName(name: String) {
        state.value = state.value?.setUserName(name)
    }

    var gender: String?
        get() = state.value?.user?.gender
        set(value) {
            state.value = state.value?.setGender(value)
        }

    var mock: String?
        get() = mMock.value
        set(value) {
            mMock.value = value
        }
}