package com.jtigernova.livedata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jtigernova.livedata.model.GeneralLiveModel

/**
 * A simple [Fragment] subclass.
 * Use the [UserHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserHistoryFragment : Fragment() {
    private val viewModel: GeneralLiveModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_user_history, container, false)

        val name = v.findViewById<TextView>(R.id.name)

        viewModel.userData.observe(viewLifecycleOwner, {
            name.text = it.user.name
        })

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
        fun newInstance(param1: String, param2: String) =
            UserHistoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}