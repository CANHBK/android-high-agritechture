package com.mandevices.iot.agriculture.ui.control


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.R.id.monitor
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.BottomSheetEditControlBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Control

class EditControlBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetEditControlBinding>()


    companion object {
        private lateinit var control: Control
        private lateinit var controlViewModel: ControlViewModel
        fun newInstance(control: Control, controlViewModel: ControlViewModel): EditControlBottomSheet {
            this.control = control
            this.controlViewModel = controlViewModel
            return EditControlBottomSheet()
        }
    }

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetEditControlBinding>(
                inflater,
                R.layout.bottom_sheet_edit_control,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlViewModel.initEditControl()

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)
            viewModel = controlViewModel
            result = controlViewModel.editControl
        }


        binding.control= control

        controlViewModel.getEditControlForm().fields.name= control.name
        editControl()

    }

    private fun editControl() {
        controlViewModel.getEditControlFields()?.observe(viewLifecycleOwner, Observer {
            controlViewModel.editControl(control.tag, it.name!!)
        })
    }

}
