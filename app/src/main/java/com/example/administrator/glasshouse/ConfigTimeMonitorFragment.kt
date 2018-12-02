package com.example.administrator.glasshouse


import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.R.id.*
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.databinding.FragmentConfigTimeMonitorBinding
import com.example.administrator.glasshouse.model.MonitorModel
import com.example.administrator.glasshouse.model.SensorModel
import com.example.administrator.glasshouse.type.ConfigTimeNodeEnv
import com.example.administrator.glasshouse.viewmodel.SensorSettingViewModel
import kotlinx.android.synthetic.main.fragment_config_time_monitor.*
import java.text.SimpleDateFormat
import java.util.*


class ConfigTimeMonitorFragment : Fragment() {

    var type = 0
    var index = 0
    var min = 0
    var hour = 0
    lateinit var monitor: MonitorModel
    lateinit var sensors: ArrayList<SensorModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        monitor = arguments!!.getSerializable("monitor") as MonitorModel
        sensors = arguments!!.getSerializable("sensor") as ArrayList<SensorModel>

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_time_monitor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtPickTime.setOnClickListener {
            pickTime()
        }

        rgType.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                R.id.rb_period -> type = 0
                R.id.rb_fix_time -> type = 1
            }
        }

        for (item in sensors) {
            val button = RadioButton(context)
            button.text = item.name
            button.id = item.index.toInt()
            rg_choose_sensor.addView(button)
        }


        btn_submit_setting.setOnClickListener {
            progress_setting_sensor.visibility = View.VISIBLE
            index = rg_choose_sensor.checkedRadioButtonId
            configEnviNode(view, monitor.serviceTag, monitor.nodeEnv)
        }
        btn_cancel_setting.setOnClickListener {
            val serviceTagBundle = Bundle()
            serviceTagBundle.putString(config.SERVICE_TAG_BUNDLE, monitor.serviceTag);
            it.findNavController().popBackStack()
//            it.findNavController().navigate(R.id.action_configTimeMonitorFragment_to_monitorFragment, serviceTagBundle)
        }
    }

    private fun configEnviNode(view: View, serviceTag: String, monitor: String) {
        val input = ConfigTimeNodeEnv.builder().serviceTag(serviceTag).typeSetTime(type.toLong())
                .nodeEnv(monitor).index(index.toLong()).hour(hour.toLong()).minute(min.toLong()).build()
        MyApolloClient.getApolloClient().mutate(
                ConfigTimeNodeEmvMutation.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<ConfigTimeNodeEmvMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!config", e.message)
                progress_setting_sensor.visibility = View.INVISIBLE
            }

            override fun onResponse(response: Response<ConfigTimeNodeEmvMutation.Data>) {
                activity!!.runOnUiThread {
                    val errors = response.errors()
                    if (errors.isEmpty()) {
                        Snackbar.make(view, "Thành công", Snackbar.LENGTH_SHORT).show()
                        progress_setting_sensor.visibility = View.INVISIBLE
                        val serviceTagBundle = Bundle()
                        serviceTagBundle.putString(config.SERVICE_TAG_BUNDLE, serviceTag);
                        view.findNavController().popBackStack()
//                        view.findNavController().navigate(R.id.action_configTimeMonitorFragment_to_monitorFragment, serviceTagBundle)
                    } else {
                        progress_setting_sensor.visibility = View.INVISIBLE
                        Snackbar.make(view, response.errors()[0].message()!!, Snackbar.LENGTH_SHORT).show()
                    }


                }

            }

        })
    }

    private fun pickTime() {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        min = calendar.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val simpleFormat = SimpleDateFormat("HH:mm")
            calendar.set(0, 0, 0, hourOfDay, minute)
            hour = hourOfDay
            min = minute
            txtPickTime.text = simpleFormat.format(calendar.time)
//            if (type == 0) {
//                txtAlert.text = "Sensor will response each $hour hours and $min minutes"
//            } else txtAlert.text = "Sensor will response everyday at $hour:$min"
        }, hour, min, true)
        timePicker.show()
    }
}
