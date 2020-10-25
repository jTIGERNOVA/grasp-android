package com.jtigernova.animations.ui.doc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DocViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Doc Fragment"
    }
    val text: LiveData<String> = _text
}