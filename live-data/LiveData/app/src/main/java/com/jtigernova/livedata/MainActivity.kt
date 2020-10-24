package com.jtigernova.livedata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.twoFrags).setOnClickListener {
            startActivity(Intent(this, FullscreenActivity::class.java))
        }

        findViewById<Button>(R.id.grid).setOnClickListener {
            startActivity(Intent(this, GridActivity::class.java))
        }
    }
}