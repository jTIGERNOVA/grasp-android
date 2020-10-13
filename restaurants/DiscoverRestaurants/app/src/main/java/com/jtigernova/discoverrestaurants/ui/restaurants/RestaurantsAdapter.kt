package com.jtigernova.discoverrestaurants.ui.restaurants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jtigernova.discoverrestaurants.R
import com.jtigernova.discoverrestaurants.model.IRestaurant

/**
 * Restaurants Adapter
 */
class RestaurantsAdapter(
    private val values: List<IRestaurant>,
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
        holder.itemRating.text = item.average_rating
        holder.itemCategory.text =
            holder.itemView.context.getString(R.string.format_status, item.status)

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

        if (item.average_rating == "0.0" || item.average_rating.isNullOrBlank()) {
            holder.itemRating.visibility = View.INVISIBLE
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
        fun onClicked(restaurant: IRestaurant)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImg: ImageView = view.findViewById(R.id.itemImg)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemCategory: TextView = view.findViewById(R.id.itemCategory)
        val itemRating: TextView = view.findViewById(R.id.itemRating)

        override fun toString(): String {
            return super.toString() + " '${itemName.text}'"
        }
    }
}