package com.example.administrator.glasshouse.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.*
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.model.MonitorModel
import com.example.administrator.glasshouse.model.SensorModel
import com.example.administrator.glasshouse.type.AllSensorsInput
import com.example.administrator.glasshouse.type.NewEnvironmentParamsInput


class MonitorAdapter(val nodeEnvList: List<GetAllNodeEnvQuery.AllNodesEnv>, var realTimeParams: AutoUpdatedEnvironmentSubSubscription.AutoUpdatedEnvironmentParams, val context: Context, val activity: Activity) : RecyclerView.Adapter<MonitorAdapter.ViewHolder>() {
    lateinit var view: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        view = layoutInflater.inflate(R.layout.item_node_env, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nodeEnvList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nodeEnv = nodeEnvList[position]
        getSensorData(view, nodeEnv.serviceTag()!!, nodeEnv.nodeEnv()!!, holder)


        holder.name.text = nodeEnv.name()
        holder.nodEnv.text = nodeEnv.nodeEnv()
        holder.btnRefresh.setOnClickListener {
            holder.progress.visibility = View.VISIBLE
            holder.btnRefresh.visibility = View.INVISIBLE
            getLastEnvironmentParam(it, nodeEnv.nodeEnv()!!, nodeEnv.serviceTag()!!, holder)
        }
        holder.btnSetting.setOnClickListener {
            val data = Bundle()
            val monitor = MonitorModel(nodeEnv.serviceTag()!!, nodeEnv.nodeEnv()!!, nodeEnv.name()!!)
            val sensor = ArrayList<SensorModel>()
            for (item in nodeEnv.sensors()) {
                val ss = SensorModel(item.index()!!, item.name()!!)
                sensor.add(ss)
            }
            data.putSerializable("monitor", monitor)
            data.putSerializable("sensor", sensor)
            it.findNavController().navigate(R.id.action_sensorFragment_to_configTimeMonitorFragment, data)
        }
    }


    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val recyclerViewSensor = item.findViewById<View>(R.id.recyler_view_sensor) as RecyclerView
        val name = item.findViewById<View>(R.id.txt_name_node_env) as TextView
        val nodEnv = item.findViewById<View>(R.id.txt_id_node_env) as TextView
        val btnRefresh = item.findViewById<View>(R.id.refresh_node_env) as ImageButton
        val progress = item.findViewById<View>(R.id.progress_refresh_env) as ProgressBar
        val btnSetting = item.findViewById<View>(R.id.btn_setting_monitor) as ImageButton

    }

    private fun getLastEnvironmentParam(view: View, nodeEnv: String, serviceTag: String, holder: ViewHolder) {

        val input = NewEnvironmentParamsInput.builder().params(listOf("1", "2", "3", "4"))
                .nodeEnv(nodeEnv)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                NewEnviromentParamsQuery.builder().params(input)
                        .build()
        ).enqueue(object : ApolloCall.Callback<NewEnviromentParamsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getSensor", e.message)
                holder.progress.visibility = View.INVISIBLE
                holder.btnRefresh.visibility = View.VISIBLE
            }

            override fun onResponse(response: Response<NewEnviromentParamsQuery.Data>) {
                activity.runOnUiThread {
                    val error = response.errors()
                    if (error.isEmpty()) {
                        getSensorData(view, serviceTag, nodeEnv, holder)
                    } else {
                        Snackbar.make(view, error[0].message()!!, Snackbar.LENGTH_LONG).show()
                        holder.progress.visibility = View.INVISIBLE
                        holder.btnRefresh.visibility = View.VISIBLE
                    }


                }
            }
        })
    }


    private fun getSensorData(view: View, serviceTag: String, nodeEnv: String, holder: MonitorAdapter.ViewHolder) {
        val input = AllSensorsInput.builder().serviceTag(serviceTag).nodeEnv(nodeEnv).build()
        MyApolloClient.getApolloClient().query(
                AllSensorsQuery.builder().params(input)
                        .build()
        ).enqueue(object : ApolloCall.Callback<AllSensorsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getSensor", e.message)
                holder.progress.visibility = View.INVISIBLE
                holder.btnRefresh.visibility = View.VISIBLE
            }

            override fun onResponse(response: Response<AllSensorsQuery.Data>) {
                activity.runOnUiThread {
                    val error = response.errors()
                    if (error.isEmpty()) {
                        val layoutManager =GridLayoutManager(context, 4)
                        holder.recyclerViewSensor.layoutManager = layoutManager
                        val adapter = SensorAdapter(response.data()!!.allSensors()!!, context, activity, holder.recyclerViewSensor)
                        holder.recyclerViewSensor.adapter = adapter
                        holder.progress.visibility = View.INVISIBLE
                        holder.btnRefresh.visibility = View.VISIBLE
                    } else {
                        holder.progress.visibility = View.INVISIBLE
                        holder.btnRefresh.visibility = View.VISIBLE
                        Snackbar.make(view, error[0].message()!!, Snackbar.LENGTH_LONG).show()

                    }
                }
            }
        })
    }
}