package com.example.administrator.glasshouse.ui.nodesettings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.FragmentMonitorBinding
import com.example.administrator.glasshouse.databinding.FragmentSensorSettingBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.ui.monitor.MonitorViewModel
import com.example.administrator.glasshouse.util.AppExecutors
import com.example.administrator.glasshouse.util.autoCleared
import javax.inject.Inject


class SensorSettingFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var sensorSettingViewModel: SensorSettingViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSensorSettingBinding>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentSensorSettingBinding>(
                inflater,
                R.layout.fragment_sensor_setting,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        sensorSettingViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SensorSettingViewModel::class.java)

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topToolbar)

        binding.topToolbar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }
    }


}
