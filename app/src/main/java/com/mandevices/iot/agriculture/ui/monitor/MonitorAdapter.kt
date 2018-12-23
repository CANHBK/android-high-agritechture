package com.mandevices.iot.agriculture.ui.monitor


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.databinding.ItemMonitorNodeBinding
import com.mandevices.iot.agriculture.ui.common.DataBoundListAdapter
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.MonitorWithSensors
import com.mandevices.iot.agriculture.vo.Relay
import com.mandevices.iot.agriculture.vo.Sensor

class MonitorAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val monitorViewModel: MonitorViewModel,
        private val onDeleteClick: (Monitor) -> Unit,
        private val onEditClick: (Monitor) -> Unit,
        private val onSensorSetting: (Monitor, Sensor) -> Unit,
        private val onRefresh: (ItemMonitorNodeBinding,Monitor) -> Unit,
        private val onDataChartClick: (String, Int) -> Unit
) : DataBoundListAdapter<MonitorWithSensors, ItemMonitorNodeBinding>(
        appExecutors = appExecutors,
        diffCallback = MONITOR_COMPARATOR
) {
    override fun createBinding(parent: ViewGroup): ItemMonitorNodeBinding {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_monitor_node,
                parent,
                false,
                dataBindingComponent
        )
    }

    override fun bind(binding: ItemMonitorNodeBinding, item: MonitorWithSensors) {
//
//        val gson = GsonBuilder().setPrettyPrinting().create()
//
//        //TODO: Khi lấy dữ liệu mới nhất thì item.sensors bị null, mặc dù trong repository không null
//        var sensorsListInput: List<Sensor> = gson.fromJson(item.monitor?.sensors, object : TypeToken<List<Sensor>>() {}.type)
//
//        sensorsListInput = sensorsListInput.sortedWith(compareBy {
//            it.index
//        })

        //        E-003-F:0
        val sensorHexString = item.monitor?.tag!!.split(":")[0].split("-")[2]
        val sensorHex = java.lang.Long.parseLong(sensorHexString, 16)
//        val sensorBit =
        val sensorBitInt = mutableListOf<Int>()
        for (bit in sensorHex.toString(2).toByteArray()) {
            sensorBitInt.add(bit - 48)
        }


        binding.apply {
            monitor = item.monitor
            viewModel = monitorViewModel
            btnReload.setOnClickListener {
                onRefresh(binding,item.monitor!!)
            }
            btnDelete.setOnClickListener {
                onDeleteClick(item.monitor!!)
            }
            btnEdit.setOnClickListener {
                onEditClick(item.monitor!!)
            }

            tempSensorSetupButton.setOnClickListener {
                onSensorSetting(item.monitor!!, item.sensorList!![0])
            }

            lightSensorSetting.setOnClickListener {
                onSensorSetting(item.monitor!!, item.sensorList!![1])
            }

            airHumiSetupButton.setOnClickListener {
                onSensorSetting(item.monitor!!, item.sensorList!![2])
            }

            gndSetupButton.setOnClickListener {
                onSensorSetting(item.monitor!!, item.sensorList!![3])
            }

            temp.setOnClickListener {
                onDataChartClick(item.monitor!!.tag, 1)
            }

            light.setOnClickListener {
                onDataChartClick(item.monitor!!.tag, 2)
            }

            airHumi.setOnClickListener {
                onDataChartClick(item.monitor!!.tag, 3)
            }

            gndHumi.setOnClickListener {
                onDataChartClick(item.monitor!!.tag, 4)
            }
            sensorsList = item.sensorList!!
            sensorBit = sensorBitInt.reversed()
        }



    }

    companion object {
        val MONITOR_COMPARATOR = object : DiffUtil.ItemCallback<MonitorWithSensors>() {
            override fun areContentsTheSame(oldItem: MonitorWithSensors, newItem: MonitorWithSensors): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: MonitorWithSensors, newItem: MonitorWithSensors): Boolean =
                    oldItem.monitor?.tag == newItem.monitor?.tag
        }


    }
}