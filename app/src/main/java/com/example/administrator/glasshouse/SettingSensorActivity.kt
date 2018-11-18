package com.example.administrator.glasshouse

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.administrator.glasshouse.Utils.Config

import kotlinx.android.synthetic.main.activity_setting_sensor.*

class SettingSensorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_sensor)
        setSupportActionBar(tbSetSensor)
        supportActionBar!!.title = "Setting node sensor"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mSharedPreferences = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val sensorName = mSharedPreferences.getString(Config.SSName,"")
        edtSSNode.setText(sensorName)
        txtSensorHeading.setText("Setting $sensorName")
    }
}
