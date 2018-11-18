package com.example.administrator.glasshouse.Fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.glasshouse.Adapter.RelayAdapter
import com.example.administrator.glasshouse.Model.RelayData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.Utils.Config


/**
 * A simple [Fragment] subclass.
 *
 */
class RelayFragment : Fragment() {

    lateinit var relayListRelay : ArrayList<RelayData>
    lateinit var mShared : SharedPreferences
    lateinit var serviceTag : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_relay, container, false)
        val relayAll = view.findViewById<View>(R.id.recyclerNodeRelay) as RecyclerView

        relayListRelay  = ArrayList()

        // Cách để xử lý dữ liệu trong mỗi item của RecyclerView
        // Tìm hiểu thêm để có thể xử lý
        // Dùng interface(recommended by cảnh)
        // Databinding to reduce adapter logic
        // reduce code logic in RecyclerView Adapter( problem solved )
        mShared  = context!!.getSharedPreferences(Config.SharedCode,Context.MODE_PRIVATE)
        serviceTag = mShared.getString(Config.GateId,"")!!

        // Hàm get tất cả các node Relay
        //getRelayData()

        relayListRelay.add(RelayData(1,"Node Relay 1",true,false))
        relayListRelay.add(RelayData(2,"Node Relay 2",false,false))
        relayListRelay.add(RelayData(3,"Node Relay 3",false,true))
        relayListRelay.add(RelayData(4,"Node Relay 4",true,true))

        relayAll.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        relayAll.adapter = RelayAdapter(relayListRelay,context!!)

        return view
    }

//    private fun getRelayData() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }


}
