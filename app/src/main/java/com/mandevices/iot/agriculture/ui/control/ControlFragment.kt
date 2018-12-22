package com.mandevices.iot.agriculture.ui.control


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
import com.mandevices.iot.agriculture.databinding.FragmentControlBinding
import com.mandevices.iot.agriculture.databinding.FragmentDashboardBinding
import com.mandevices.iot.agriculture.db.RelayDao
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.ui.dashboard.*
import com.mandevices.iot.agriculture.ui.monitor.MonitorFragmentArgs
import com.mandevices.iot.agriculture.ui.monitor.MonitorFragmentDirections
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Const
import com.mandevices.iot.agriculture.vo.Relay
import com.mandevices.iot.agriculture.vo.Status
import com.mandevices.iot.agriculture.vo.User
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject


class ControlFragment : Fragment(), Injectable {

    private var addBottomSheet: AddControlBottomSheet? = null
    private var deleteBottomSheet: DeleteControlBottomSheet? = null
    private var editBottomSheet: EditControlBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var relayDao:RelayDao

    private lateinit var controlViewModel: ControlViewModel

    private lateinit var serviceTag: String

    @Inject
    lateinit var client: MqttAndroidClient

    @Inject
    lateinit var mqttConnectOptions: MqttConnectOptions


    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentControlBinding>()

    private var adapter by autoCleared<ControlAdapter>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        serviceTag = ControlFragmentArgs.fromBundle(arguments).serviceTag

        val dataBinding = DataBindingUtil.inflate<FragmentControlBinding>(
                inflater,
                R.layout.fragment_control,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        controlViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ControlViewModel::class.java)

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)
        (activity as AppCompatActivity).setSupportActionBar(binding.bottomAppBar)


        setupMqtt()

        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)

            topToolbar.setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }
            result = controlViewModel.controls

            fabAdd.setOnClickListener {

               try {
                   addBottomSheet?.show(activity!!.supportFragmentManager, addBottomSheet?.tag)
               }catch (e:Exception){
                   e.printStackTrace()
               }


            }
            topToolbar.inflateMenu(R.menu.menu_dashboard)
        }



        ControlAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                onDeleteClick = {
                    deleteBottomSheet = DeleteControlBottomSheet.newInstance(
                            control = it, controlViewModel = controlViewModel)
                    deleteBottomSheet?.show(activity!!.supportFragmentManager, deleteBottomSheet?.tag)
                },
                onEditClick = {
                    editBottomSheet = EditControlBottomSheet.newInstance(control = it, controlViewModel = controlViewModel)
                    editBottomSheet?.show(activity!!.supportFragmentManager, editBottomSheet?.tag)
                },
                onRelaySetting = { control, relayIndex ->
                    val relaySetting = ControlFragmentDirections.settingRelay(control, relayIndex)
                    view.findNavController().navigate(relaySetting)

                },
                onSetState = {
                  binding, serviceTag,tag, index,state ->
                    controlViewModel.setState(serviceTag = serviceTag,tag = tag,index = index,state = state)
                    binding.setLifecycleOwner(viewLifecycleOwner)
                    binding.result=controlViewModel.setStateRelay
                    binding.index = index
//                    controlViewModel.setStateRelay.observe(viewLifecycleOwner, Observer {
//                        binding.result=it
//                        binding.index = index
//                    })
                },
                controlViewModel = controlViewModel

        ).also {
            binding.relayRecyclerView.adapter = it
            adapter = it
        }


        addBottomSheet = AddControlBottomSheet.newInstance(serviceTag = serviceTag, controlViewModel = controlViewModel)


        controlViewModel.apply {

            loadControls(serviceTag = serviceTag)


            controls.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    adapter.submitList(it.data)
                }
            })

            addControl.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        addBottomSheet?.dismiss()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            })

            deleteControl.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        deleteBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            editControl.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        editBottomSheet?.dismiss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
            refresh.observe(viewLifecycleOwner, Observer {  })
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
//                        Toast.makeText(context, "CONNECTION FAILED", Toast.LENGTH_SHORT).show()
                    }
                }
            } else myMqttSubscribe("RELAY")

            client.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val atJsonObject = JSONObject(message.toString())
                    val type = atJsonObject.getString("type")
                    if(type=="auto"){
                        controlViewModel.refresh(serviceTag)
                    }
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
