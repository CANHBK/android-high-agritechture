package com.example.administrator.glasshouse


import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.R.id.*
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.type.ServiceInput
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_add_gate_way.*


class AddGateWayFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_gate_way, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_gate_way.setOnClickListener {
            val name = txt_name_gate_way.text.toString()
            val serviceTag = txt_service_tag.text.toString()
            if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(serviceTag)){

                addGate(it,name,serviceTag)
            } else {
                Snackbar.make(it,"All information must be filled", Snackbar.LENGTH_LONG).show()
            }
        }

        btn_cancel_add_gate_way.setOnClickListener {
            it.findNavController().navigate(R.id.action_addGateWayFragment_to_homeFragment)
        }

    }

    private fun addGate(view: View,name:String,servieTag:String){
        val id = Paper.book().read<String>(config.USER_ID_KEY);
        val input = ServiceInput.builder()
                .serviceTag(servieTag)
                .name(name)
                .userId(id).build()

        MyApolloClient.getApolloClient().mutate(
                AddGateMutation.builder()
                        .params(input)
                        .build()
        ).enqueue(object : ApolloCall.Callback<AddGateMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!add", e.message)
            }

            override fun onResponse(response: Response<AddGateMutation.Data>) {
                //Bắt lỗi các trường hợp
                val check = response.data()!!.addGate()
                if (check != null){
                    activity!!.runOnUiThread{
                        view.findNavController().navigate(R.id.action_addGateWayFragment_to_homeFragment)
                    }
                } else {
                    activity!!.runOnUiThread{
                        Snackbar.make(view,response.errors()[0].message()!!, Snackbar.LENGTH_SHORT).show()
                    }}
            }
        })
    }

}
