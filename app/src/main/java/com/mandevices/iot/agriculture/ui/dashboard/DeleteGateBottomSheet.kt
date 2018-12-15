package com.mandevices.iot.agriculture.ui.dashboard

import android.app.Dialog
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
import com.mandevices.iot.agriculture.databinding.BottomSheetDeleteGateBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Gate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteGateBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetDeleteGateBinding>()


    companion object {
        private lateinit var gate: Gate
        private lateinit var dashBoardViewModel: DashBoardViewModel
        fun newInstance(gate: Gate, dashBoardViewModel: DashBoardViewModel): DeleteGateBottomSheet {
            this.gate = gate
            this.dashBoardViewModel = dashBoardViewModel
            return DeleteGateBottomSheet()

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetDeleteGateBinding>(
                inflater,
                R.layout.bottom_sheet_delete_gate,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${gate.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?", Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${gate.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?")
        }

        return dataBinding.root
    }

    override fun onViewCreated(
            view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = dashBoardViewModel

        binding.gate = gate

        binding.result = dashBoardViewModel.removeGate

    }

}