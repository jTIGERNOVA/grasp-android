package com.jtigernova.discoverrestaurants.ui.restaurants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.Restaurant

/**
 * Restaurants Adapter
 */
class RestaurantsAdapter(
    private val values: List<Restaurant>,
    private val restaurantListener: IRestaurantListener?
) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_restaurant_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemName.text = item.name
        holder.itemCategory.text = item.status
        holder.itemDistance.text = item.delivery_fee.toString()

        item.cover_img_url?.let {
            Glide.with(holder.itemView).load(it)
                .error(R.color.colorBackground)
                .placeholder(R.color.colorBackground)
                .into(holder.itemImg)
        }

        //pass on click to restaurant listener
        holder.itemView.setOnClickListener {
            restaurantListener?.onClicked(item)
        }
    }

    /**
     * Restaurant listener for events
     */
    interface IRestaurantListener {
        /**
         * When a restaurant has been clicked
         *
         * @param restaurant Restaurant that was clicked
         */
        fun onClicked(restaurant: Restaurant)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImg: ImageView = view.findViewById(R.id.itemImg)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemCategory: TextView = view.findViewById(R.id.itemCategory)
        val itemDistance: TextView = view.findViewById(R.id.itemDistance)

        override fun toString(): String {
            return super.toString() + " '${itemName.text}'"
        }
    }
}