package com.mandevices.iot.agriculture.ui.monitor


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.databinding.ItemMonitorNodeBinding
import com.mandevices.iot.agriculture.ui.common.DataBoundListAdapter
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.MonitorWithSensors
import com.mandevices.iot.agriculture.vo.Sensor

class MonitorAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val monitorViewModel: MonitorViewModel,
        private val onDeleteClick: (Monitor) -> Unit,
        private val onEditClick: (Monitor) -> Unit,
        private val onSetTimeSensor: (Sensor) -> Unit,
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
                onSetTimeSensor(item.sensorList!![0])
            }

            lightSensorSetting.setOnClickListener {
                onSetTimeSensor(item.sensorList!![1])
            }

            airHumiSetupButton.setOnClickListener {
                onSetTimeSensor( item.sensorList!![2])
            }

            gndSetupButton.setOnClickListener {
                onSetTimeSensor( item.sensorList!![3])
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