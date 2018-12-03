package com.example.administrator.glasshouse.Fragment


import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.AddNodeControlMutation
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.type.NodeControlInput
import kotlinx.android.synthetic.main.fragment_add_relay.*

class AddRelayFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_relay, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val relayName = view.findViewById<EditText>(R.id.edtRelayName)
        val relayID = view.findViewById<EditText>(R.id.edtRelayID)
        val btnAdd = view.findViewById<View>(R.id.btnSubRelay) as Button



        btnAdd.setOnClickListener {
            val idGate = edt_service_tag.text.toString()
            val relay: String = relayName.text.toString()
            val relayId = relayID.text.toString()

            if (!TextUtils.isEmpty(relay) && !TextUtils.isEmpty(relayId) && !TextUtils.isEmpty(idGate)) {
                addRelayNode(it, relayId, idGate, relay)
            } else {
                Snackbar.make(it, "Các trường không được trống", Snackbar.LENGTH_SHORT).show()
            }
        }

        btn_cancel_add_control.setOnClickListener {
            it.findNavController().navigate(R.id.action_addRelayFragment_to_homeFragment)
        }
    }

    private fun addRelayNode(view: View, relayID: String, idGate: String, relayName: String) {
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
                    val errros = response.errors()
                    if (errros.isEmpty()){
                        Snackbar.make(view, "Thêm thành công", Snackbar.LENGTH_LONG).show()
                        view.findNavController().navigate(R.id.action_addRelayFragment_to_homeFragment)
                    }else{
                        Snackbar.make(view, response.errors()[0].message()!!, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }


}
