package com.example.administrator.glasshouse.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.administrator.glasshouse.Model.NodeControlData
import com.example.administrator.glasshouse.Model.RelayData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SettingRelayActivity
import com.example.administrator.glasshouse.Utils.Config


class RelayAdapter(val statusRelays: ArrayList<RelayData>, val context: Context) : RecyclerView.Adapter<RelayAdapter.RelayViewholder>() {

    var mAdapterCallBack: AdapterCallback
    lateinit var mSharedPreferences: SharedPreferences

    init {
        this.mAdapterCallBack = context as AdapterCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelayViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_relay, parent, false)

        return RelayViewholder(view)
    }

    override fun getItemCount(): Int {
        return statusRelays.size
    }

    override fun onBindViewHolder(holder: RelayViewholder, position: Int) {
        var b1status = statusRelays[position].b1Status
        var b2status = statusRelays[position].b2Status
        val name = statusRelays[position].nodeName
        val relayTag = statusRelays[position].nodeId
        var type: Long
        checkStatusB1(holder.imgBut1, b1status)
        checkStatusB2(holder.imgBut2, b2status)

        mSharedPreferences = context.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)

        holder.relayName.text = name
//        holder.progress_1.isEnabled = false
//        holder.progress_2.isEnabled = false
        holder.imgBut1.setOnClickListener {
            holder.progress_1.visibility = View.VISIBLE
            holder.imgBut1.visibility = View.GONE
            type = 1
            b1status = !b1status
            //holder.progress_1.isEnabled = true
            //holder.progress_1.visibility = View.VISIBLE
            saveSharedPreStatus(name, type, relayTag, b1status)
            mAdapterCallBack.onMethodCallback()

            //holder.progress_1.isEnabled = false
            checkStatusB1(holder.imgBut1, b1status)
            holder.progress_1.visibility = View.INVISIBLE
            holder.imgBut1.visibility = View.VISIBLE
            //Toast.makeText(context,"Check",Toast.LENGTH_SHORT).show()
        }


        holder.imgBut2.setOnClickListener {
            holder.progress_2.visibility = View.VISIBLE
            holder.imgBut2.visibility = View.GONE
            type = 2
            b2status = !b2status
            //holder.progress_2.isEnabled = true
            //holder.progress_2.visibility = View.VISIBLE
            saveSharedPreStatus(name, type, relayTag, b2status)
            mAdapterCallBack.onMethodCallback()
            //holder.progress_1.isEnabled = false
            checkStatusB2(holder.imgBut2, b2status)
            holder.progress_2.visibility = View.INVISIBLE
            holder.imgBut2.visibility = View.VISIBLE
        }

        holder.imgBut1.setOnLongClickListener {
            type = 1
            saveSharedPreStatus(name, type, relayTag, b1status)
            sendToSettingRelay()
            return@setOnLongClickListener true
        }
        holder.imgBut2.setOnLongClickListener {
            type = 2
            saveSharedPreStatus(name, type, relayTag, b2status)
            sendToSettingRelay()
            return@setOnLongClickListener true
        }
    }

    private fun sendToSettingRelay() {
        val intent = Intent(context, SettingRelayActivity::class.java)
        context.startActivity(intent)
    }

    private fun saveSharedPreStatus(name: String, type: Long, relayTag: String, status: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putString(Config.RelayName, name)
        editor.putLong(Config.RELAY_TYPE, type)
        editor.putString(Config.RELAY_TAG, relayTag)
        if (status) editor.putString(Config.RELAY_STATE, "O")
        else editor.putString(Config.RELAY_STATE, "F")
        editor.apply()
    }

    //Tạo một interface để Main activity có thể xử lý sự kiện trong Adapter
    //Reduce code trong adapter
    // Main activity sẽ xử lý sự kiện gửi thông tin lên server
    interface AdapterCallback {
        fun onMethodCallback()
    }

    inner class RelayViewholder(item: View) : RecyclerView.ViewHolder(item) {
        val imgBut1 = item.findViewById<View>(R.id.btnRelay1) as ImageButton
        val imgBut2 = item.findViewById<View>(R.id.btnRelay2) as ImageButton
        val relayName = item.findViewById<View>(R.id.txtRelayName) as TextView
        val progress_1 = item.findViewById<View>(R.id.progressBtn1) as ProgressBar
        val progress_2 = item.findViewById<View>(R.id.progressBtn2) as ProgressBar
    }

    private fun checkStatusB1(button: ImageButton, status: Boolean) {
        // Quy ước ID Quạt:0 ; Đèn:1 ; Bơm:2
        if (status) {
            //button.visibility = View.GONE
            button.setBackgroundResource(R.drawable.custom_image_button_on)
            button.setImageResource(R.drawable.ic_fan)
        } else {
            button.setBackgroundResource(R.drawable.custom_image_button_off)
            button.setImageResource(R.drawable.ic_fan_off)
        }
    }

    private fun checkStatusB2(button: ImageButton, status: Boolean) {
        // Quy ước ID Quạt:0 ; Đèn:1 ; Bơm:2
        if (status) {
            //button.visibility = View.GONE
            button.setBackgroundResource(R.drawable.custom_image_button_on)
            button.setImageResource(R.drawable.ic_light_bulb)
        } else {
            button.setBackgroundResource(R.drawable.custom_image_button_off)
            button.setImageResource(R.drawable.ic_light_bulb_off)
        }
    }


}