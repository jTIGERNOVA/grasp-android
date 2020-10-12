package com.jtigernova.discoverrestaurants

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.ui.restaurant.RestaurantDetailFragment
import com.jtigernova.discoverrestaurants.ui.restaurants.RestaurantsAdapter
import com.jtigernova.discoverrestaurants.ui.restaurants.RestaurantsFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RestaurantsAdapter.IRestaurantListener {

    private val keyRestaurants = "restaurants"
    private val keyBackStack = "stack"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //only on first creation
        if (savedInstanceState == null) {
            goToFragment(
                //coordinates given by test maker, so we hardcode
                fragment = RestaurantsFragment.newInstance(
                    lat = 37.422740f,
                    lng = -122.139956f
                )
            )
        }
    }

    private fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment, keyRestaurants)
            .addToBackStack(keyBackStack)
            .commit()
    }

    override fun onClicked(restaurant: Restaurant) {
        goToFragment(fragment = RestaurantDetailFragment.newInstance(restaurant))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //check if all fragments are off the stack, if so, pop the stack to finish the activity
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }
}