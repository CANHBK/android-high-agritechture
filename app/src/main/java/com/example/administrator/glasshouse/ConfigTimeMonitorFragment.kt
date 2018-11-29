package com.example.administrator.glasshouse


import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.glasshouse.databinding.FragmentConfigTimeMonitorBinding


class ConfigTimeMonitorFragment : android.support.v4.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val sensorSettingBinding = DataBindingUtil.inflate<FragmentConfigTimeMonitorBinding>(inflater, R.layout.fragment_config_time_monitor, container, false)
        sensorSettingBinding.setLifecycleOwner(this)
        sensorSettingBinding.viewmodel = activity.run { ViewModelProvider.of(this) }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_time_monitor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}
