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
import com.example.administrator.glasshouse.GetAllNodeEnvQuery
import com.example.administrator.glasshouse.Model.MultiRelayData
import com.example.administrator.glasshouse.Model.SensorData
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import com.example.administrator.glasshouse.type.ServiceInput


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
    lateinit var userID: String
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
        userID = mShared.getString(Config.UserId,"")!!
        serviceTag = mShared.getString(Config.GateId, "")!!
        //userID = mShared.getString(Config.UserId,"")

        // Hàm get dữ liệu từ server về
        getSensorData()




        val recycle_multi = view.findViewById(R.id.recycler_multi) as RecyclerView
        recycle_multi.layoutManager = LinearLayoutManager(context!!, LinearLayout.HORIZONTAL, false)
        val multiAdapter = MultiAdapter(multiRelayList)
        recycle_multi.adapter = multiAdapter


        // Đổ dữ liệu khác nhau theo từng ID khác nhau

//        sensorList.add(SensorData("4", "node_1", 20, 30,45, 99, 10))
//        sensorList.add(SensorData("5", "node_2", 30, 40, 55,209, 60))
//        sensorList.add(SensorData("6", "node_3", 21, 50, 65,309, 50))
//        sensorList.add(SensorData(7, "node_4", 22, 60, 75,409, 70))
//        sensorList.add(SensorData(8, "node_5", 24, 70, 86,509, 100))

        multiRelayList.add(MultiRelayData(1, "Quat_1", true))
        multiRelayList.add(MultiRelayData(2, "Quat_2", false))
        multiRelayList.add(MultiRelayData(3, "Quat_3", false))
        multiRelayList.add(MultiRelayData(4, "Quat_4", true))
        multiRelayList.add(MultiRelayData(5, "Quat_5", true))




        return view
    }

    private fun getSensorData() {
        val input = ServiceInput.builder()
                .userId(userID)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                GetAllNodeEnvQuery.builder().params(input)
                        .build()
        ).enqueue(object : ApolloCall.Callback<GetAllNodeEnvQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getSensor", e.message)
            }

            override fun onResponse(response: Response<GetAllNodeEnvQuery.Data>) {
                // Đang check Server
                activity!!.runOnUiThread {
                    val list = response.data()!!.allNodesEnv()
                    if (list == null){
                        // Hiện "Chưa có node sensor nào "
                        //txtNoSensor.visibility = View.VISIBLE
                        Toast.makeText(context!!,"Hiện tại chưa quản lý Node Sensor nào!!",Toast.LENGTH_SHORT).show()
                    } else {
                        for (data in list) {
                            val nodeSensorTag = data.nodeEnv()!!
                            val nodeEnviName = data.name()
                            var temp = "0"
                            var light = "0"
                            var airHumi = "0"
                            var groundHumi = "0"
                            if(data.lastTemperature() !=null){
                                temp = data.lastTemperature()!!.value()!!
                            }
                            if (data.lastLight() != null){
                                light = data.lastLight()!!.value()!!
                            }
                            if (data.lastAirHumidity() != null){
                                airHumi = data.lastAirHumidity()!!.value()!!
                            }
                            if (data.lastGroundHumidity() != null){
                                groundHumi = data.lastGroundHumidity()!!.value()!!
                            }
                            //val battery = data.battery()?.toInt()
                            sensorList.add(SensorData(nodeSensorTag, nodeEnviName!!, temp, light,airHumi, groundHumi, 30))
                            //Lấy thử dữ liệu
                            Log.d("!nodeSensor", nodeSensorTag)
                            //Vẫn đổ dữ liệu mẫu lên
                            val recycle_overview = view!!.findViewById(R.id.recycler_Overview) as RecyclerView
                            recycle_overview.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
                            val overviewAdapter = SensorAdapter(sensorList, context!!)
                            recycle_overview.adapter = overviewAdapter
                        }
                    }
                }
            }
        })
    }
}
