package com.mandevices.iot.agriculture.ui.monitor

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
import com.mandevices.iot.agriculture.databinding.FragmentMonitorBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.FarmApp
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Status
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception
import javax.inject.Inject

class MonitorFragment : Fragment(), Injectable {
    private var addBottomSheet: AddNodeBottomSheet? = null
    private var deleteBottomSheet: DeleteNodeBottomSheet? = null
    private var editBottomSheet: EditNodeBottomSheet? = null

    @Inject
    lateinit var client: MqttAndroidClient

    @Inject
    lateinit var mqttConnectOptions: MqttConnectOptions

    private lateinit var serviceTag: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monitorViewModel: MonitorViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentMonitorBinding>()

    private var adapter by autoCleared<MonitorAdapter>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        serviceTag = MonitorFragmentArgs.fromBundle(arguments).serviceTag

        val dataBinding = DataBindingUtil.inflate<FragmentMonitorBinding>(
                inflater,
                R.layout.fragment_monitor,
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
        (activity as AppCompatActivity).setSupportActionBar(binding.topToolbar)

        setupMqtt()

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)

            result = monitorViewModel.monitors

            fabAdd.setOnClickListener {
                addBottomSheet?.show(activity!!.supportFragmentManager, addBottomSheet?.tag)
            }

            topToolbar.setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }
        }

        MonitorAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                onDeleteClick = {
                    deleteBottomSheet = DeleteNodeBottomSheet.newInstance(monitor = it, monitorViewModel = monitorViewModel)
                    deleteBottomSheet?.show(activity!!.supportFragmentManager, deleteBottomSheet?.tag)
                },
                onEditClick = {
                    editBottomSheet = EditNodeBottomSheet.newInstance(monitor = it, monitorViewModel = monitorViewModel)
                    editBottomSheet?.show(activity!!.supportFragmentManager, editBottomSheet?.tag)
                },
                onSensorSetting = { monitor, sensorIndex ->
                    val sensorSetting = MonitorFragmentDirections.settingSensor(monitor, sensorIndex)
                    view.findNavController().navigate(sensorSetting)

                }
        ).also {
            binding.rvListNode.adapter = it
            adapter = it
        }


        addBottomSheet = AddNodeBottomSheet.newInstance(serviceTag = serviceTag, monitorViewModel = monitorViewModel)

        monitorViewModel.apply {
            loadMonitor(serviceTag)

            monitors.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                    adapter.submitList(it.data)
                }
            })

            addMonitor.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        addBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            deleteMonitor.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        deleteBottomSheet?.dismiss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            editMonitor.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        editBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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
                        myMqttSubscribe("GET-ENVIRONMENT-PARAMS")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Toast.makeText(context, "CONNECTION FAILED", Toast.LENGTH_SHORT).show()
                    }
                }
            } else myMqttSubscribe("GET-ENVIRONMENT-PARAMS")

            client.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Toast.makeText(context, "$topic - ${message.toString()}", Toast.LENGTH_SHORT).show()
                    monitorViewModel.loadMonitor(serviceTag)
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
