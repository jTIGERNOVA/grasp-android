package com.jtigernova.showmedogs

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.refresh)
        dog = findViewById(R.id.dog)

        swipeRefreshLayout.setOnRefreshListener(this)

        requestQueue = Volley.newRequestQueue(this)

        onRefresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)


    }

    override fun onStop() {
        super.onStop()

        requestQueue.cancelAll(null)
    }

    class DogData(val url: String?)

    private fun error() {
        Toast.makeText(
            this, "Dog not FOUND!",
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
                        Glide.with(this).load(it).into(dog)
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