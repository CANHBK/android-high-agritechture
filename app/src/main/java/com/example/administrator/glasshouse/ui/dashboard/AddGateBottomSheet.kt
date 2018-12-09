package com.example.administrator.glasshouse.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.BottomSheetAddGateBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class AddGateBottomSheet : BottomSheetDialogFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dashBoardViewModel: DashBoardViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetAddGateBinding>()

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetAddGateBinding>(
                inflater,
                R.layout.bottom_sheet_add_gate,
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

        dashBoardViewModel = ViewModelProviders.of(
                this, viewModelFactory)
                .get(DashBoardViewModel::class.java)

        binding.viewModel = dashBoardViewModel
    }

}