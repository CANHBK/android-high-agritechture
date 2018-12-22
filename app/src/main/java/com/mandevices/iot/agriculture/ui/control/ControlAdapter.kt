package com.mandevices.iot.agriculture.ui.control


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.databinding.ItemControlRelayBinding
import com.mandevices.iot.agriculture.db.RelayDao
import com.mandevices.iot.agriculture.ui.common.DataBoundListAdapter
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Relay

class ControlAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val controlViewModel:ControlViewModel,
        private val onDeleteClick: (Control) -> Unit,
        private val onEditClick: (Control) -> Unit,
        private val onRelaySetting: (Control, Int) -> Unit,
        private val onSetState:(ItemControlRelayBinding,String,String,Int,String)->Unit
) : DataBoundListAdapter<Control, ItemControlRelayBinding>(
        appExecutors = appExecutors,
        diffCallback = CONTROL_COMPARATOR
) {
    private lateinit var view: ItemControlRelayBinding
    override fun createBinding(parent: ViewGroup): ItemControlRelayBinding {
        view = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_control_relay,
                parent,
                false,
                dataBindingComponent
        )
        return view
    }

    override fun bind(binding: ItemControlRelayBinding, item: Control) {

        val gson = GsonBuilder().setPrettyPrinting().create()

        var relayList: List<Relay> = gson.fromJson(item.relays, object : TypeToken<List<Relay>>() {}.type)

        relayList = relayList.sortedWith(compareBy {
            it.index
        })

        //        C-003-0:F
        val relayHexString = item.tag.split(":")[1]
        val relayHex = java.lang.Long.parseLong(relayHexString, 16)
        val relayBit = relayHex.toString(2)
        val relayBitInt = mutableListOf<Int>()
        for (bit in relayBit.toByteArray()) {
            relayBitInt.add(bit - 48)
        }

        binding.viewModel=controlViewModel

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

        }

        binding.relayList = relayList
        binding.control = item
        binding.index = -1

        binding.deviceImage0.setOnClickListener {
            onSetState(binding,item.serviceTag,item.tag,1,relayList[0].state)
        }
        binding.deviceImage1.setOnClickListener {
            onSetState(binding,item.serviceTag,item.tag,2,relayList[1].state)
        }

//        binding.result=controlViewModel.setStateRelay

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