package com.jtigernova.discoverrestaurants.ui.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Restaurants.
 */
@AndroidEntryPoint
class RestaurantsFragment : BaseFragment() {
    private val viewModel: RestaurantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check activity
        if (activity !is RestaurantsAdapter.IRestaurantListener) {
            throw Exception(
                "Activity ${activity?.localClassName ?: "unknown"} must implement " +
                        "RestaurantsAdapter.OnClickedListener"
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (viewModel.restaurantsData.hasActiveObservers()) return

        viewModel.restaurantsData.value?.observe(viewLifecycleOwner) {

            loadRestaurants(data = it, view = view as RecyclerView) {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_restaurants_list,
            container, false
        ) as RecyclerView
    }

    /**
     * Loads restaurant data via Door Dash api
     *
     * @param view View
     * @param done Function to run when refresh has finished
     */
    private fun loadRestaurants(data: List<Restaurant>, view: RecyclerView, done: () -> Unit = {}) {
        view.adapter = RestaurantsAdapter(data, activity as RestaurantsAdapter.IRestaurantListener)

        done()
    }

    companion object {
        @JvmStatic
        fun newInstance(lat: Float, lng: Float) =
            RestaurantsFragment().apply {
                arguments = Bundle().apply {
                    putFloat("lat", lat)
                    putFloat("lng", lng)
                }
            }
    }
}