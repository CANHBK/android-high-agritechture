package com.example.administrator.glasshouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        // Cài đặt nút back
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        txtFullName.setOnClickListener {
            switcherName.showNext()
        }
    }
}
