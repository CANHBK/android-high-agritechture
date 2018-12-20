package com.mandevices.iot.agriculture.ui.dashboard

import android.annotation.SuppressLint
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
import com.mandevices.iot.agriculture.databinding.BottomSheetEditGateBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Gate
import com.mandevices.iot.agriculture.vo.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EditGateBottomSheet : BottomSheetDialogFragment(), Injectable {
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetEditGateBinding>()


    companion object {
        private lateinit var gate: Gate
        private lateinit var dashBoardViewModel: DashBoardViewModel
        fun newInstance(gate: Gate, dashBoardViewModel: DashBoardViewModel): EditGateBottomSheet {
            this.gate = gate
            this.dashBoardViewModel = dashBoardViewModel
            return EditGateBottomSheet()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    @SuppressLint("VisibleForTests")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetEditGateBinding>(
                inflater,
                R.layout.bottom_sheet_edit_gate,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        binding.gate = gate

        binding.viewModel = dashBoardViewModel

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        dashBoardViewModel.initEditGate()

        dashBoardViewModel.getEditGateForm()?.fields?.name= gate.name

        editGate()

        handleResultEditGate()

    }

    private fun handleResultEditGate() {
        dashBoardViewModel.editGate.observe(viewLifecycleOwner, Observer {
            val status = it.status
            when (status) {
                Status.LOADING -> {
                    binding.loading = true
                    binding.hasError = false
                }
                Status.SUCCESS -> {
                    binding.loading = false
                    binding.hasError = false

                }
                Status.ERROR -> {
                    binding.hasError = true
                    binding.loading = false
                    binding.errorMessage = it.message!!
                }
            }
        })
    }

    private fun editGate() {
        dashBoardViewModel.getEditGateFields()?.observe(viewLifecycleOwner, Observer {
            dashBoardViewModel.editGate(gate.serviceTag, it.name!!)
        })
    }

}