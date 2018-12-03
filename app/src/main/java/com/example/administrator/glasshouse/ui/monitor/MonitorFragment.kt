package com.example.administrator.glasshouse.ui.monitor


import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.administrator.glasshouse.Adapter.MonitorAdapter
import com.example.administrator.glasshouse.AutoUpdatedEnvironmentSubSubscription
import com.example.administrator.glasshouse.GetAllNodeEnvQuery

import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.api.GraphQL
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.di.Injectable

import com.example.administrator.glasshouse.type.ServiceInput
import io.paperdb.Paper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class MonitorFragment : androidx.fragment.app.Fragment(), Injectable {
    lateinit var overViewAdapter: MonitorAdapter
    lateinit var recycle_overview: androidx.recyclerview.widget.RecyclerView

    @Inject
    lateinit var apolloClient: ApolloClient

    var realTimeParam: AutoUpdatedEnvironmentSubSubscription.AutoUpdatedEnvironmentParams = AutoUpdatedEnvironmentSubSubscription.AutoUpdatedEnvironmentParams(
            "", null, null, null, null, null, null, null
    )
    var compositeDisposable: CompositeDisposable? = null


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


        recycle_overview = view.findViewById(R.id.recycler_Overview) as androidx.recyclerview.widget.RecyclerView

        compositeDisposable = CompositeDisposable()
//        trackNewEnvParam()


        return view
    }


    private fun getNodeEnvData(view: View, userID: String, serviceTag: String) {

        val input = ServiceInput.builder()
                .userId(userID)
                .serviceTag(serviceTag).build()
        apolloClient.query(
                GetAllNodeEnvQuery.builder().params(input)
                        .build()
        ).httpCachePolicy(HttpCachePolicy.CACHE_FIRST).enqueue(object : ApolloCall.Callback<GetAllNodeEnvQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!monifragment", e.toString())
            }

            override fun onResponse(response: Response<GetAllNodeEnvQuery.Data>) {
                activity!!.runOnUiThread {
                    val result = response.data()!!.allNodesEnv()!!
                    if (result.isEmpty()) {

                        Snackbar.make(view, "Hiện tại chưa quản lý Node Sensor nào!!", Snackbar.LENGTH_LONG).show()
                    }
                    overViewAdapter = MonitorAdapter(result, realTimeParam, context!!, activity!!)
                    recycle_overview.adapter = overViewAdapter


                    overViewAdapter.notifyDataSetChanged();


                }
            }
        })
    }
}
