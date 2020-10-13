package com.jtigernova.discoverrestaurants

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    companion object {
        const val REQUEST_CODE_LOCATION_PERMISSION = 29

        const val locationProvider = LocationManager.GPS_PROVIDER
        const val locationService = Context.LOCATION_SERVICE

        const val backupLat = 37.422740
        const val backupLng = -122.139956
    }

    private val keyRestaurants = "restaurants"
    private val keyBackStack = "stack"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //only on first creation
        if (savedInstanceState != null) {
            return
        }

        tryRestaurantLoad()
    }

    private fun tryRestaurantLoad() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            goToRestaurants()
            return
        }

        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        } else {
            goToRestaurants()
        }
    }

    @SuppressLint("MissingPermission")
    private fun goToRestaurants() {
        val locationManager = getSystemService(locationService)
                as LocationManager
        val loc = locationManager.getLastKnownLocation(locationProvider)

        goToFragment(
            //coordinates given by test maker, so we hardcode
            fragment = RestaurantsFragment.newInstance(
                lat = loc?.latitude ?: backupLat,
                lng = loc?.longitude ?: backupLng
            )
        )
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQUEST_CODE_LOCATION_PERMISSION) {

            return
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            goToRestaurants()
        } else {
            Log.e(
                "Main", "User denied location permission :(. " +
                        "Will not start location monitor"
            )

            Toast.makeText(
                this, getString(R.string.permission_denied),
                Toast.LENGTH_LONG
            ).show()

            goToFragment(
                //coordinates given by test maker, so we hardcode
                fragment = RestaurantsFragment.newInstance(
                    lat = backupLat,
                    lng = backupLng
                )
            )
        }
    }
}