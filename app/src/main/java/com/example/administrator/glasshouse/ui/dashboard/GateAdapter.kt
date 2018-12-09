package com.example.administrator.glasshouse.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.databinding.ItemGateWayBinding
import com.example.administrator.glasshouse.ui.common.DataBoundListAdapter
import com.example.administrator.glasshouse.vo.Gate

class GateAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors
) : DataBoundListAdapter<Gate, ItemGateWayBinding>(
        appExecutors = appExecutors,
        diffCallback = GATE_COMPARATOR
) {
    override fun createBinding(parent: ViewGroup): ItemGateWayBinding {
        val binding = DataBindingUtil.inflate<ItemGateWayBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_gate_way,
                parent,
                false,
                dataBindingComponent
        )

//        binding.btnControl.setOnClickListener {
//            it.findNavController().navigate(R.id.action_homeFragment_to_controlFragment)
//        }
//
//        binding.btnMonitor.setOnClickListener {
//            it.findNavController().navigate(R.id.action_homeFragment_to_sensorFragment)
//        }
        return binding
    }

    override fun bind(binding: ItemGateWayBinding, item: Gate) {
        binding.gate = item
    }

    companion object {
        val GATE_COMPARATOR = object : DiffUtil.ItemCallback<Gate>() {
            override fun areContentsTheSame(oldItem: Gate, newItem: Gate): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Gate, newItem: Gate): Boolean =
                    oldItem.serviceTag == newItem.serviceTag
        }


    }
}