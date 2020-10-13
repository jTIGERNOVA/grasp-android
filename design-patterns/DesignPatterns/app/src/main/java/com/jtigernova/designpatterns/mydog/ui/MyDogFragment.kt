package com.jtigernova.designpatterns.mydog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.jtigernova.designpatterns.R

class MyDogFragment : Fragment() {

    companion object {
        fun newInstance() = MyDogFragment()
    }

    //    private val viewModel: MyDogViewModel by viewModels(factoryProducer = {
//        SavedStateViewModelFactory(requireActivity().application, this)
//    })
    private val viewModel: MyDogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        arguments?.apply {
            putInt("id", 3)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_dog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dog.observe(viewLifecycleOwner) {
            it.url?.let { url ->
                Glide.with(this).load(url).placeholder(R.color.colorPrimaryDark)
                    .into(view.findViewById(R.id.dog))
            }
        }
    }
}