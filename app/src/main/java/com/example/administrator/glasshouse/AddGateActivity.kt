package com.example.administrator.glasshouse

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.ServiceInput
import kotlinx.android.synthetic.main.activity_add_gate.*

class AddGateActivity : AppCompatActivity() {

    lateinit var mShared : SharedPreferences
    lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("!check","OnCreate")
        setContentView(R.layout.activity_add_gate)
        mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        id = mShared.getString(Config.UserId, "Nope")!!

        // set up toolbar và nút back
        setSupportActionBar(tbAddFarm)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btnAddFarmCancel.setOnClickListener {
            sendToFarmChange()
        }
        btnAddFarmOk.setOnClickListener {
            val name = edtFarmName.text.toString()
            val idGate = edtIDGate.text.toString()
            if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(idGate)){
                // Làm việc với bên Server để thêm tên Gate
                addGate(it,idGate,name)
            } else {
                Snackbar.make(it,"All information must be filled",Snackbar.LENGTH_LONG).show()
            }
        }
        imgQRcode.setOnClickListener { qrScan() }
    }

    private fun sendToFarmChange() {
        val intent = Intent(this@AddGateActivity,FarmChangeActivity::class.java)
        startActivity(intent)
    }

    private fun qrScan() {
        val dialog = Dialog(this@AddGateActivity)
        dialog.setContentView(R.layout.get_qr_code)


        dialog.show()
    }

    // Hàm khai báo khởi tạo Farm lên Server
    private fun addGate(view: View, idGate: String, name:String) {
        val input = ServiceInput.builder()
                .serviceTag(idGate)
                .name(name)
                .userId(id).build()
        Log.d("!info","$id $idGate $name")
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
                    Log.d("!add", response.data()!!.addGate()!!.serviceTag)
                    this@AddGateActivity.runOnUiThread{
                        Snackbar.make(view,"Add Gate Succesfully",Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this@AddGateActivity,FarmChangeActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    this@AddGateActivity.runOnUiThread{
                    Snackbar.make(view,response.errors()[0].message()!!,Snackbar.LENGTH_SHORT).show()
                }}
            }
        })
    }
}
