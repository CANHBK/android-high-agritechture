package com.mandevices.iot.agriculture.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.BottomSheetUserBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UserBottomSheet : BottomSheetDialogFragment(), Injectable {


    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetUserBinding>()


    companion object {
        private lateinit var userViewModel: UserViewModel
        private lateinit var user:User
        private lateinit var logOut: () -> Unit
        fun newInstance(user: User, userViewModel: UserViewModel,logOut:()->Unit): UserBottomSheet {
            this.userViewModel = userViewModel
            this.logOut=logOut
            this.user=user
            return UserBottomSheet()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetUserBinding>(
                inflater,
                R.layout.bottom_sheet_user,
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


        binding.viewModel = userViewModel

        binding.user= user

        binding.logoutButton.setOnClickListener {
            logOut()

        }

    }


}