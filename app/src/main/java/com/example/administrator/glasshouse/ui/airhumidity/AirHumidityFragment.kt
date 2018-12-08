package com.example.administrator.glasshouse.ui.airhumidity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

import com.example.administrator.glasshouse.R
import kotlinx.android.synthetic.main.fragment_air_humidity.*


class AirHumidityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_air_humidity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_from_air_humidity.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

}
