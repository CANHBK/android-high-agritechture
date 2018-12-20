package com.mandevices.iot.agriculture.ui.nodesettings


import android.app.TimePickerDialog
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

import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.FragmentSensorSettingBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.autoCleared
import java.util.*
import javax.inject.Inject
import kotlin.math.min


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

        binding.selectedTimeText.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val mHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val mMinute = currentTime.get(Calendar.MINUTE)

            binding.selectedTimeText.text = "$mHour:$mMinute"

            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.selectedTimeText.text = "$hourOfDay:$minute"
            }, mHour, mMinute, true).show()
        }

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
