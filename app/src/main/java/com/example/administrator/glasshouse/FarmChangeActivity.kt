package com.example.administrator.glasshouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.FarmChangeAdapter
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import kotlinx.android.synthetic.main.activity_area_change.*
import kotlinx.android.synthetic.main.bottom_sheet.*


class FarmChangeActivity : AppCompatActivity() {

    val listFarm: ArrayList<String> = ArrayList()
    lateinit var mShared : SharedPreferences
    lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_change)

        // Nhận ID người dùng từ SignIn/Sign Up
        mShared = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        id = mShared.getString(Config.UserId, "")!!
        Log.d("!id", id)
        settingBottomSheet()


        getGateData(id)
        //Nếu có Data thì đổ lên RecyclerView đồng thời ableNoFarmViewGroup(false)


        // Nếu như Data rỗng thì hiện thị NoFarmView và set sự kiện Click cho imgAddFarm để add Farm
        imgAddFarm.setOnClickListener { it ->
            sendToAddGate()
        }

        // Có thể thêm Farm ngay cả khi đã có những Farm trước đấy r
        fabBottomSheet.setOnClickListener {
            sendToAddGate()
        }

    }

    private fun sendToAddGate() {
        val intent = Intent(this@FarmChangeActivity,AddGateActivity::class.java)
        startActivity(intent)
    }

    // hiện thị dialog để addFarm





    // hàm Query lấy dữ liệu các Farm theo UserID
    private fun getGateData(id: String) {
        MyApolloClient.getApolloClient().query(
                GetUserByIDQuery.builder()
                        .id(id)
                        .build()
        ).enqueue(object : ApolloCall.Callback<GetUserByIDQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!get", e.message)
            }

            override fun onResponse(response: Response<GetUserByIDQuery.Data>) {
//                Log.d("!get", response.data()!!.farms()!![0].name())
                Log.d("!here", "Có vào đây")

                if (!response.data()!!.user()!!.gatePermission!!.isEmpty()) {
                    this@FarmChangeActivity.runOnUiThread {
                        // Lấy dữ liệu và đổ vào RecyclerView
                        val list = response.data()!!.user!!.gatePermission!!
                        for (i in 0..(list.size - 1)) {
                            listFarm.add(list[i].serviceTag()!!)
                            //Toast.makeText(this@FarmChangeActivity,"NotHere",Toast.LENGTH_LONG).show()
                            val layoutManager = GridLayoutManager(this@FarmChangeActivity, 3)
                            recy_changeArea.layoutManager = layoutManager
                            val adapter = FarmChangeAdapter(listFarm, this@FarmChangeActivity)
                            recy_changeArea.adapter = adapter
                        }
                    }
                } else { // không có dữ liệu sẽ nhảy vào đây
                    this@FarmChangeActivity.runOnUiThread {
                        //Toast.makeText(this@FarmChangeActivity,"Here",Toast.LENGTH_LONG).show()
                        ableNoFarmViewGroup(true)
                    }

                }
            }
        })
    }

    // Check lại hàm disable ViewGroup
    private fun ableNoFarmViewGroup(status: Boolean) {
        val layout: LinearLayout = findViewById(R.id.noFarmView)
        val bottomSheet: LinearLayout = findViewById(R.id.bottom_sheet)
        txtChooseFarm.visibility = View.INVISIBLE
        bottomSheet.visibility = View.INVISIBLE

        // Xem cách cài thuộc tính của
        val p = fabBottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        p.anchorId = View.NO_ID
        fabBottomSheet.layoutParams = p
        fabBottomSheet.hide()
        for (i in 0..(layout.childCount - 1)) {
            val child = layout.getChildAt(i)
            child.visibility = View.VISIBLE
            child.isEnabled = status
        }
        for (i in 0..(bottomSheet.childCount - 1)) {
            val child = bottomSheet.getChildAt(i)
            child.visibility = View.INVISIBLE
            child.isEnabled = !status
        }

    }

    // Cấu hình cho Bottom Sheet
    private fun settingBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.isFitToContents = true
        bottomSheetBehavior.isHideable = false
    }
}
