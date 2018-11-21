package com.example.administrator.glasshouse.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.administrator.glasshouse.Model.SensorData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SettingSensorActivity
import com.example.administrator.glasshouse.Utils.Config


class SensorAdapter(val dataList: ArrayList<SensorData>, val context: Context) : RecyclerView.Adapter<SensorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDoAm.text = dataList[position].airhummi
        holder.txtTemp.text = dataList[position].temp
        holder.txtGroundHimi.text = dataList[position].groundHumi
        holder.txtLight.text = dataList[position].light
        holder.txtPin.text = dataList[position].pin.toString() + "%"
        holder.progessbar.progress = dataList[position].pin!!
        holder.txtNodeName.text = dataList[position].nodeName
        holder.setNode.setOnClickListener {
            val mSharedPreferences = context.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
            val editor = mSharedPreferences.edit()
            editor.putString(Config.SSName, holder.txtNodeName.text.toString())
            editor.putString(Config.SENSOR_TAG, dataList[position].nodeSensorID)
            editor.apply()
            sendToSensorSetting()
        }
    }

    private fun sendToSensorSetting() {
        val intent = Intent(context, SettingSensorActivity::class.java)
        context.startActivity(intent)
    }


    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

        val txtNodeName = item.findViewById<View>(R.id.txtNodeName) as TextView
        val txtTemp = item.findViewById<View>(R.id.txtTemp) as TextView
        val txtDoAm = item.findViewById<View>(R.id.txtAirHumi) as TextView
        val txtLight = item.findViewById<View>(R.id.txtLight) as TextView
        val txtPin = item.findViewById<View>(R.id.txtPin) as TextView
        val txtGroundHimi = item.findViewById<View>(R.id.txtGroundHumi) as TextView
        val progessbar = item.findViewById<View>(R.id.battery) as ProgressBar
        val setNode: Button = item.findViewById<View>(R.id.btnSetDevice) as Button

    }
}