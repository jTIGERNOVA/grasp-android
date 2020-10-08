package com.jtigernova.discoverrestaurants.view

import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Base fragment for app. Adds convenient functions.
 */
abstract class BaseFragment : Fragment(), CoroutineScope {

    /**
     * Dispatch coroutines from main thread
     */
    override val coroutineContext: CoroutineContext = Dispatchers.Main

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

    protected fun fire(block: suspend CoroutineScope.() -> Unit) {
        launch(
            context = coroutineContext,
            start = CoroutineStart.DEFAULT, block = block
        )
    }
}