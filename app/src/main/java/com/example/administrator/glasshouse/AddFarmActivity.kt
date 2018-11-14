package com.example.administrator.glasshouse

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.type.FarmInput
import kotlinx.android.synthetic.main.activity_add_farm.*

class AddFarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_farm)
        val mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val id = mShared.getString(Config.UserId, "")

        // set up toolbar và nút back
        setSupportActionBar(tbAddFarm)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btnAddFarmCancel.setOnClickListener {
            sendToFarmChange()
        }
        btnAddFarmOk.setOnClickListener {
            val name = edtFarmName.text.toString()
            addFarm(id!!,name)
        }
        imgQRcode.setOnClickListener { qrScan() }
    }

    private fun sendToFarmChange() {
        val intent = Intent(this@AddFarmActivity,FarmChangeActivity::class.java)
        startActivity(intent)
    }

    private fun qrScan() {
        val dialog = Dialog(this@AddFarmActivity)
        dialog.setContentView(R.layout.get_qr_code)


        dialog.show()
    }

    // Hàm khai báo khởi tạo Farm lên Server
    private fun addFarm(id: String, name: String) {
        val input = FarmInput.builder().idHost(id)
                .name(name).build()
        MyApolloClient.getApolloClient().mutate(
                AddFarmMutation.builder()
                        .data(input)
                        .build()
        ).enqueue(object : ApolloCall.Callback<AddFarmMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!add", e.message)
            }

            override fun onResponse(response: Response<AddFarmMutation.Data>) {
                Log.d("!add", response.data()!!.addFarm()!!.name())
            }
        })
    }
}
