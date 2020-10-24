package com.jtigernova.livedata

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.jtigernova.livedata.model.GeneralLiveModel

/**
 * Grid
 */
class GridActivity : AppCompatActivity() {

    private val viewModel: GeneralLiveModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_grid)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Grid - LiveData"
        }

        if (savedInstanceState == null) {
            viewModel.freshUser(name = "NULL")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }
}