package com.jtigernova.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jtigernova.livedata.model.GeneralLiveModel

/**
 * Data Binding. NOTE: This activity is broken!
 */
class DataBindActivity : AppCompatActivity() {

    private val viewModel: GeneralLiveModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_data_binding, null, true)

        setContentView(R.layout.activity_data_binding)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        // Obtain binding
        val binding: ViewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_data_binding)

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        if (savedInstanceState == null) {
            viewModel.refresh(name = "NULL")

            viewModel.mock = "NULL"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }
}