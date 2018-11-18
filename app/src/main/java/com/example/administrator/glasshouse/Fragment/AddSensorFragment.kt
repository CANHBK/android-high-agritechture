package com.example.administrator.glasshouse.Fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.dd.processbutton.iml.ActionProcessButton
import com.example.administrator.glasshouse.AddNodeSensorMutation

import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import kotlinx.android.synthetic.main.fragment_add_sensor.*


/**
 * A simple [Fragment] subclass.
 *
 */
class AddSensorFragment : Fragment() {

    lateinit var mShared: SharedPreferences
    lateinit var idGate: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_sensor, container, false)
        val sensorName = view.findViewById<EditText>(R.id.edtSensorName)
        val sensorID = view.findViewById<EditText>(R.id.edtSensorID)
        val getQR = view.findViewById<ImageView>(R.id.imgScanSensor)
        val btnAdd = view.findViewById<View>(R.id.btnSubSensor) as Button

        mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        idGate = mShared.getString(Config.GateId, "")!!


        btnAdd.setOnClickListener {
            val ssName: String? = sensorName.text.toString()
            val ssID = sensorID.text.toString().toLong()

            if (!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(ssID.toString())) {
                addSensorNode(ssID, idGate)
            } else {
                Toast.makeText(context!!,"Fill in all information",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun addSensorNode(IDNode: Long, IDGate: String) {
        MyApolloClient.getApolloClient().mutate(
                AddNodeSensorMutation.builder().nodeSensorTag(IDNode)
                        .serviceTag(IDGate).build()
        ).enqueue(object : ApolloCall.Callback<AddNodeSensorMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!addSensor", e.message)
            }

            override fun onResponse(response: Response<AddNodeSensorMutation.Data>) {
                activity!!.runOnUiThread {
                    val successCheck = response.data()!!.addNodeSensor()!!.serviceTag()
                    if (successCheck != null) {
                        Toast.makeText(context!!, "Add Node Successfully", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context!!, "Failed!! Please double-check the ID Node", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }


}
