package com.jtigernova.discoverrestaurants.view.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.view.toMoney


/**
 * Restaurant detail fragment
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
        val view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false)

        restaurant?.let {
            view.findViewById<TextView>(R.id.restaurantName).text = it.name
            view.findViewById<TextView>(R.id.restaurantDesc).text = it.description
            view.findViewById<TextView>(R.id.restaurantDeliveryFee).text =
                getString(
                    R.string.format_delivery_fee, it.delivery_fee.toMoney()
                )

            if (it.status == "closed") {
                view.findViewById<TextView>(R.id.restaurantStatus).text = getString(R.string.closed)
            } else {
                view.findViewById<TextView>(R.id.restaurantStatus).text =
                    getString(R.string.format_status, it.status)
            }

            val img = view.findViewById<ImageView>(R.id.restaurantImg)

            it.cover_img_url?.let { restaurantImg ->
                Glide.with(requireContext()).load(restaurantImg)
                    .error(R.color.colorBackground)
                    .placeholder(R.color.colorBackground)
                    .into(img)
            }
        }

        return view
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