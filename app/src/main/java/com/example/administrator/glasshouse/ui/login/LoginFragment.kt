package com.example.administrator.glasshouse.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.FragmentLoginBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Status
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

     var binding by autoCleared<FragmentLoginBinding>()

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @SuppressLint("VisibleForTests")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentLoginBinding>(
                inflater,
                R.layout.fragment_login,
                container,
                false,
                dataBindingComponent
        )


        binding = dataBinding
        return dataBinding.root
    }

    @SuppressLint("VisibleForTests")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LoginViewModel::class.java)


        binding.setLifecycleOwner(viewLifecycleOwner)


        binding.btnLogin.setOnClickListener {
                        login()
        }



        binding.btnSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.to_create_account)
        }

        loginViewModel.user.observe(viewLifecycleOwner, Observer {
            val status = it.status
            when (status) {
                Status.LOADING -> binding.loading = true
                Status.SUCCESS -> {
                    binding.loading = false
                    binding.root.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                Status.ERROR -> {
                    binding.loading = false
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }


    private fun login() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loginViewModel.login(email, password)
        } else {

            Snackbar.make(binding.root, "Xin hãy nhập đầy đủ Email và mật khẩu", Snackbar.LENGTH_LONG).show()
        }


    }
}