package com.example.administrator.glasshouse.ui.monitor

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.*

import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.di.Injectable

import com.example.administrator.glasshouse.type.ServiceInput
import com.example.administrator.glasshouse.ui.control.ControlAdapter
import com.example.administrator.glasshouse.ui.farm.FarmAdapter
import com.example.administrator.glasshouse.ui.farm.FarmControlAdapter
import com.example.administrator.glasshouse.vo.Farm
import io.paperdb.Paper

import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_monitor.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject

class MonitorFragment : Fragment(), Injectable {
    lateinit var overViewAdapter: MonitorAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var apolloClient: ApolloClient

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

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


        compositeDisposable = CompositeDisposable()
//        trackNewEnvParam()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_from_gate.setOnClickListener {
            it.findNavController().popBackStack()
        }
//        getNodeControl(view, serviceTag)
        overViewAdapter = MonitorAdapter(dataBindingComponent, appExecutors)
        recycler_Overview.adapter = overViewAdapter
    }


//    private fun getNodeEnvData(view: View, userID: String, serviceTag: String) {
//
//        val input = ServiceInput.builder()
//                .userId(userID)
//                .serviceTag(serviceTag).build()
//        apolloClient.query(
//                GetAllNodeEnvQuery.builder().params(input)
//                        .build()
//        ).httpCachePolicy(HttpCachePolicy.CACHE_FIRST).enqueue(object : ApolloCall.Callback<GetAllNodeEnvQuery.Data>() {
//            override fun onFailure(e: ApolloException) {
//                Log.d("!monifragment", e.toString())
//            }
//
//            override fun onResponse(response: Response<GetAllNodeEnvQuery.Data>) {
//                activity!!.runOnUiThread {
//                    val result = response.data()!!.allNodesEnv()!!
//                    if (result.isEmpty()) {
//
//                        Snackbar.make(view, "Hiện tại chưa quản lý Node Sensor nào!!", Snackbar.LENGTH_LONG).show()
//                    }
//                    overViewAdapter = MonitorAdapter(result, recycle_overview, realTimeParam, context!!, activity!!)
//                    recycle_overview.adapter = overViewAdapter
//
//
//                    overViewAdapter.notifyDataSetChanged();
//
//
//                }
//            }
//        })
//    }

    private fun getNodeControl(view: View, serviceTag: String) {
        val userId = Paper.book().read<String>(config.USER_ID_KEY);
        val input = ServiceInput.builder().userId(userId)
                .serviceTag(serviceTag).build()
        MyApolloClient.getApolloClient().query(
                GetAllNodeControlQuery.builder().params(input).build()
        ).enqueue(object : ApolloCall.Callback<GetAllNodeControlQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!getControl", e.message)
            }

            override fun onResponse(response: Response<GetAllNodeControlQuery.Data>) {
                activity!!.runOnUiThread {
                    val data = response.data()
                    val error = response.errors()
                    if (data != null) {
                        val allNodeControl = data.allNodeControl()!!
                        Log.d("!controlnode", data.toString())
                        val relayAll = view.findViewById<View>(R.id.recyclerNodeRelay) as androidx.recyclerview.widget.RecyclerView
                        relayAll.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!, androidx.recyclerview.widget.RecyclerView.VERTICAL, false)
                        relayAll.adapter = ControlAdapter(allNodeControl, context!!, activity!!)
                    }

                    if (error.size != 0) {
                        val errorMess = error[0].message()!!;
                        Snackbar.make(view, errorMess, Snackbar.LENGTH_LONG).show()
                    }
                }

            }

        })
    }
}
