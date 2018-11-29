package com.example.administrator.glasshouse.Fragment


import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.administrator.glasshouse.Adapter.MonitorAdapter
import com.example.administrator.glasshouse.AutoUpdatedEnvironmentSubSubscription
import com.example.administrator.glasshouse.GetAllNodeEnvQuery
//import com.example.administrator.glasshouse.GetAllNodeEnvQuery

import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.type.NodeEnvInput
import com.example.administrator.glasshouse.type.ServiceInput
import io.paperdb.Paper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class MonitorFragment : Fragment() {
    lateinit var overViewAdapter: MonitorAdapter
    lateinit var recycle_overview: RecyclerView

    var realTimeParam: AutoUpdatedEnvironmentSubSubscription.AutoUpdatedEnvironmentParams = AutoUpdatedEnvironmentSubSubscription.AutoUpdatedEnvironmentParams(
            "", null, null, null, null, null, null, null
    )
    var compositeDisposable: CompositeDisposable? = null


    lateinit var mShared: SharedPreferences
    lateinit var serviceTag: String
    lateinit var userID: String
    lateinit var nodeEnvList: ArrayList<GetAllNodeEnvQuery.AllNodesEnv>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitor, container, false)
        serviceTag = arguments?.getString(config.SERVICE_TAG_BUNDLE)!!
        nodeEnvList = ArrayList()


        userID = Paper.book().read(config.USER_ID_KEY)

//         Hàm get dữ liệu từ server về
        getNodeEnvData(view, userID, serviceTag)


        recycle_overview = view.findViewById(R.id.recycler_Overview) as RecyclerView
        recycle_overview.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)

        compositeDisposable = CompositeDisposable()
//        trackNewEnvParam()


        return view
    }


//    private fun trackNewEnvParam() {
//        val input = NodeEnvInput.builder().serviceTag("G001").nodeEnv("E-001-F:0").build()
//        Log.d("!input", input.toString())
//        val newEnvSub = AutoUpdatedEnvironmentSubSubscription.builder().params(input).build()
//        val envSubscriptionClient = MyApolloClient.getApolloClient().subscribe(newEnvSub)
//        // Sử dụng RxJava để tiện xử lý sự kiện
//        compositeDisposable!!.add(Rx2Apollo.from(envSubscriptionClient)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSubscriber<Response<AutoUpdatedEnvironmentSubSubscription.Data>>() {
//                    override fun onComplete() {
//                        //super.onComplete()
//                        Log.d("!subOnCompelete", "onComplete")
//                    }
//
//                    override fun onError(t: Throwable?) {
//                        //super.onError(t)
//                        Log.d("!subOnError", t!!.message)
//                    }
//
//                    override fun onNext(response: Response<AutoUpdatedEnvironmentSubSubscription.Data>) {
//                        val data = response.data()!!.autoUpdatedEnvironmentParams()!!;
//                        realTimeParam = data
////                        realTimeParam = RealTimeParam(data.serviceTag()!!,data.nodeEnv()!!,
////                                data.name()!!,data.temperatures()!![0],data.lights()!![0],data.airHumidities()!![0],
////                                data.groundHumidities()!![0])
//                        overViewAdapter = MonitorAdapter(nodeEnvList, data, context!!,activity!!)
//                        recycle_overview.adapter = overViewAdapter
//                        overViewAdapter.notifyDataSetChanged()
//                        Log.d("!subOnNext", response.data().toString())
//                    }
//                })
//        )
//    }

    private fun getNodeEnvData(view: View, userID: String, serviceTag: String) {

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
                activity!!.runOnUiThread {
                    val result = response.data()!!.allNodesEnv()!!
                    if (result.isEmpty()) {

                        Snackbar.make(view, "Hiện tại chưa quản lý Node Sensor nào!!", Snackbar.LENGTH_LONG).show()
                    }
                    overViewAdapter = MonitorAdapter(result, realTimeParam, context!!,activity!!)
                    recycle_overview.adapter = overViewAdapter


                    overViewAdapter.notifyDataSetChanged();


                }
            }
        })
    }
}
