package com.mandevices.iot.agriculture.ui.chart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.FragmentChartBinding
import com.mandevices.iot.agriculture.databinding.FragmentControlBinding
import com.mandevices.iot.agriculture.db.RelayDao
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.ui.control.*
import com.mandevices.iot.agriculture.ui.monitor.MonitorViewModel
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Status
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception
import javax.inject.Inject

class FragmentChart : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var monitorViewModel: MonitorViewModel

    private lateinit var sensorData: String

    private lateinit var monitorTag: String

    @Inject
    lateinit var client: MqttAndroidClient

    @Inject
    lateinit var mqttConnectOptions: MqttConnectOptions


    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentChartBinding>()

    private var adapter by autoCleared<ControlAdapter>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        monitorTag = FragmentChartArgs.fromBundle(arguments).monitorTag

        val dataBinding = DataBindingUtil.inflate<FragmentChartBinding>(
                inflater,
                R.layout.fragment_chart,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        monitorViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MonitorViewModel::class.java)

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)


        setupMqtt()

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)

            topToolbar.setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }

            topToolbar.inflateMenu(R.menu.menu_dashboard)
        }






        monitorViewModel.apply {

            getMonitorDataByDate(tag = monitorTag)

            monitorDataByDate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    sensorData = it.data!!.content
                }
            })

        }

    }

    private fun setupMqtt() {
        try {
            if (!client.isConnected) {
                val conToken = client.connect(mqttConnectOptions)
                conToken.actionCallback = object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        myMqttSubscribe("RELAY")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Toast.makeText(context, "CONNECTION FAILED", Toast.LENGTH_SHORT).show()
                    }
                }
            } else myMqttSubscribe("RELAY")

            client.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Toast.makeText(context, "$topic - ${message.toString()}", Toast.LENGTH_SHORT).show()
//                    controlViewModel.loadControls(serviceTag)
                }

                override fun connectionLost(cause: Throwable?) {
                    setupMqtt()
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }

            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun myMqttSubscribe(topic: String) {
        val subToken = client.subscribe(topic, 2)
        subToken.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Success: $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "Failed: $topic")
            }

        }
    }

}