package com.jtigernova.animations.ui.bar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BarViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Bar Fragment"
    }
    val text: LiveData<String> = _text
}