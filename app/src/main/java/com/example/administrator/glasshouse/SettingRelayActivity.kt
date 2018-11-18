package com.example.administrator.glasshouse

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.administrator.glasshouse.Utils.Config
import kotlinx.android.synthetic.main.activity_setting_relay.*
import java.text.SimpleDateFormat
import java.util.*

class SettingRelayActivity : AppCompatActivity() {

    lateinit var mShared: SharedPreferences
    var relayTag: Int = 0
    lateinit var relayName: String
    var type: Int = 0
    lateinit var state: String
    var hour: Int = 0
    var min: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_relay)
        setSupportActionBar(toolbarSetRelay)
        supportActionBar!!.title = "Setting Relay"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        relayTag = mShared.getInt(Config.RELAY_TAG, 0)
        relayName = mShared.getString(Config.RelayName, "")!!
        type = mShared.getInt(Config.RELAY_TYPE, 0)
        state = mShared.getString(Config.RELAY_STATE, "O")!!

        // Cài đặt cho hình
        if (state == "O") {
            when (type) {
                1 -> {
                    imgRelaySet.setBackgroundResource(R.drawable.custom_image_button_on)
                    imgRelaySet.setImageResource(R.drawable.ic_fan)
                }
                2 -> {
                    imgRelaySet.setBackgroundResource(R.drawable.custom_image_button_on)
                    imgRelaySet.setImageResource(R.drawable.ic_light_bulb)
                }
            }

        } else {
            when (type) {
                1 -> {
                    imgRelaySet.setBackgroundResource(R.drawable.custom_image_button_off)
                    imgRelaySet.setImageResource(R.drawable.ic_fan_off)
                }
                2 -> {
                    imgRelaySet.setBackgroundResource(R.drawable.custom_image_button_off)
                    imgRelaySet.setImageResource(R.drawable.ic_light_bulb_off)
                }
            }
        }

        //Cài đặt tên
        txtRelaySet.text = relayName
        txtTypeSet.text = "Type $type"
        edtRelaySet.setText(relayName)
        //Show TimePicker Dialog
        txtShowTime.setOnClickListener {
            showTimePicker()
        }

        btnSubmitSetting.setOnClickListener { it ->

            state = if (switchState.isChecked) {
                "O"
            } else "F"
            // thêm hàm configTimeRelay
            Toast.makeText(this@SettingRelayActivity, "$relayTag $type $min $hour $state", Toast.LENGTH_LONG).show()
            //SubmitChange(relayTag,type,min,hour,state)
        }
    }

    private fun SubmitChange(relayTag: Int, type: Int, min: Int, hour: Int, state: String) {

    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        min = calendar.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this@SettingRelayActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val simpleFormat = SimpleDateFormat("HH:mm")
            calendar.set(0, 0, 0, hourOfDay, minute)
            hour = hourOfDay
            min = minute
            txtShowTime.text = simpleFormat.format(calendar.time)
        }, hour, min, true)
        Log.d("!time","$hour $min")
        timePicker.show()
    }
}
