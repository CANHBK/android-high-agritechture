package com.example.administrator.glasshouse.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.administrator.glasshouse.ModelTest.Datacheck_2
import com.example.administrator.glasshouse.R

class RecyclerMultiAdapter(val dataList: ArrayList<Datacheck_2>) : RecyclerView.Adapter<RecyclerMultiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerMultiAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_row_multi, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = dataList[position].name
        if (dataList[position].isActived) {
            holder.stateSensor.visibility = View.VISIBLE
            holder.stateError.visibility = View.INVISIBLE
        } else {
            holder.stateSensor.visibility = View.INVISIBLE
            holder.stateError.visibility = View.VISIBLE
        }

    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val txtName: TextView = item.findViewById<View>(R.id.txtDeviceName) as TextView
        val stateSensor: View = item.findViewById<View>(R.id.stateSensor)
        val stateError: View = item.findViewById<View>(R.id.errorSensor)
    }
}