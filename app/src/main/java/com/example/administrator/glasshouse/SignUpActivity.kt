package com.example.administrator.glasshouse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

//     val EMAIL_ADDRESS_PATTERN = Pattern.compile(
//             "[a-zA-Z0-9+._%-+]{1,256}" +
//             "@" +
//             "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
//             "." +
//             "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}"
//)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        validatePass()
        validatePassConfirm()
        btnSignUp.setOnClickListener {
            //sendToMain()
            val lastName = edtLastName.text.toString()
            val middleName = ""
            val firstName = edtFirstName.text.toString()
            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()
            //if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            val check = !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(middleName)
                                !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)
            if(check){
                register(lastName, middleName, firstName, email, pass)
            }
            else {
                Toast.makeText(this@SignUpActivity,"Some information is missing",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun register(lastName: String, middleName: String, firstName: String, email: String, pass: String) {
        MyApolloClient.getApolloClient().mutate(
                RegisterMutation.builder()
                        .firstName(firstName)
                        .middleName(middleName)
                        .lastName(lastName)
                        .email(email)
                        .password(pass)
                        .build())
                .enqueue(object : ApolloCall.Callback<RegisterMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.d("register", e.message)
                    }

                    override fun onResponse(response: Response<RegisterMutation.Data>) {
                        val id = response.data()!!.register()!!.id()
                        if (id != null) {
                            this@SignUpActivity.runOnUiThread {
                                val mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
                                val editor = mShared.edit()
                                editor.putString(Config.UserId, id)
                                editor.apply()
                                Toast.makeText(this@SignUpActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                sendToFarmChange()
                            }
                        } else {
                            val error = response.errors().toString()
                            this@SignUpActivity.runOnUiThread { Toast.makeText(this@SignUpActivity, error, Toast.LENGTH_LONG).show()}
                        }

                    }

                }
                )
    }


    private fun validatePass() {
        inputPassUp.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text!!.length in 1..6) {
                    //inputPassUp.error = "At least 6 chars"
                    inputPassUp.hint = "At least 6 chars"
                    inputPassUp.isErrorEnabled = true
                    btnSignUp.isEnabled = false
                } else {
                    inputPassUp.hint = "Validated Password"
                    inputPassUp.isErrorEnabled = false
                    btnSignUp.isEnabled = true
                }
            }

        })
    }

    private fun validatePassConfirm() {
        inputCfPass.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pass = edtPass.text.toString()
                val cfPass = inputCfPass.editText!!.text.toString()
                if (cfPass != pass) {
                    inputCfPass.hint = "Password doesn't match"
                    inputCfPass.error = "wrong"
                    inputCfPass.isErrorEnabled = true
                    btnSignUp.isEnabled = false
                } else {
                    inputCfPass.hint = "Ok"
                    inputCfPass.isErrorEnabled = false
                    btnSignUp.isEnabled = true
                }
            }
        })
    }

    private fun sendToFarmChange() {
        val FarmIntent = Intent(this@SignUpActivity, FarmChangeActivity::class.java)
        startActivity(FarmIntent)
    }
}
