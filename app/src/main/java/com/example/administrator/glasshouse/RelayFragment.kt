package com.example.administrator.glasshouse


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.administrator.glasshouse.Adapter.RelayAdapter
import com.example.administrator.glasshouse.ModelTest.DataCheck_relay


/**
 * A simple [Fragment] subclass.
 *
 */
class RelayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_relay, container, false)
        val relayAll = view.findViewById<View>(R.id.recyclerNodeRelay) as RecyclerView

        // Cách để xử lý dữ liệu trong mỗi item của RecyclerView
        // Tìm hiểu thêm để có thể xử lý
        // Dùng interface(recommended by cảnh)
        // Databinding to reduce adapter logic
        // ruduce code logic in RecyclerView Adapter

        val relayList : ArrayList<DataCheck_relay> = ArrayList()
        relayList.add(DataCheck_relay("Node Relay 1",true,false))
        relayList.add(DataCheck_relay("Node Relay 2",false,false))
        relayList.add(DataCheck_relay("Node Relay 3",false,true))
        relayList.add(DataCheck_relay("Node Relay 4",true,true))


        relayAll.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        relayAll.adapter = RelayAdapter(relayList,context!!)

        return view
    }


}
