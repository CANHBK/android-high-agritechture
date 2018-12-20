package com.mandevices.iot.agriculture.ui.chart

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class FragmentChart : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var monitorViewModel: MonitorViewModel

    private var sensorData: String? = null

    private lateinit var monitorTag: String
    private var dataIndex: Int? = null

    @Inject
    lateinit var client: MqttAndroidClient

    @Inject
    lateinit var mqttConnectOptions: MqttConnectOptions


    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentChartBinding>()

    private var adapter by autoCleared<ControlAdapter>()

    private val sensorNameList = listOf<String>("", "Nhiệt độ", "Ánh sáng", "Độ ẩm không khí", "Độ ẩm đất")

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        monitorTag = FragmentChartArgs.fromBundle(arguments).monitorTag
        dataIndex = FragmentChartArgs.fromBundle(arguments).dataIndex

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

            nodeName.text = monitorTag

            sensorType.text = sensorNameList[dataIndex!!]

            setDateButton.setOnClickListener {
                val c = Calendar.getInstance()
                val mYear = c.get(Calendar.YEAR)
                val mMonth = c.get(Calendar.MONTH)
                val mDay = c.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(context,
                        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                            this.dateText.text = "$dayOfMonth/${month + 1}/$year"
                            monitorViewModel.getMonitorDataByDate(
                                    tag = monitorTag,
                                    year = year,
                                    month = month + 1,
                                    day = dayOfMonth)
                        },
                        mYear, mMonth, mDay)
                        .show()
            }

            binding.lineChart.apply {
                axisLeft.apply {
                    axisMinimum = 0f
                    setDrawGridLines(false)
                }
                axisRight.apply {
                    setDrawGridLines(false)
                }
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                }
                axisRight.isEnabled = false
                isDragEnabled = true
                Description().also { description ->
                    description.text = "Ten Sensor"
                    this.description = description
                }
                isScaleXEnabled = true
                isScaleYEnabled = true
                setVisibleXRangeMaximum(12f)
                animateX(1000, Easing.Linear)
                moveViewToX(100f)
                legend.isEnabled = true
            }
        }

        monitorViewModel.apply {

            getMonitorDataByDate(tag = monitorTag)

            monitorDataByDate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    sensorData = it.data!!.content
                    if (sensorData != null) {
                        val dataArray = JSONArray(sensorData)

                        val entries = mutableListOf<Entry>()
                        val dataValueArray = mutableListOf<Int>()
                        for (i in 0 until dataArray.length()) {
                            val dataObject = dataArray.getJSONObject(i)
                            val dataValue = dataObject.getJSONArray("value")
                            val dataInt = dataValue.optInt(dataIndex!! - 1)
                            dataValueArray.add(dataInt)
                            val dataTimeLong = dataObject.getLong("updatedAt")
                            val date = Calendar.getInstance()
                            date.timeInMillis = dataTimeLong
                            entries.add(
                                    Entry((date.get(Calendar.HOUR_OF_DAY) + date.get(Calendar.MINUTE) / 60).toFloat(),
                                            dataInt.toFloat()))
                        }

                        var aveValue = 0f
                        for (value in dataValueArray) {
                            aveValue += value * 1.0f / dataValueArray.size
                        }

                        binding.maxValText.text = "${dataValueArray.max()}"
                        binding.minValText.text = "${dataValueArray.min()}"
                        binding.aveValText.text = "${aveValue.roundToInt()}"

                        LineDataSet(entries, "Cảm biến")
                                .also { lineDataSet ->
                                    lineDataSet.apply {
                                        color = Color.RED
                                        circleColors = listOf(Color.RED)
                                        setDrawFilled(true)
                                        fillColor = Color.parseColor("#ffc9c7")
                                        valueTextColor = Color.parseColor("#000000")
                                    }

                                    binding.lineChart.data = LineData(lineDataSet)
                                    binding.lineChart.invalidate()
                                }
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