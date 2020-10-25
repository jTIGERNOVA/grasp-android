package com.jtigernova.animations.ui.bar

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.jtigernova.animations.R

class BarFragment : Fragment() {

    private lateinit var loadingAnimation: AnimationDrawable
    private lateinit var barViewModel: BarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barViewModel =
            ViewModelProvider(this).get(BarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bar, container, false)

        barViewModel.text.observe(viewLifecycleOwner, Observer {
            Snackbar.make(root, it, Snackbar.LENGTH_SHORT).show()
        })

        root.findViewById<ImageView>(R.id.loadingImg).apply {
            setImageResource(R.drawable.loading)
            loadingAnimation = drawable as AnimationDrawable
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        loadingAnimation.start()
    }
}