package com.example.administrator.glasshouse

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.ConfigTimeNodeControlInput
import com.example.administrator.glasshouse.type.StateRelayInput
import kotlinx.android.synthetic.main.activity_setting_relay.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class SettingRelayActivity : AppCompatActivity() {

    lateinit var mShared: SharedPreferences
    lateinit var nodeControl: String
    lateinit var serviceTag: String
    lateinit var relayName: String
    var type: Int = 0
    lateinit var state: String
    var startHour: Int = 0
    var startMin: Int = 0
    var endHour: Int = 0
    var endMin: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_relay)
        setSupportActionBar(toolbarSetRelay)
        supportActionBar!!.title = "Setting Relay"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        serviceTag = mShared.getString(Config.GateId,"")!!
        nodeControl = mShared.getString(Config.RELAY_TAG, "")!!
        relayName = mShared.getString(Config.RelayName, "")!!
        type = mShared.getLong(Config.RELAY_TYPE, 0).toInt()
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
        txtTimeStart.setOnClickListener {
            pickTimeStart()
        }
        txtTimeEnd.setOnClickListener {
            pickTimeEnd()
        }

        btnSubmitSetting.setOnClickListener { it ->

            state = if (switchState.isChecked) {
                "O"
            } else "F"
            // thêm hàm configTimeRelay
            Toast.makeText(this@SettingRelayActivity, "$nodeControl $type $startHour $startMin $endHour $endMin $state", Toast.LENGTH_LONG).show()
            SubmitChange(serviceTag,nodeControl,type, startMin,startHour,state)
        }
    }

    private fun SubmitChange(serviceTag:String,nodeControl: String, type: Int, min: Int, hour: Int, state: String) {
        val input = ConfigTimeNodeControlInput.builder()
                .serviceTag(serviceTag).nodeControl(nodeControl).typeSetTime(0).state(state).index(type.toLong())
                .hour(hour.toLong()).minute(min.toLong()).build()
        MyApolloClient.getApolloClient().mutate(
                ConfigTimeNodeControlMutation.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<ConfigTimeNodeControlMutation.Data>(){
            override fun onFailure(e: ApolloException) {
                Log.d("!setState",e.message)
            }

            override fun onResponse(response: Response<ConfigTimeNodeControlMutation.Data>) {
                this@SettingRelayActivity.runOnUiThread{
                    if(response.data()!!.configTimeNodeControl() !=null){
                        Toast.makeText(this@SettingRelayActivity,"Setting Completed",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SettingRelayActivity,response.errors()[0].message(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun pickTimeStart() {
        val calendar = Calendar.getInstance()
        startHour = calendar.get(Calendar.HOUR_OF_DAY)
        startMin = calendar.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this@SettingRelayActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val simpleFormat = SimpleDateFormat("HH:mm")
            calendar.set(0, 0, 0, hourOfDay, minute)
            startHour = hourOfDay
            startMin = minute
            txtTimeStart.text = simpleFormat.format(calendar.time)
        }, startHour, startMin, true)
        timePicker.show()
    }

    private fun pickTimeEnd() {
        val calendar = Calendar.getInstance()
        endHour = calendar.get(Calendar.HOUR_OF_DAY)
        endMin = calendar.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this@SettingRelayActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val simpleFormat = SimpleDateFormat("HH:mm")
            calendar.set(0, 0, 0, hourOfDay, minute)
            endHour = hourOfDay
            endMin = minute
            txtTimeEnd.text = simpleFormat.format(calendar.time)
        }, endHour, endMin, true)
        timePicker.show()
    }
}
