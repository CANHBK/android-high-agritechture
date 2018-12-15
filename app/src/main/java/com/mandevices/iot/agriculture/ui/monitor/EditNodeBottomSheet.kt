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
import com.mandevices.iot.agriculture.ui.control.MonitorViewModel

class EditNodeBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetEditNodeBinding>()


    companion object {
        private lateinit var monitor: Monitor
        private lateinit var monitorViewModel: MonitorViewModel
        fun newInstance(monitor: Monitor, monitorViewModel: MonitorViewModel): EditNodeBottomSheet {
            this.monitor = monitor
            this.monitorViewModel = monitorViewModel
            return EditNodeBottomSheet()
        }
    }

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetEditNodeBinding>(
                inflater,
                R.layout.bottom_sheet_edit_node,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monitorViewModel.initEditMonitor()

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)
            viewModel = monitorViewModel
            result = monitorViewModel.editMonitor
        }

        //TODO: Khi đưa vào apply thì không hoạt động
        binding.monitor= monitor

        editGate()

    }

    private fun editGate() {
        monitorViewModel.getEditMonitorFields()?.observe(viewLifecycleOwner, Observer {
            monitorViewModel.editMonitor(monitor.tag, it.name!!)
        })
    }

}