package com.example.administrator.glasshouse.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.example.administrator.glasshouse.util.AppExecutors
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.databinding.ItemGateWayBinding
import com.example.administrator.glasshouse.ui.common.DataBoundListAdapter
import com.example.administrator.glasshouse.vo.Gate

class GateAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val onDeleteClick: (Gate) -> Unit,
        private val onEditClick: (Gate) -> Unit
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

        return binding
    }

    override fun bind(binding: ItemGateWayBinding, item: Gate) {
        binding.gate = item
        binding.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
        binding.btnEdit.setOnClickListener {
            onEditClick(item)
        }
        binding.root.setOnClickListener {
            Log.d("test","helo")

            val monitor = DashboardFragmentDirections.monitor(item.serviceTag)
            it.findNavController().navigate(monitor)
        }
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