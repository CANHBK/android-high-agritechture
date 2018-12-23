package com.mandevices.iot.agriculture.ui.monitor

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.BottomSheetEditNodeBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Monitor
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mandevices.iot.agriculture.databinding.BottomSheetSetTimeSensorBinding
import com.mandevices.iot.agriculture.vo.Sensor

class SetTimeBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetSetTimeSensorBinding>()


    companion object {
        private lateinit var sensor: Sensor
        private lateinit var monitorViewModel: MonitorViewModel
        fun newInstance(sensor: Sensor, monitorViewModel: MonitorViewModel): SetTimeBottomSheet {
            this.sensor = sensor
            this.monitorViewModel = monitorViewModel
            return SetTimeBottomSheet()
        }
    }

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetSetTimeSensorBinding>(
                inflater,
                R.layout.bottom_sheet_set_time_sensor,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monitorViewModel.initSetTime()
        monitorViewModel.getSetTimeForm().fields.time = sensor.minute
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel= monitorViewModel
        binding.result= monitorViewModel.configTimerMonitor
//        binding.apply {
//            setLifecycleOwner(viewLifecycleOwner)
//            viewModel = monitorViewModel
//            result = monitorViewModel.configTimerMonitor
//        }

        //TODO: Khi đưa vào apply thì không hoạt động
        binding.sensor = sensor

        setTime()

    }

    private fun setTime() {
        monitorViewModel.getSetTimeFields()?.observe(viewLifecycleOwner, Observer {
            monitorViewModel.configTimerMonitor(
                    serviceTag = sensor.serviceTag,
                    tag = sensor.tag,
                    index = sensor.index.toString(),
                    isPeriodic = true,
                    hour = "0",
                    minute = it.time!!
            )
        })
    }

}