package com.example.administrator.glasshouse.ui.monitor

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.databinding.ItemMonitorNodeBinding
import com.example.administrator.glasshouse.ui.common.DataBoundListAdapter
import com.example.administrator.glasshouse.util.AppExecutors
import com.example.administrator.glasshouse.vo.Monitor

class MonitorAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val onDeleteClick: (Monitor) -> Unit,
        private val onEditClick: (Monitor) -> Unit
) : DataBoundListAdapter<Monitor, ItemMonitorNodeBinding>(
        appExecutors = appExecutors,
        diffCallback = MONITOR_COMPARATOR
) {
    override fun createBinding(parent: ViewGroup): ItemMonitorNodeBinding {
        val binding = DataBindingUtil.inflate<ItemMonitorNodeBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_monitor_node,
                parent,
                false,
                dataBindingComponent
        )

        return binding
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
        }

        binding.sensorBit = sensorBitInt.reversed()


//        Log.d("Tesst", sensorHexCode.toString())
//        binding.monitor = item
//        binding.btnDelete.setOnClickListener {
//            onDeleteClick(item)
//        }
//        binding.btnEdit.setOnClickListener {
//            onEditClick(item)
//        }
//        binding.root.setOnClickListener {
//            val monitor = DashboardFragmentDirections.ActionHomeFragmentToMonitorFragment(item.serviceTag)
//            it.findNavController().navigate(monitor)
//        }
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