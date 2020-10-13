package com.jtigernova.designpatterns.mydog.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jtigernova.designpatterns.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    protected val myScope =
        CoroutineScope(Dispatchers.Main)

    protected fun argEx(msg: String) = IllegalArgumentException(msg)

    protected fun <T> getOrError(key: String): T {
        return savedStateHandle[key] ?: throw argEx(key)
    }

    override fun onCleared() {

        try {
            myScope.cancel()

            "View model scope cleared: ${this.javaClass.simpleName}".log()
        } catch (ex: Exception) {
        }

        super.onCleared()
    }
}