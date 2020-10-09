package com.jtigernova.showmedogs

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val api = "https://random.dog/woof.json"

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dog: ImageView

    private lateinit var requestQueue: RequestQueue

    class DogData(val url: String?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.refresh)
        dog = findViewById(R.id.dog)

        swipeRefreshLayout.setOnRefreshListener(this)

        requestQueue = Volley.newRequestQueue(this)

        onRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> onRefresh()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()

        requestQueue.cancelAll(null)
    }

    private fun error() {
        Toast.makeText(
            this, getString(R.string.dog_not_found),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun loadNewDog() {
        swipeRefreshLayout.isRefreshing = true

        requestQueue.add(
            StringRequest(
                Request.Method.GET, api,
                Response.Listener<String> { response ->
                    val url = Gson().fromJson(response, DogData::class.java).url

                    url?.let {
                        Glide.with(this).load(it).placeholder(R.drawable.ic_downloading)
                            .error(R.drawable.ic_network_error).into(dog)
                    }

                    swipeRefreshLayout.isRefreshing = false
                },
                Response.ErrorListener {
                    error()

                    swipeRefreshLayout.isRefreshing = false
                })
        )
    }

    override fun onRefresh() {
        loadNewDog()
    }
}