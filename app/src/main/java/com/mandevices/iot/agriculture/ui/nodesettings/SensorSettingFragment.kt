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
import com.mandevices.iot.agriculture.ui.monitor.MonitorViewModel
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Status
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject


class SensorSettingFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monitorViewModel: MonitorViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var monitor: Monitor

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSensorSettingBinding>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        monitor = SensorSettingFragmentArgs.fromBundle(arguments).monitor
        val dataBinding = DataBindingUtil.inflate<FragmentSensorSettingBinding>(
                inflater,
                R.layout.fragment_sensor_setting,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding
        val currentTime = Calendar.getInstance()
        val mHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val mMinute = currentTime.get(Calendar.MINUTE)
        binding.selectedTimeText.setOnClickListener {


            binding.selectedTimeText.text = "${timeTextFormat(mHour)}:${timeTextFormat(mMinute)}"

            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.selectedTimeText.text = "${timeTextFormat(hourOfDay)}:${timeTextFormat(minute)}"
            }, mHour, mMinute, true).show()
        }

        monitorViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MonitorViewModel::class.java)

        return dataBinding.root

    }

    private fun timeTextFormat(timeNumber: Int): String {
        return if (timeNumber > 9) "$timeNumber"
        else "0$timeNumber"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topToolbar)

        val sensorIndex = SensorSettingFragmentArgs.fromBundle(arguments).sensorIndex

        var isPeriodic = false
        binding.profileGroup.setOnCheckedChangeListener { group, checkedId ->
            isPeriodic = when (binding.profileGroup.checkedRadioButtonId) {
                R.id.periodic_option -> true
                else -> false
            }
        }

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)
            result = monitorViewModel.configTimerMonitor

            topToolbar.setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }

        }
        binding.monitor = monitor

        binding.saveButton.setOnClickListener {
            monitorViewModel.configTimerMonitor(
                    serviceTag = monitor.serviceTag,
                    tag = monitor.tag,
                    index = sensorIndex.toString(),
                    isPeriodic = isPeriodic,
                    hour = binding.selectedTimeText.text.toString().split(":")[0],
                    minute = binding.selectedTimeText.text.toString().split(":")[1]
            )
        }
        monitorViewModel.apply {
            monitorViewModel.configTimerMonitor.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it.status == Status.SUCCESS) {
                    view.findNavController().popBackStack()
                }
            })
        }


    }


}
