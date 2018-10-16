package com.example.administrator.glasshouse

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btnToSignup.setOnClickListener {
            sendToSignUp()
        }
    }

    private fun sendToSignUp() {
        val intent = Intent(this@SignInActivity,SignUpActivity::class.java)
        startActivity(intent)
    }
}
