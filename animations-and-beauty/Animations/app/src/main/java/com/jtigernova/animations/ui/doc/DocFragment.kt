package com.jtigernova.animations.ui.doc

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

class DocFragment : Fragment() {

    private lateinit var loadingAnimation: AnimationDrawable
    private lateinit var docViewModel: DocViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        docViewModel =
            ViewModelProvider(this).get(DocViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_doc, container, false)

        docViewModel.text.observe(viewLifecycleOwner, Observer {
            Snackbar.make(root, it, Snackbar.LENGTH_SHORT).show()
        })

        root.findViewById<ImageView>(R.id.doc).apply {
            setImageResource(R.drawable.doc)
            loadingAnimation = drawable as AnimationDrawable
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        loadingAnimation.start()
    }
}