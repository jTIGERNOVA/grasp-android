package com.jtigernova.livedata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.twoFrags).setOnClickListener {
            startActivity(Intent(this, TwoFragmentsActivity::class.java))
        }

        findViewById<Button>(R.id.grid).setOnClickListener {
            startActivity(Intent(this, GridActivity::class.java))
        }

        dataBind.setOnClickListener {
            startActivity(Intent(this, DataBindActivity::class.java))
        }
    }
}