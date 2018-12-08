package com.example.administrator.glasshouse.ui.monitor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.administrator.glasshouse.*
import com.example.administrator.glasshouse.databinding.ItemMonitorBinding
import com.example.administrator.glasshouse.ui.common.DataBoundListAdapter
import com.example.administrator.glasshouse.vo.Monitor
import javax.inject.Inject


class MonitorAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors) : DataBoundListAdapter<Monitor, ItemMonitorBinding>(
        appExecutors = appExecutors,
        diffCallback = MONITOR_COMPARATOR
) {

    override fun createBinding(parent: ViewGroup): ItemMonitorBinding {
        val binding = DataBindingUtil.inflate<ItemMonitorBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_monitor,
                parent,
                false,
                dataBindingComponent
        )

        return binding
    }

    override fun bind(binding: ItemMonitorBinding, item: Monitor) {
        binding.monitor = item
    }


    companion object {
        val MONITOR_COMPARATOR = object : DiffUtil.ItemCallback<Monitor>() {
            override fun areContentsTheSame(oldItem: Monitor, newItem: Monitor): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Monitor, newItem: Monitor): Boolean =
                    oldItem.id == newItem.id
        }


    }
}