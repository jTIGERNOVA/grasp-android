package com.jtigernova.discoverrestaurants.view.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.Restaurant


/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantDetailFragment : Fragment() {
    private var restaurant: Restaurant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            restaurant = it.getParcelable(ARG_RESTAURANT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    companion object {
        private const val ARG_RESTAURANT = "restaurant"

        @JvmStatic
        fun newInstance(restaurant: Restaurant) =
            RestaurantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RESTAURANT, restaurant)
                }
            }
    }
}