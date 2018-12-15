package com.example.administrator.glasshouse.ui.control


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.databinding.FragmentRelayControlBinding


class RelayControlFragment : Fragment() {

    private lateinit var binding: FragmentRelayControlBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_relay_control, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }


}
