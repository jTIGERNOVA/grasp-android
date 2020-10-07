package com.jtigernova.discoverrestaurants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.view.restaurant.RestaurantDetailFragment
import com.jtigernova.discoverrestaurants.view.restaurants.RestaurantsAdapter
import com.jtigernova.discoverrestaurants.view.restaurants.RestaurantsFragment

class MainActivity : AppCompatActivity(), RestaurantsAdapter.OnClickedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, RestaurantsFragment.newInstance(), "restaurants")
                .addToBackStack("stack")
                .commit()
        }
    }

    override fun onClicked(restaurant: Restaurant) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, RestaurantDetailFragment.newInstance(restaurant), "restaurants")
            .addToBackStack("stack")
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        //check if all fragments are off the stack, if so, pop the stack to finish the activity
        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        }
    }
}