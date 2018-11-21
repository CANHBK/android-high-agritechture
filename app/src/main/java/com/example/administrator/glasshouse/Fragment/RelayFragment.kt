package com.example.administrator.glasshouse.Fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.RelayAdapter
import com.example.administrator.glasshouse.AllRelayOfControlQuery
import com.example.administrator.glasshouse.GetAllNodeControlQuery
import com.example.administrator.glasshouse.Model.NodeControlData
import com.example.administrator.glasshouse.Model.RelayData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.NodeControlInput
import com.example.administrator.glasshouse.type.ServiceInput


/**
 * A simple [Fragment] subclass.
 *
 */
class RelayFragment : Fragment() {

    lateinit var listRelay: ArrayList<RelayData>
    lateinit var mShared: SharedPreferences
    lateinit var serviceTag: String
    lateinit var userID: String
    lateinit var allNodeControl: ArrayList<NodeControlData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_relay, container, false)


        mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        userID = mShared.getString(Config.UserId,"")!!
        serviceTag = mShared.getString(Config.GateId, "")!!
        allNodeControl = ArrayList()
        listRelay = ArrayList()
        // Hàm get tất cả các node Relay
        getNodeControl()

//        if (!allNodeControl.isEmpty()) {
//            for (i in 0..allNodeControl.size)
//                getRelayOfNode(allNodeControl[i].nodeID,allNodeControl[i].controlName)
//        }
        return view
    }

    private fun getRelayOfNode(nodeID: String,name :String) {
        val input = NodeControlInput.builder().serviceTag(serviceTag)
                .nodeControl(nodeID).build()
        MyApolloClient.getApolloClient().query(
                AllRelayOfControlQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<AllRelayOfControlQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getRelay", e.message)
            }

            override fun onResponse(response: Response<AllRelayOfControlQuery.Data>) {
                activity!!.runOnUiThread {
                    val checkResponse = response.data()!!.allRelaysOfControl()
                    if (checkResponse != null) {
                        var b1OnOff = "F"
                        if (checkResponse[0] == null){
                            b1OnOff = checkResponse[0].state()
                        }
                        val b1Status = (b1OnOff == "F")
                        var b2OnOff = "F"
                        if (checkResponse[0] == null){
                            b2OnOff = checkResponse[0].state()
                        }
                        val b2Status: Boolean = (b2OnOff == "F")
                        listRelay.add(RelayData(nodeID,name,b1Status, b2Status))
                        Log.d("!nodeControl",response.data()!!.allRelaysOfControl()!![0].name())
                        val relayAll = view!!.findViewById<View>(R.id.recyclerNodeRelay) as RecyclerView
                        relayAll.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
                        relayAll.adapter = RelayAdapter(listRelay, context!!)
                    } else{
                        //Toast.makeText(context!!,response.errors()[0].message(),Toast.LENGTH_SHORT).show()
                        Log.d("!control","Không có chuỗi gửi về")
                        listRelay.add(RelayData(nodeID,name,false,false))
                        val relayAll = view!!.findViewById<View>(R.id.recyclerNodeRelay) as RecyclerView
                        relayAll.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
                        relayAll.adapter = RelayAdapter(listRelay, context!!)
                    }
                }
            }
        })
    }

    private fun getNodeControl() {
        val input = ServiceInput.builder().userId(userID)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                GetAllNodeControlQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<GetAllNodeControlQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getControl", e.message)
            }
            override fun onResponse(response: Response<GetAllNodeControlQuery.Data>) {
                activity!!.runOnUiThread {
                    val checkResponse = response.data()!!.allNodeControl()
                    if (checkResponse != null) {
                        val listControl = response.data()!!.allNodeControl()!!
                        for (item in listControl){
                            allNodeControl.add(NodeControlData(item.nodeControl(), item.name()!!))
                            getRelayOfNode(item!!.nodeControl(), item.name()!!)
                        }
                    } else {
                        activity!!.runOnUiThread{
                            Toast.makeText(context!!,"Hiện tại chưa quản lý Node Control nào",Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

        })
    }


}
