package com.example.administrator.glasshouse

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnSignUp.setOnClickListener {
            sendToMain()
        }
    }

    private fun sendToMain() {
        val mainIntent = Intent(this@SignUpActivity,MainActivity::class.java)
        startActivity(mainIntent)
    }
}
