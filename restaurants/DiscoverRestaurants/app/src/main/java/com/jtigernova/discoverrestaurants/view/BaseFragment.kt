package com.jtigernova.discoverrestaurants.view

import androidx.fragment.app.Fragment
import com.jtigernova.discoverrestaurants.api.DoorDash
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Base fragment for app. Adds convenient functions.
 */
abstract class BaseFragment : Fragment(), CoroutineScope {

    private var doorDash: DoorDash? = null

    /**
     * Dispatch coroutines from main thread
     */
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    /**
     * Gets Door Dash api
     */
    protected fun doorDash(): DoorDash {
        //lazy load api
        doorDash?.let {
            return it
        }

        DoorDash(this).let {
            //set lazy variable
            doorDash = it
            return it
        }
    }

    /**
     * Called when an activity needs this fragment to refresh it's data.
     *
     * @param done Called when refresh is finished. (Optional).
     */
    open fun onRefreshNeeded(done: () -> Unit = {}) {
        done()
    }

    override fun onDestroy() {
        super.onDestroy()

        //cancel any pending requests/coroutines
        try {
            cancel()
        } catch (ex: Exception) {
            //don't care, just wanted to make sure
        }
    }

    /**
     * Using the current scope, runs a coroutine after switching to the main context
     *
     * @param block suspend function to run
     */
    protected fun fire(block: suspend CoroutineScope.() -> Unit) {
        launch(
            context = coroutineContext,
            start = CoroutineStart.DEFAULT, block = block
        )
    }
}