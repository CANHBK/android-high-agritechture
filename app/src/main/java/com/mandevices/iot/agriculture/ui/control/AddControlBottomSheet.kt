package com.mandevices.iot.agriculture.ui.control


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.BottomSheetAddControlBinding
import com.mandevices.iot.agriculture.databinding.BottomSheetAddNodeBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.ui.monitor.AddNodeBottomSheet
import com.mandevices.iot.agriculture.ui.monitor.MonitorViewModel
import com.mandevices.iot.agriculture.util.autoCleared


class AddControlBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetAddControlBinding>()

    companion object {
        private lateinit var controlViewModel: ControlViewModel
        private lateinit var serviceTag: String
        fun newInstance(serviceTag: String, controlViewModel: ControlViewModel): AddControlBottomSheet {
            this.serviceTag = serviceTag
            this.controlViewModel = controlViewModel
            return AddControlBottomSheet()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetAddControlBinding>(
                inflater,
                R.layout.bottom_sheet_add_control,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlViewModel.initAddControl()

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)
            viewModel = controlViewModel
            result = controlViewModel.addControl
        }

//        binding.setLifecycleOwner(viewLifecycleOwner)
//
//        binding.viewModel = monitorViewModel
//
//        binding.result = monitorViewModel.addMonitor

        addMonitor()

    }

    private fun addMonitor() {
        controlViewModel.getAddControlFields()?.observe(viewLifecycleOwner, Observer {
            controlViewModel.addControl(serviceTag = serviceTag, tag = it.tag!!, name = it.name!!)
        })
    }


}