package com.mandevices.iot.agriculture.ui.monitor

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.BottomSheetDeleteNodeBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Monitor
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mandevices.iot.agriculture.vo.Status

class DeleteNodeBottomSheet : BottomSheetDialogFragment(), Injectable {
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetDeleteNodeBinding>()

    companion object {
        private lateinit var monitor: Monitor
        private lateinit var monitorViewModel: MonitorViewModel
        fun newInstance(monitor: Monitor, monitorViewModel: MonitorViewModel): DeleteNodeBottomSheet {
            this.monitor = monitor
            this.monitorViewModel = monitorViewModel
            return DeleteNodeBottomSheet()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetDeleteNodeBinding>(
                inflater,
                R.layout.bottom_sheet_delete_node,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${monitor.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?", Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${monitor.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?")
        }

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)
        monitorViewModel.deleteMonitor.value?.status = Status.INIT
        binding.viewModel = monitorViewModel

        binding.monitor = monitor

        binding.result = monitorViewModel.deleteMonitor
//        binding.btnDeleteMonitor.setOnClickListener {
//            monitorViewModel.deleteMonitor(monitor.tag)
//        }
    }

}