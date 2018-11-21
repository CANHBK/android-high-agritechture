package com.example.administrator.glasshouse.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.AddNodeControlMutation
import com.example.administrator.glasshouse.MainActivity
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.NodeControlInput

/**
 * A simple [Fragment] subclass.
 *
 */
class AddRelayFragment : Fragment() {

    lateinit var mShared : SharedPreferences
    lateinit var idGate : String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_relay, container, false)
        val relayName = view.findViewById<EditText>(R.id.edtRelayName)
        val relayID = view.findViewById<EditText>(R.id.edtRelayID)
        val getQRRelay = view.findViewById<ImageView>(R.id.imgScanRelay)
        val btnAdd = view.findViewById<View>(R.id.btnSubRelay) as Button

        mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        idGate = mShared.getString(Config.GateId, "")!!


        btnAdd.setOnClickListener {
            val relay: String = relayName.text.toString()
            val relayId = relayID.text.toString()

            if (!TextUtils.isEmpty(relay) && !TextUtils.isEmpty(relayId)) {
                addRelayNode(relayId, idGate,relay)
            } else {
                Toast.makeText(context!!,"Fill in all information",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun addRelayNode(relayID: String, idGate: String, relayName : String) {
        val input = NodeControlInput.builder().nodeControl(relayID).serviceTag(idGate)
                .name(relayName).build()
        MyApolloClient.getApolloClient().mutate(
                AddNodeControlMutation.builder().params(input).build()

        ).enqueue(object : ApolloCall.Callback<AddNodeControlMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!addSensor", e.message)
            }

            override fun onResponse(response: Response<AddNodeControlMutation.Data>) {
                activity!!.runOnUiThread {
                    val successCheck = response.data()!!.addNodeControl()
                    if (successCheck != null) {
                        Toast.makeText(context!!, "Add Node Successfully", Toast.LENGTH_LONG).show()
                        val intent = Intent(context!!,MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context!!, response.errors()[0].message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }


}
