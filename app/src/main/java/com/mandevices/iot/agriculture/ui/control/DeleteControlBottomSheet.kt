package com.mandevices.iot.agriculture.ui.control


import android.os.Build
import android.os.Bundle
import android.text.Html

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.R.id.monitor
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.BottomSheetDeleteControlBinding
import com.mandevices.iot.agriculture.databinding.BottomSheetDeleteNodeBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.ui.monitor.DeleteNodeBottomSheet
import com.mandevices.iot.agriculture.ui.monitor.MonitorViewModel
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Monitor

class DeleteControlBottomSheet : BottomSheetDialogFragment(), Injectable {
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetDeleteControlBinding>()

    companion object {
        private lateinit var control: Control
        private lateinit var controlViewModel: ControlViewModel
        fun newInstance(control: Control, controlViewModel: ControlViewModel): DeleteControlBottomSheet {
            this.control = control
            this.controlViewModel = controlViewModel
            return DeleteControlBottomSheet()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetDeleteControlBinding>(
                inflater,
                R.layout.bottom_sheet_delete_control,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${control.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?", Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${control.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?")
        }

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = controlViewModel

        binding.control = control

        binding.result = controlViewModel.deleteControl

    }

}
