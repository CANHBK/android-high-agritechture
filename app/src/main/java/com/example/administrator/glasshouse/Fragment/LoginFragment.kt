package com.example.administrator.glasshouse.Fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.LoginMutation
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.R.id.*
import com.example.administrator.glasshouse.R.string.email
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.config.config
import io.paperdb.Paper

import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoLogin(view)
        btn_signIn.setOnClickListener {
            val email = input_email.text.toString()
            val password = input_password.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                login(it, email, password)
            } else {

                Snackbar.make(it, "Xin hãy nhập đầy đủ Email và mật khẩu", Snackbar.LENGTH_LONG).show()
            }


        }

        txt_create_account.setOnClickListener {
            it.findNavController().navigate(R.id.to_create_account)
        }
    }

    private fun autoLogin(view: View) {
        try {
            val email: String = Paper.book().read(config.EMAIL_KEY)
            val password: String = Paper.book().read(config.PASSWORD_ID_KEY)

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) return
            login(view, email, password)
        } catch (e: Exception) {

        }


    }

    private fun login(view: View, email: String, password: String) {
        MyApolloClient.getApolloClient().mutate(
                LoginMutation.builder()
                        .email(email)
                        .password(password)
                        .build())
                .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        activity!!.runOnUiThread {

                            Snackbar.make(view, e.message!!, Snackbar.LENGTH_LONG).show()
                        }
                    }

                    override fun onResponse(response: Response<LoginMutation.Data>) {

                        val user = response.data()!!.login()
                        if (user != null) {
                            activity!!.runOnUiThread {
                                Paper.book().write(config.USER_ID_KEY, user.id())
                                Paper.book().write(config.EMAIL_KEY, email)
                                Paper.book().write(config.PASSWORD_ID_KEY, password)
                                view.findNavController().navigate(R.id.to_home)
                            }
                        } else {
                            val error = response.errors()[0].message()
                            activity!!.runOnUiThread {
                                Snackbar.make(view, error!!, Snackbar.LENGTH_LONG).show()

                            }
                        }
                    }
                })
    }
}