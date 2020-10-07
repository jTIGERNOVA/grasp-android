package com.jtigernova.discoverrestaurants.view.restaurants

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.Restaurant

/**
 * A fragment representing a list of Restaurants.
 */
class RestaurantsFragment : Fragment() {
    private val stateRestaurants = "restaurants"

    private var data: ArrayList<Restaurant>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity !is RestaurantsAdapter.OnClickedListener) {
            throw Exception(
                "Activity ${activity?.localClassName ?: "unknown"} must implement " +
                        "RestaurantsAdapter.OnClickedListener"
            )
        }

        //handle input arguments
        arguments?.let {

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
                layoutManager = LinearLayoutManager(context)
                adapter =
                    RestaurantsAdapter(
                        it,
                        activity as RestaurantsAdapter.OnClickedListener
                    )
            }
            return view
        }

        loadRestaurants(view = view)

        return view
    }

    private fun loadRestaurants(view: RecyclerView) {
        //simulate a network call
        Handler().postDelayed({
            data = arrayListOf(
                Restaurant("0", "R 1", "The best", "http", "yes", 1.20f),
                Restaurant("0", "R 1", "The best", "http", "yes", 1.20f),
                Restaurant("0", "R 1", "The best", "http", "yes", 1.20f)
            )

            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter =
                    RestaurantsAdapter(
                        data ?: listOf(),
                        activity as RestaurantsAdapter.OnClickedListener
                    )
            }
        }, 3000)
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