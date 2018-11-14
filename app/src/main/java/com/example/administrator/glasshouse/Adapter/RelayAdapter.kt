package com.example.administrator.glasshouse.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.administrator.glasshouse.ModelTest.DataCheck_relay
import com.example.administrator.glasshouse.R


class RelayAdapter(val relayStatus: ArrayList<DataCheck_relay>,val context: Context) : RecyclerView.Adapter<RelayAdapter.RelayViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelayViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_relay, parent, false)
        return RelayViewholder(view)
    }

    override fun getItemCount(): Int {
        return relayStatus.size
    }

    override fun onBindViewHolder(holder: RelayViewholder, position: Int) {
        var b1status = relayStatus[position].b1Status
        var b2status = relayStatus[position].b2Status
        val name = relayStatus[position].nodeName
        checkStatusB1(holder.imgBut1,b1status)
        checkStatusB2(holder.imgBut2,b1status)

        holder.relayName.text = name
        holder.imgBut1.setOnClickListener {
            b1status = !b1status
            // Mỗi một Node Relay chỉ có 2 trạng thái đèn
            // Có thể có nhiều đèn
            checkStatusB1(holder.imgBut1, b1status)
        }
        holder.imgBut2.setOnClickListener {
            b2status = !b2status
            checkStatusB2(holder.imgBut2,b2status)
        }
    }

    inner class RelayViewholder(item: View) : RecyclerView.ViewHolder(item) {
        val imgBut1 = item.findViewById<View>(R.id.btnRelay1) as ImageButton
        val imgBut2 = item.findViewById<View>(R.id.btnRelay2) as ImageButton
        val relayName = item.findViewById<View>(R.id.txtRelayName) as TextView
    }

    private fun checkStatusB1(button:ImageButton,status: Boolean) {
        // Quy ước ID Quạt:0 ; Đèn:1 ; Bơm:2
        if (status) {
            button.setBackgroundResource(R.drawable.custom_image_button_on)
            button.setImageResource(R.drawable.ic_fan)
        } else {
            button.setBackgroundResource(R.drawable.custom_image_button_off)
            button.setImageResource(R.drawable.ic_fan_off)
        }
    }

    private fun checkStatusB2(button:ImageButton,status: Boolean) {
        // Quy ước ID Quạt:0 ; Đèn:1 ; Bơm:2
        if (status) {
            button.setBackgroundResource(R.drawable.custom_image_button_on)
            button.setImageResource(R.drawable.ic_light_bulb)
        } else {
            button.setBackgroundResource(R.drawable.custom_image_button_off)
            button.setImageResource(R.drawable.ic_light_bulb_off)
        }
    }


}