package com.jtigernova.designpatterns.data.api

import com.google.gson.Gson
import com.jtigernova.designpatterns.data.model.Dog
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

@Suppress("BlockingMethodInNonBlockingContext")
class DogApi(private val scope: CoroutineScope) :
    IDogApi {
    companion object {
        /**
         * Path to Door Dash api
         */
        const val api = "https://random.dog/woof.json"
    }

    fun destroy() {
        try {
            scope.cancel()
        } catch (ex: Exception) {
        }
    }

    /**
     * Using the current scope, runs a coroutine after switching to a background context
     *
     * @param block suspend function to run
     */
    private fun net(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(
            context = Dispatchers.IO,
            start = CoroutineStart.DEFAULT, block = block
        )
    }

    override suspend fun getDog(): Dog? {
        var res: Dog? = null

        net {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(api)
                .get()
                .build()

            val response = client.newCall(request).execute().body?.string()

            response?.let {
                res = Gson().fromJson<Dog>(response, Dog::class.java)
            }
        }.join()

        return res
    }
}