package com.example.administrator.glasshouse

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.ConfigTimeNodeEnv
import kotlinx.android.synthetic.main.activity_setting_sensor.*
import java.text.SimpleDateFormat
import java.util.*

class SettingSensorActivity : AppCompatActivity() {

    var type = 0
    var index = 0
    var min = 0
    var hour = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_sensor)
        setSupportActionBar(tbSetSensor)
        supportActionBar!!.title = "Setting node sensor"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mSharedPreferences = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val sensorName = mSharedPreferences.getString(Config.SSName, "")
        val sensorID = mSharedPreferences.getString(Config.SENSOR_TAG, "")!!
        val serviceTag = mSharedPreferences.getString(Config.GateId, "")!!
        edtSSNode.setText(sensorName)
        txtSensorHeading.setText("Setting $sensorName")

        txtPickTime.setOnClickListener {
            pickTime()
        }

        rgType.setOnCheckedChangeListener{ _,isChecked->
            when(isChecked){
                R.id.rbPeriod -> type = 0
                R.id.rbPemanent -> type = 1
            }
        }
        rgPara1.setOnCheckedChangeListener{ _,isChecked ->
            when(isChecked){
                R.id.rbTemp -> index = 1
                R.id.rbLight -> index = 2
            }

        }
        rgPara2.setOnCheckedChangeListener{ _,isChecked ->
            when(isChecked){
                R.id.rbAir -> index = 3
                R.id.rbGround -> index = 4
            }

        }
        btnOk.setOnClickListener {
            val enviNodeName = edtSSNode.text.toString()


            configEnviNode(serviceTag, sensorID)

        }
    }

    private fun configEnviNode(serviceTag: String, sensorID: String) {
        val input = ConfigTimeNodeEnv.builder().serviceTag(serviceTag).typeSetTime(type.toLong())
                .nodeEnv(sensorID).index(index.toLong()).hour(hour.toLong()).minute(min.toLong()).build()
        MyApolloClient.getApolloClient().mutate(
                ConfigTimeNodeEmvMutation.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<ConfigTimeNodeEmvMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!config", e.message)
            }

            override fun onResponse(response: Response<ConfigTimeNodeEmvMutation.Data>) {
                this@SettingSensorActivity.runOnUiThread {
                    if (response.data()!!.configTimeNodeEnv() !=null) {
                        Toast.makeText(this@SettingSensorActivity, "Setting Completed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SettingSensorActivity, response.errors()[0].message(), Toast.LENGTH_SHORT).show()
                    }
                }

            }

        })
    }

    private fun pickTime() {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        min = calendar.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this@SettingSensorActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val simpleFormat = SimpleDateFormat("HH:mm")
            calendar.set(0, 0, 0, hourOfDay, minute)
            hour = hourOfDay
            min = minute
            txtPickTime.text = simpleFormat.format(calendar.time)
            if (type == 0) {
                txtAlert.text = "Sensor will response each $hour hours and $min minutes"
            } else txtAlert.text = "Sensor will response everyday at $hour:$min"
        }, hour, min, true)
        timePicker.show()
    }
}
