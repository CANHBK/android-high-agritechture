package com.example.administrator.glasshouse.ui.gate

import android.content.Context
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.GetAllGateOfUserQuery
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.config.config

class GateAdapter(val gateWayList: List<GetAllGateOfUserQuery.AllGatesOfUser>, val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<GateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_gate_way, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gateWayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gateWay = gateWayList[position]
        val totalNodeControl = gateWay.controls()?.toInt()
        val totalNodeEnv = gateWay.monitors()?.toInt()
        holder.name.text = gateWay.name()
        holder.subName.text = gateWay.serviceTag()
        holder.txtTotalNodeControl.text = totalNodeControl.toString()
        holder.txtTotalNodeEnv.text = totalNodeEnv.toString()
        if (totalNodeControl == 0) holder.btnControl.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
        if (totalNodeEnv == 0) holder.btnMonitor.backgroundTintList = (context.resources.getColorStateList(R.color.secondary_text))
        holder.btnMonitor.setOnClickListener {
            if (totalNodeEnv != 0) {
                val serviceTagBundle = Bundle()
                serviceTagBundle.putString(config.SERVICE_TAG_BUNDLE, gateWay.serviceTag());
                it.findNavController().navigate(R.id.action_homeFragment_to_sensorFragment, serviceTagBundle)
            }
            Snackbar.make(it, "Chưa có Node Environment để giám sát", Snackbar.LENGTH_LONG).show()

        }
        holder.btnControl.setOnClickListener {
            if (totalNodeControl != 0) {
                val serviceTagBundle = Bundle()
                serviceTagBundle.putString(config.SERVICE_TAG_BUNDLE, gateWay.serviceTag());
                it.findNavController().navigate(R.id.action_homeFragment_to_controlFragment, serviceTagBundle)
            }
            Snackbar.make(it, "Chưa có Node Control để điều khiển", Snackbar.LENGTH_LONG).show()

        }

    }

    inner class ViewHolder(item: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(item) {
        val name: TextView = item.findViewById<View>(R.id.txt_name_gate_way) as TextView
        val subName: TextView = item.findViewById<View>(R.id.txt_sub_name_gate_way) as TextView
        val txtTotalNodeEnv = item.findViewById<View>(R.id.txt_total_node_env) as TextView
        val txtTotalNodeControl = item.findViewById<View>(R.id.txt_total_node_control) as TextView
        val btnMonitor: MaterialButton = item.findViewById(R.id.btn_monitor)
        val btnControl: MaterialButton = item.findViewById(R.id.btn_control)
    }
}