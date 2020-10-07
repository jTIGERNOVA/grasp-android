package com.jtigernova.discoverrestaurants.view

import androidx.fragment.app.Fragment

/**
 * Base fragment for app. Adds convenient functions.
 */
abstract class BaseFragment : Fragment() {

    /**
     * Called when an activity needs this fragment to refresh it's data.
     *
     * @param done Called when refresh is finished. (Optional).
     */
    abstract fun onRefreshNeeded(done: () -> Unit = {})
}