package com.jtigernova.designpatterns.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jtigernova.designpatterns.data.model.AppPreferences
import com.jtigernova.designpatterns.data.model.Dog
import com.jtigernova.designpatterns.data.api.IDogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class AppRepo(
    private val scope: CoroutineScope,
    private val api: IDogApi
) : IAppRepo {
    private fun fire(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(
            context = scope.coroutineContext,
            start = CoroutineStart.DEFAULT, block = block
        )
    }

    override fun getPreferences(): AppPreferences {
        TODO("Not yet implemented")
    }

    override fun signIn(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun register(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDog(): LiveData<Dog> {
        val data = MutableLiveData<Dog>()

        fire {
            api.getDog()?.let {
                data.value = it
            }
        }
        return data
    }
}