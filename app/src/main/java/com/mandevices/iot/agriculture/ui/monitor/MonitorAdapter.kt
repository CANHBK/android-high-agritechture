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

class MonitorAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val onDeleteClick: (Monitor) -> Unit,
        private val onEditClick: (Monitor) -> Unit,
        private val onSensorSetting: (Monitor, Int) -> Unit,
        private val onDataChartClick: (String, Int) -> Unit
) : DataBoundListAdapter<Monitor, ItemMonitorNodeBinding>(
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

    override fun bind(binding: ItemMonitorNodeBinding, item: Monitor) {

        //        E-003-F:0
        val sensorHexString = item.tag.split(":")[0].split("-")[2]
        val sensorHex = java.lang.Long.parseLong(sensorHexString, 16)
        val sensorBit = sensorHex.toString(2)
        val sensorBitInt = mutableListOf<Int>()
        for (bit in sensorBit.toByteArray()) {
            sensorBitInt.add(bit - 48)
        }


        binding.apply {
            monitor = item
            btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
            btnEdit.setOnClickListener {
                onEditClick(item)
            }

            tempSensorSetupButton.setOnClickListener {
                onSensorSetting(item, 1)
            }

            lightSensorSetting.setOnClickListener {
                onSensorSetting(item,2)
            }

            airHumiSetupButton.setOnClickListener {
                onSensorSetting(item,3)
            }

            gndSetupButton.setOnClickListener {
                onSensorSetting(item,4)
            }

            temp.setOnClickListener {
                onDataChartClick(item.tag,1)
            }

            light.setOnClickListener {
                onDataChartClick(item.tag,2)
            }

            airHumi.setOnClickListener {
                onDataChartClick(item.tag,3)
            }

            gndHumi.setOnClickListener {
                onDataChartClick(item.tag,4)
            }

        }

        binding.sensorBit = sensorBitInt.reversed()


    }

    companion object {
        val MONITOR_COMPARATOR = object : DiffUtil.ItemCallback<Monitor>() {
            override fun areContentsTheSame(oldItem: Monitor, newItem: Monitor): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Monitor, newItem: Monitor): Boolean =
                    oldItem.tag == newItem.tag
        }


    }
}