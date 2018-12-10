package com.example.administrator.glasshouse.ui.login

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
import com.example.administrator.glasshouse.databinding.FragmentLoginBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Const
import com.example.administrator.glasshouse.vo.Status
import io.paperdb.Paper
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

    var binding by autoCleared<FragmentLoginBinding>()

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

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

        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LoginViewModel::class.java)


        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = loginViewModel

        onLogin()

        handleResultLogin()

    }

    private fun onLogin() {
        loginViewModel.getLoginFields()?.observe(this, Observer {
            val email = it.email
            val password = it.password
            loginViewModel.login(email, password)
        })
    }

    private fun handleResultLogin() {
        loginViewModel.user.observe(viewLifecycleOwner, Observer {
            val status = it.status
            when (status) {
                Status.LOADING -> binding.loading = true
                Status.SUCCESS -> {
                    binding.loading = false
                    Paper.book().write(Const.USER_ID, it.data!!.id)
                    binding.root.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                Status.ERROR -> {
                    binding.loading = false
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
}