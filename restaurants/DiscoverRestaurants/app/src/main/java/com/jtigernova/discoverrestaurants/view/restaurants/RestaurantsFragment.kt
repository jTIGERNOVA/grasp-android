package com.jtigernova.discoverrestaurants.view.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.view.BaseFragment

/**
 * A fragment representing a list of Restaurants.
 */
class RestaurantsFragment : BaseFragment() {
    private val stateRestaurants = "restaurants"

    private var data: ArrayList<Restaurant>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check activity
        if (activity !is RestaurantsAdapter.IRestaurantListener) {
            throw Exception(
                "Activity ${activity?.localClassName ?: "unknown"} must implement " +
                        "RestaurantsAdapter.OnClickedListener"
            )
        }

        //check for restaurants in saved state
        if (savedInstanceState != null) {
            val tData = savedInstanceState.getParcelableArrayList<Restaurant>(stateRestaurants)
                ?: arrayListOf<Restaurant>()

            data = tData
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //save restaurants
        data?.let {
            outState.putParcelableArrayList(stateRestaurants, it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_restaurants_list,
            container, false
        ) as RecyclerView

        //check if data has already been loaded
        data?.let {
            with(view) {
                adapter =
                    RestaurantsAdapter(
                        it,
                        activity as RestaurantsAdapter.IRestaurantListener
                    )
            }
            return view
        }

        //show progress during data loading
        val progress = ProgressBar(requireContext())

        with(progress) {
            layoutParams = ViewGroup.LayoutParams(100, 100)
            isIndeterminate = true

            container?.addView(this)
        }

        //data not loaded, so load it
        loadRestaurants(view = view) {
            //not loading anymore
            container?.removeView(progress)
        }

        return view
    }

    /**
     * Handles when a refresh is needed for the fragment
     *
     * @param done Function to run when refresh has finished
     */
    override fun onRefreshNeeded(done: () -> Unit) {

        loadRestaurants(view = view as RecyclerView, done = done)
    }

    /**
     * Loads restaurant data via Door Dash api
     *
     * @param view View
     * @param done Function to run when refresh has finished
     */
    private fun loadRestaurants(view: RecyclerView, done: () -> Unit = {}) {
        view.adapter = null

        //define a coroutine function to all coroutine api execution
        //fire always executes on the main thread
        fire {
            //coordinates given by test maker, so we hardcode
            val lat = 37.422740f
            val lng = -122.139956f

            data = doorDash().getRestaurants(lat = lat, lng = lng)

            with(view) {
                adapter =
                    RestaurantsAdapter(
                        data ?: listOf(),
                        activity as RestaurantsAdapter.IRestaurantListener
                    )
            }

            done()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RestaurantsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}