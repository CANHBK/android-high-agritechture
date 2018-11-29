package com.example.administrator.glasshouse.Fragment


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.ControlAdapter
import com.example.administrator.glasshouse.GetAllNodeControlQuery
import com.example.administrator.glasshouse.model.RelayData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.type.ServiceInput
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_control.*


class ControlFragment : Fragment() {


    lateinit var listRelay: ArrayList<RelayData>
    lateinit var serviceTag: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_control, container, false)
        serviceTag = arguments!!.getString(config.SERVICE_TAG_BUNDLE)!!
        listRelay = ArrayList()
        // Hàm get tất cả các node Relay
        getNodeControl(view,serviceTag)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout_control.setOnRefreshListener {
           Log.d("!callll","call")
        }
    }


    private fun getNodeControl(view: View,serviceTag:String) {
        val userId = Paper.book().read<String>(config.USER_ID_KEY);
        val input = ServiceInput.builder().userId(userId)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                GetAllNodeControlQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<GetAllNodeControlQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getControl", e.message)
            }

            override fun onResponse(response: Response<GetAllNodeControlQuery.Data>) {
                activity!!.runOnUiThread {
                    val data = response.data()
                    val error = response.errors()
                    if (data != null) {
                        val allNodeControl = data.allNodeControl()!!
                        Log.d("!controlnode", data.toString())
                        val relayAll = view.findViewById<View>(R.id.recyclerNodeRelay) as RecyclerView
                        relayAll.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
                        relayAll.adapter = ControlAdapter(allNodeControl, context!!,activity!!)
                    }

                    if (error.size != 0) {
                        val errorMess = error[0].message()!!;
                        Snackbar.make(view, errorMess, Snackbar.LENGTH_LONG).show()
                    }
                }

            }

        })
    }


}
