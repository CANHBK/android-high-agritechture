package com.example.administrator.glasshouse.Fragment

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.RegisterMutation
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.type.UserInput
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_already_account.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        btn_signUp.setOnClickListener {
            val email = input_email.text.toString()
            val name = input_name.text.toString()
            val password = input_password.text.toString()
            register(it, name, email, password)

        }
    }

    private fun register(view: View, name: String, email: String, password: String) {
        val input = UserInput.builder().name(name)
                .email(email)
                .password(password).build()
        MyApolloClient.getApolloClient().mutate(
                RegisterMutation.builder().params(input)
                        .build())
                .enqueue(object : ApolloCall.Callback<RegisterMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        activity!!.runOnUiThread { Snackbar.make(view, e.message!!, Snackbar.LENGTH_LONG).show() }
                    }

                    override fun onResponse(response: Response<RegisterMutation.Data>) {
                        val errors = response.errors()
                        if (errors.isEmpty()) {
                            val user = response.data()!!.register()
                            activity!!.runOnUiThread {
                                Paper.book().write(config.USER_ID_KEY, user!!.id())
                                view.findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                            }
                        } else {
                            val error = response.errors()[0].message()
                            activity!!.runOnUiThread { Snackbar.make(view, error!!, Snackbar.LENGTH_LONG).show() }
                        }

                    }

                }
                )
    }
}