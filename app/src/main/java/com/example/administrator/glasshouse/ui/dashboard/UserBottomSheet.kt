package com.example.administrator.glasshouse.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.BottomSheetEditGateBinding
import com.example.administrator.glasshouse.databinding.BottomSheetUserBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Status
import com.example.administrator.glasshouse.vo.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject


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
//            it.findNavController().navigate(R.id.log_out)
            logOut()

        }

    }


}