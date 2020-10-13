package com.jtigernova.designpatterns

import com.jtigernova.designpatterns.data.repo.AppRepo
import com.jtigernova.designpatterns.data.api.DogApi
import com.jtigernova.designpatterns.data.repo.IAppRepo
import com.jtigernova.designpatterns.data.api.IDogApi
import kotlinx.coroutines.CoroutineScope

object Dep {
//    private var appRepo: AppRepo? = null

    fun clear() {
//        appRepo = null
    }

    fun api(scope: CoroutineScope): IDogApi {
        return DogApi(scope)
    }

    fun appRepo(scope: CoroutineScope): IAppRepo {
        val api = api(scope)

        return AppRepo(
            scope = scope,
            api = api
        )
    }
}