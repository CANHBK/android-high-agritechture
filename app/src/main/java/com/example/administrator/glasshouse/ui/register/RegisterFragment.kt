package com.example.administrator.glasshouse.ui.register

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.FragmentRegisterBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Status
import javax.inject.Inject

class RegisterFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var registerViewModel: RegisterViewModel

    var binding by autoCleared<FragmentRegisterBinding>()

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentRegisterBinding>(
                inflater,
                R.layout.fragment_register,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RegisterViewModel::class.java)

        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = registerViewModel

        binding.btnSignUp.setOnClickListener {
            register()
        }

        binding.btnLogin.setOnClickListener {
            it.findNavController().popBackStack()
        }

        registerViewModel.user.observe(viewLifecycleOwner, Observer {
            val status = it.status
            when (status) {
                Status.LOADING -> binding.loading = true
                Status.SUCCESS -> {
                    binding.loading = false
                    binding.root.findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
                Status.ERROR -> {
                    binding.loading = false
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun register() {
        val email = binding.edtEmail.text.toString()
        val name = binding.edtFullName.text.toString()
        val password = binding.edtPassword.text.toString()
        val rePassword = binding.edtRePassword.text.toString()
        registerViewModel.register(email, name, password)
    }
}