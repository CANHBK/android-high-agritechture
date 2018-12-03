package com.example.administrator.glasshouse.Fragment


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.AddNodeEnvironmentMutation

import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.type.NodeEnvInput
import kotlinx.android.synthetic.main.fragment_add_sensor.*


class AddSensorFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_sensor, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sensorName = view.findViewById<EditText>(R.id.edtSensorName)
        val sensorID = view.findViewById<EditText>(R.id.edtSensorID)
        val btnAdd = view.findViewById<View>(R.id.btnSubSensor) as Button


        btnAdd.setOnClickListener {
            val ssName: String = sensorName.text.toString()
            val ssID = sensorID.text.toString()
            val idGate = edt_service_tag.text.toString()

            if (!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(ssID)) {
                addSensorNode(view, ssID, idGate, ssName)
            } else {
                Snackbar.make(it, "Không được trống", Snackbar.LENGTH_SHORT).show()
            }
        }
        btn_cancel_add_node_env.setOnClickListener {
            it.findNavController().navigate(R.id.action_addSensorFragment_to_homeFragment)
        }
    }

    private fun addSensorNode(view: View, IDNode: String, IDGate: String, sensorName: String) {

        val input = NodeEnvInput.builder().nodeEnv(IDNode).serviceTag(IDGate).name(sensorName).build()
        MyApolloClient.getApolloClient().mutate(
                AddNodeEnvironmentMutation.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<AddNodeEnvironmentMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!addSensor", e.message)
            }

            override fun onResponse(response: Response<AddNodeEnvironmentMutation.Data>) {
                //trả về errors[...]
                val errors = response.errors();
                if (errors.isEmpty()) {
                    view.findNavController().navigate(R.id.action_addSensorFragment_to_homeFragment)
                } else {
                    //Lỗi trả về từ Server
                    val error = response.errors().toTypedArray()[0].message()!!
                    return Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()
                }

            }

        })
    }


}
