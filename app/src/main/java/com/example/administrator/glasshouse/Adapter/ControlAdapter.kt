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
import android.widget.TextView
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.AllRelayOfControlQuery
import com.example.administrator.glasshouse.GetAllNodeControlQuery
import com.example.administrator.glasshouse.GetCurrentStateRelayQuery
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.model.ControlModel
import com.example.administrator.glasshouse.model.RelayModel
import com.example.administrator.glasshouse.type.NodeControlInput
import com.example.administrator.glasshouse.type.RelayInput
import com.example.administrator.glasshouse.type.ServiceInput
import io.paperdb.Paper


class ControlAdapter(val allNodeControl: List<GetAllNodeControlQuery.AllNodeControl>, val context: Context, val activity: Activity) : RecyclerView.Adapter<ControlAdapter.RelayViewholder>() {
    lateinit var view: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelayViewholder {
        val inflater = LayoutInflater.from(parent.context)
        view = inflater.inflate(R.layout.item_relay, parent, false)

        return RelayViewholder(view)
    }

    override fun getItemCount(): Int {
        return allNodeControl.size
    }

    override fun onBindViewHolder(holder: RelayViewholder, position: Int) {

        val nodeControl = allNodeControl[position]
        val name = nodeControl.name()!!
        val relayTag = nodeControl.nodeControl()
        allRelays(view, nodeControl.serviceTag(), nodeControl.nodeControl(), holder)
        holder.title.text = name
        holder.subTitle.text = relayTag
        holder.btnRefresh.setOnClickListener {
            holder.progressRefresh.visibility = View.VISIBLE
            holder.btnRefresh.visibility = View.INVISIBLE
            getCurrentStateRelay(it, nodeControl.serviceTag(), nodeControl.nodeControl(), holder, position);
        }
        holder.btnConfigTime.setOnClickListener {

            val data = Bundle()
            val control = ControlModel(nodeControl.serviceTag(), nodeControl.nodeControl())
            val relays = ArrayList<RelayModel>()
            for (item in nodeControl.relays()) {
                val rl = RelayModel(item.index(), item.name())
                relays.add(rl)
            }
            data.putSerializable("control", control)
            data.putSerializable("relays", relays)
            it.findNavController().navigate(R.id.action_controlFragment_to_configTimeControlFragment, data)
        }

    }

    private fun getCurrentStateRelay(view: View, serviceTag: String, nodeControl: String, holder: RelayViewholder, position: Int) {
        val input = RelayInput.builder().index(listOf(1, 2, 3, 4)).nodeControl(nodeControl)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                GetCurrentStateRelayQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<GetCurrentStateRelayQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getControl", e.message)
                holder.progressRefresh.visibility = View.INVISIBLE
                holder.btnRefresh.visibility = View.VISIBLE
            }

            override fun onResponse(response: Response<GetCurrentStateRelayQuery.Data>) {
                activity.runOnUiThread {


                    Log.d("!test", response.toString())
                    val data = response.data()
                    val error = response.errors()
                    if (data != null) {
                        allRelays(view, serviceTag, nodeControl, holder)
                    }

                    if (error.size != 0) {
                        val errorMess = error[0].message()!!;
                        holder.progressRefresh.visibility = View.INVISIBLE
                        holder.btnRefresh.visibility = View.VISIBLE
                        Snackbar.make(view, errorMess, Snackbar.LENGTH_LONG).show()
                    }
                }

            }

        })
    }


    inner class RelayViewholder(item: View) : RecyclerView.ViewHolder(item) {
        val recyclerViewBtn = item.findViewById<View>(R.id.recycler_btn_on_off) as RecyclerView
        val title = item.findViewById<View>(R.id.txt_name_node_control) as TextView
        val subTitle = item.findViewById<View>(R.id.txt_id_node_control) as TextView
        val btnRefresh = item.findViewById<View>(R.id.refresh_node_control)
        val progressRefresh = item.findViewById<View>(R.id.progress_refresh)
        val btnConfigTime = item.findViewById<View>(R.id.btn_config_time_control) as ImageButton
        val view = item;


    }

    private fun allRelays(view: View, serviceTag: String, nodeControl: String, holder: RelayViewholder) {
        val input = NodeControlInput.builder().nodeControl(nodeControl)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                AllRelayOfControlQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<AllRelayOfControlQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getControl", e.message)
                holder.progressRefresh.visibility = View.INVISIBLE
                holder.btnRefresh.visibility = View.VISIBLE
            }

            override fun onResponse(response: Response<AllRelayOfControlQuery.Data>) {
                activity.runOnUiThread {
                    val data = response.data()
                    val error = response.errors()
                    if (data != null) {
                        val layoutManager = GridLayoutManager(context, 2)
                        holder.recyclerViewBtn.layoutManager = layoutManager
                        val adapter = RelayAdapter(data.allRelaysOfControl()!!, context, holder.recyclerViewBtn, activity)
                        holder.recyclerViewBtn.adapter = adapter
                        holder.progressRefresh.visibility = View.INVISIBLE
                        holder.btnRefresh.visibility = View.VISIBLE
                    }

                    if (error.size != 0) {
                        val errorMess = error[0].message()!!;
                        holder.progressRefresh.visibility = View.INVISIBLE
                        holder.btnRefresh.visibility = View.VISIBLE
                        Snackbar.make(view, errorMess, Snackbar.LENGTH_LONG).show()
                    }
                }

            }

        })
    }


}