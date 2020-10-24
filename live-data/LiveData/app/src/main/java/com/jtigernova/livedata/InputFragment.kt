package com.jtigernova.livedata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.jtigernova.livedata.model.GeneralLiveModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : Fragment() {
    private val viewModel: GeneralLiveModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_input, container, false)

        val btn = v.findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            viewModel.setUserName(name = UUID.randomUUID().toString().substring(0, 8))
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment UserHistoryFragment.
         */
        @JvmStatic
        fun newInstance() =
            InputFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}