package com.example.administrator.glasshouse.ui.monitor

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.BottomSheetDeleteNodeBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Monitor
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
        binding.viewModel = monitorViewModel
        binding.result = monitorViewModel.deleteMonitor
//        binding.btnDelete.setOnClickListener {
//            monitorViewModel.deleteMonitor(monitor.tag)
//        }
    }

}