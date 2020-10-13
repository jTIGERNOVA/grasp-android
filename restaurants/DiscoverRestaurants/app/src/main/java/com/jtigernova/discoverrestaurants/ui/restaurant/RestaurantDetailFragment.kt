package com.jtigernova.discoverrestaurants.ui.restaurant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.IRestaurant
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.ui.toMoney


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

        restaurant?.let { tRest ->
            view.findViewById<TextView>(R.id.restaurantName).text = tRest.name
            view.findViewById<TextView>(R.id.restaurantDesc).text = tRest.description
            view.findViewById<TextView>(R.id.restaurantDeliveryFee).text =
                getString(
                    R.string.format_delivery_fee, tRest.delivery_fee.toMoney()
                )
            view.findViewById<TextView>(R.id.restaurantWebsite).setOnClickListener {
                tRest.url?.let { tUrl ->
                    val url = "https://doordash.com$tUrl"

                    with(Intent(Intent.ACTION_VIEW)) {
                        data = Uri.parse(url)
                        requireActivity().startActivity(this)
                    }
                }
            }

            if (tRest.status == "closed") {
                view.findViewById<TextView>(R.id.restaurantStatus).text = getString(R.string.closed)
            } else {
                view.findViewById<TextView>(R.id.restaurantStatus).text =
                    getString(R.string.format_status, tRest.status)
            }

            val img = view.findViewById<ImageView>(R.id.restaurantImg)

            tRest.cover_img_url?.let { restaurantImg ->
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
        fun newInstance(restaurant: IRestaurant) =
            RestaurantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RESTAURANT, restaurant.asParcelable())
                }
            }
    }
}