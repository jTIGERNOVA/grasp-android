package com.jtigernova.discoverrestaurants

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jtigernova.discoverrestaurants.model.Restaurant
import com.jtigernova.discoverrestaurants.view.BaseFragment
import com.jtigernova.discoverrestaurants.view.restaurant.RestaurantDetailFragment
import com.jtigernova.discoverrestaurants.view.restaurants.RestaurantsAdapter
import com.jtigernova.discoverrestaurants.view.restaurants.RestaurantsFragment

/**
 * Main activity
 */
class MainActivity : AppCompatActivity(), RestaurantsAdapter.IRestaurantListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val keyRestaurants = "restaurants"
    private val keyBackStack = "stack"

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener(this)

        //only on first creation
        if (savedInstanceState == null) {
            goToFragment(fragment = RestaurantsFragment.newInstance())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {

                onRefresh()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isOnMainFragment(): Boolean {
        return supportFragmentManager.backStackEntryCount <= 1
    }

    private fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment, keyRestaurants)
            .addToBackStack(keyBackStack)
            .commit()
    }

    override fun onClicked(restaurant: Restaurant) {
        goToFragment(fragment = RestaurantDetailFragment.newInstance(restaurant))
    }

    override fun onBackPressed() {
        super.onBackPressed()

        //check if all fragments are off the stack, if so, pop the stack to finish the activity
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    override fun onRefresh() {
        if (!isOnMainFragment()) {
            //do not refresh if user is not on restaurants fragment
            //TODO ELSE either fresh something or remove the menu item
            swipeRefreshLayout.isRefreshing = false

            return
        }

        swipeRefreshLayout.isRefreshing = true

        supportFragmentManager.fragments.forEach {
            if (it is BaseFragment) {
                it.onRefreshNeeded {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}