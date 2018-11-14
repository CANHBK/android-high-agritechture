package com.example.administrator.glasshouse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_sign_in.*
import okhttp3.internal.Util
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btnSignIn.setOnClickListener {
            //sendToSignUp()
            val email = edtEmailIn.text.toString()
            val password = edtPassIn.text.toString()
            probarIn.visibility = View.VISIBLE

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                login(email, password)
            } else {
                probarIn.visibility = View.INVISIBLE
                Toast.makeText(this@SignInActivity,"Xin hãy nhập đầy đủ Email và mật khẩu",Toast.LENGTH_LONG).show()
            }

        }
        btnToSignup.setOnClickListener {
            sendToSignUp()
        }

        //getUsers()
    }

    private fun getUsers() {
        MyApolloClient.getApolloClient().query(
                GetAllUsersQuery.builder().build()
        ).enqueue(object : ApolloCall.Callback<GetAllUsersQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!user", e.message)
            }

            override fun onResponse(response: Response<GetAllUsersQuery.Data>) {
                Log.d("!user", response.data()!!.users()!![0].id())
            }
        })
    }

    private fun sendToSignUp() {
        val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun sendToFarmChange() {
        val intent = Intent(this@SignInActivity, FarmChangeActivity::class.java)
        startActivity(intent)
    }

    private fun login(email: String, password: String) {
        MyApolloClient.getApolloClient().mutate(
                LoginMutation.builder()
                        .email(email)
                        .password(password)
                        .build())
                .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.d("!login", e.message)
                        this@SignInActivity.runOnUiThread {
                            probarIn.visibility = View.INVISIBLE
                            Toast.makeText(this@SignInActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onResponse(response: Response<LoginMutation.Data>) {

                        // Check cách để có thể lấy được response.data()!!
                        val checkResponse = response.data()!!.login()
//                        Log.d("!login", id.toString())
                        if (checkResponse != null) {
                            this@SignInActivity.runOnUiThread {
                                val id = checkResponse.id()
                                Toast.makeText(this@SignInActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                // Thực hiện lưu ID người dùng bằng SharedPreference ngay sau khi đăng nhập
                                val mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
                                val editor = mShared.edit()
                                editor.putString(Config.UserId, id)
                                editor.apply()
                                probarIn.visibility = View.INVISIBLE
                                sendToFarmChange()
                            }
                        } else {
                            val error = response.errors()[0].message()
                            this@SignInActivity.runOnUiThread {
                                Toast.makeText(this@SignInActivity, error, Toast.LENGTH_LONG).show()
                                probarIn.visibility = View.INVISIBLE
                            }
                        }
                    }
                })
    }
}



