package com.example.administrator.glasshouse.Fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.MultiAdapter
import com.example.administrator.glasshouse.Adapter.SensorAdapter
import com.example.administrator.glasshouse.GetSensorQuery
import com.example.administrator.glasshouse.Model.MultiRelayData
import com.example.administrator.glasshouse.Model.SensorData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import kotlinx.android.synthetic.main.fragment_overview.*


/**
 * A simple [Fragment] subclass.
 *
 */
class SensorFragment : Fragment() {
//    companion object {
//        public fun newInstance(id: Int): SensorFragment {
//            val args = Bundle()
//            args.putInt("parameter", id)
//            val fragment = SensorFragment()
//            fragment.setArguments(args)
//            return fragment
//        }
//    }

    lateinit var mShared: SharedPreferences
    lateinit var serviceTag: String
    lateinit var sensorList: ArrayList<SensorData>
    lateinit var multiRelayList: ArrayList<MultiRelayData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        sensorList = ArrayList()
        multiRelayList = ArrayList()
        // Phần này có thể bỏ đi khi có Server( đã thay thế bằng SharedPreference)
        // Nhận ID người dùng đã có ở SharedPreference
        mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        serviceTag = mShared.getString(Config.GateId, "")!!
        //userID = mShared.getString(Config.UserId,"")

        // Hàm get dữ liệu từ server về
        getSensorData()

        val recycle_multi = view.findViewById(R.id.recycler_multi) as RecyclerView
        val recycle_overview = view.findViewById(R.id.recycler_Overview) as RecyclerView

        recycle_multi.layoutManager = LinearLayoutManager(context!!, LinearLayout.HORIZONTAL, false)
        recycle_overview.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)


        // Đổ dữ liệu khác nhau theo từng ID khác nhau

        sensorList.add(SensorData(4, "node_1", 20, 30,45, 99, 10))
        sensorList.add(SensorData(5, "node_2", 30, 40, 55,209, 60))
        sensorList.add(SensorData(6, "node_3", 21, 50, 65,309, 50))
        sensorList.add(SensorData(7, "node_4", 22, 60, 75,409, 70))
        sensorList.add(SensorData(8, "node_5", 24, 70, 86,509, 100))

        multiRelayList.add(MultiRelayData(1, "Quat_1", true))
        multiRelayList.add(MultiRelayData(2, "Quat_2", false))
        multiRelayList.add(MultiRelayData(3, "Quat_3", false))
        multiRelayList.add(MultiRelayData(4, "Quat_4", true))
        multiRelayList.add(MultiRelayData(5, "Quat_5", true))


        val multiAdapter = MultiAdapter(multiRelayList)
        val overviewAdapter = SensorAdapter(sensorList, context!!)
        recycle_multi.adapter = multiAdapter
        recycle_overview.adapter = overviewAdapter
        return view
    }

    private fun getSensorData() {
        MyApolloClient.getApolloClient().query(
                GetSensorQuery.builder().serviceTag(serviceTag)
                        .build()
        ).enqueue(object : ApolloCall.Callback<GetSensorQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getSensor", e.message)
            }

            override fun onResponse(response: Response<GetSensorQuery.Data>) {
                // Đang check Server
                activity!!.runOnUiThread {
                    val list = response.data()!!.gateWay()!!.nodeSensor()
                    if (list == null){
                        // Hiện "Chưa có node sensor nào "
                        //txtNoSensor.visibility = View.VISIBLE
                        Toast.makeText(context!!,"Hiện tại chưa quản lý Node Sensor nào!!",Toast.LENGTH_SHORT).show()
                    } else {
                        for (data in list) {
                            val nodeSensorTag = data.nodeSensorTag()!!.toInt()
                            val temp = data.temperature()?.toInt()
                            val airHumi = data.humidity()?.toInt()
                            val light = data.light()?.toInt()
                            //val battery = data.battery()?.toInt()
                            sensorList.add(SensorData(nodeSensorTag, "Node", temp, airHumi,30, light, 30))
                            //Lấy thử dữ liệu
                            //Vẫn đổ dữ liệu mẫu lên
                            // Gửi dữ liệu ở Adapter sang setting Sensor node
                        }
                    }
                }
            }

        })
    }


}
