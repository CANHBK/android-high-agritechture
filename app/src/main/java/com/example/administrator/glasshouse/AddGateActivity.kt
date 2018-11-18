package com.example.administrator.glasshouse

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.FarmInput
import kotlinx.android.synthetic.main.activity_add_farm.*

class AddGateActivity : AppCompatActivity() {

    lateinit var mShared : SharedPreferences
    lateinit var id : String
    lateinit var IdGate : String
    override fun onStart() {
        super.onStart()
        Log.d("!check","OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("!check","OnResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("!check","OnDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d("!check","OnPause")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("!check","OnCreate")
        setContentView(R.layout.activity_add_farm)
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
            addGate(id,idGate)
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
    private fun addGate(id: String, idGate: String) {
        MyApolloClient.getApolloClient().mutate(
                AddGateMutation.builder()
                        .userID(id)
                        .idGate(idGate)
                        .build()
        ).enqueue(object : ApolloCall.Callback<AddGateMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!add", e.message)
            }

            override fun onResponse(response: Response<AddGateMutation.Data>) {
                //Bắt lỗi các trường hợp
                Log.d("!add", response.data()!!.addGateWay()!!.serviceTag)
                this@AddGateActivity.runOnUiThread{
                    Toast.makeText(this@AddGateActivity,"Add Gate Succesfully",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
