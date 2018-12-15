package com.mandevices.iot.agriculture.ui.control


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.databinding.ItemControlRelayBinding
import com.mandevices.iot.agriculture.ui.common.DataBoundListAdapter
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.vo.Control

class ControlAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val onDeleteClick: (Control) -> Unit,
        private val onEditClick: (Control) -> Unit,
        private val onRelaySetting: (Control, Int) -> Unit
) : DataBoundListAdapter<Control, ItemControlRelayBinding>(
        appExecutors = appExecutors,
        diffCallback = CONTROL_COMPARATOR
) {
    override fun createBinding(parent: ViewGroup): ItemControlRelayBinding {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_control_relay,
                parent,
                false,
                dataBindingComponent
        )
    }

    override fun bind(binding: ItemControlRelayBinding, item: Control) {

        //        C-003-0:F
        val relayHexString = item.tag.split(":")[1]
        val relayHex = java.lang.Long.parseLong(relayHexString, 16)
        val relayBit = relayHex.toString(2)
        val relayBitInt = mutableListOf<Int>()
        for (bit in relayBit.toByteArray()) {
            relayBitInt.add(bit - 48)
        }


        binding.apply {

            btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
            btnEdit.setOnClickListener {
                onEditClick(item)
            }

            relaySetupButton0.setOnClickListener {
                onRelaySetting(item, 1)
            }

            relaySetupButton1.setOnClickListener {
                onRelaySetting(item, 2)
            }

            relayNameText2.setOnClickListener {
                onRelaySetting(item, 3)
            }

            relaySetupButton3.setOnClickListener {
                onRelaySetting(item, 4)
            }

        }

        binding.relayBit = relayBitInt.reversed()
        binding.control = item


    }

    companion object {
        val CONTROL_COMPARATOR = object : DiffUtil.ItemCallback<Control>() {
            override fun areContentsTheSame(oldItem: Control, newItem: Control): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Control, newItem: Control): Boolean =
                    oldItem.tag == newItem.tag
        }


    }
}