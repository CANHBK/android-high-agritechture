package com.example.administrator.glasshouse.ui.monitor

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.BottomSheetAddNodeBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.ui.dashboard.AddGateBottomSheet
import com.example.administrator.glasshouse.util.autoCleared
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNodeBottomSheet : BottomSheetDialogFragment(),Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetAddNodeBinding>()

    companion object {
        private lateinit var monitorViewModel: MonitorViewModel
        private lateinit var serviceTag: String
        fun newInstance(serviceTag:String,monitorViewModel: MonitorViewModel): AddNodeBottomSheet {
            this.serviceTag=serviceTag
            this.monitorViewModel = monitorViewModel
            return AddNodeBottomSheet()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetAddNodeBinding>(
                inflater,
                R.layout.bottom_sheet_add_node,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = monitorViewModel

        binding.result = monitorViewModel.addMonitor

        addMonitor()

    }

    private fun addMonitor() {
        monitorViewModel.getAddMonitorFields()?.observe(viewLifecycleOwner, Observer {
            monitorViewModel.addMonitor(serviceTag = serviceTag,tag=it.tag!!, name = it.name!!)
        })
    }


}