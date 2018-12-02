package com.example.administrator.glasshouse


import android.os.Bundle

import android.app.TimePickerDialog
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
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.model.ControlModel
import com.example.administrator.glasshouse.model.RelayModel
import com.example.administrator.glasshouse.type.ConfigTimeNodeControlInput
import kotlinx.android.synthetic.main.fragment_config_time_control.*
import java.text.SimpleDateFormat
import java.util.*


class ConfigTimeControlFragment : Fragment() {
    lateinit var control: ControlModel
    lateinit var relays: ArrayList<RelayModel>
    var type: Int = 0
    var index = 0
    lateinit var state: String
    var hour: Int = 0
    var min: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        control = arguments!!.getSerializable("control") as ControlModel
        relays = arguments!!.getSerializable("relays") as ArrayList<RelayModel>

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_time_control, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_pick_time_relay.setOnClickListener {
            pickTime()
        }

        rg_type_set_time.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                R.id.rb_period -> type = 0
                R.id.rb_fix_time -> type = 1
            }
        }

        for (item in relays) {
            val button = RadioButton(context)
            button.text = item.name
            button.id = item.index.toInt()
            rg_choose_relay.addView(button)
        }





        btn_submit_setting_relay.setOnClickListener {

            index = rg_choose_relay.checkedRadioButtonId
            state = if (sw_state_relay.isChecked) {
                "O"
            } else "F"
            SubmitChange(control.serviceTag, control.nodeControl, index, min, hour, state, view)
        }
        btn_cancel_setting_relay.setOnClickListener {
            val serviceTagBundle = Bundle()
            serviceTagBundle.putString(config.SERVICE_TAG_BUNDLE, control.serviceTag);
            it.findNavController().popBackStack()
//            it.findNavController().navigate(R.id.action_configTimeControlFragment_to_controlFragment, serviceTagBundle)
        }
    }

    private fun SubmitChange(serviceTag: String, nodeControl: String, index: Int, min: Int, hour: Int, state: String, view: View) {
        progress_setting_relay.visibility = View.VISIBLE
        val input = ConfigTimeNodeControlInput.builder()
                .serviceTag(serviceTag).nodeControl(nodeControl).typeSetTime(0).state(state).index(index.toLong())
                .hour(hour.toLong()).minute(min.toLong()).build()
        MyApolloClient.getApolloClient().mutate(
                ConfigTimeNodeControlMutation.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<ConfigTimeNodeControlMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!setState", e.message)
                progress_setting_relay.visibility = View.GONE
            }

            override fun onResponse(response: Response<ConfigTimeNodeControlMutation.Data>) {
                activity!!.runOnUiThread {
                    val errors = response.errors()
                    if (errors.isEmpty()) {
                        progress_setting_relay.visibility = View.GONE
                        Snackbar.make(view, "Thành công", Snackbar.LENGTH_SHORT).show()
                        view.findNavController().popBackStack()
//                        view.findNavController().navigate(R.id.action_configTimeControlFragment_to_controlFragment)
                    } else {
                        progress_setting_relay.visibility = View.GONE
                        Snackbar.make(view, response.errors()[0].message()!!, Snackbar.LENGTH_LONG).show()
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
            txt_pick_time_relay.text = simpleFormat.format(calendar.time)
//            if (type == 0) {
//                txtAlert.text = "Sensor will response each $hour hours and $min minutes"
//            } else txtAlert.text = "Sensor will response everyday at $hour:$min"
        }, hour, min, true)
        timePicker.show()
    }

}
