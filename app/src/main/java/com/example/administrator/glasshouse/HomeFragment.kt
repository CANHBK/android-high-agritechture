package com.example.administrator.glasshouse


import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.GateWayAdapter
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.config.config
import io.paperdb.Paper
import kotlinx.android.synthetic.main.bottom_sheet_home.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : android.support.v4.app.Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var gateWayAdapter: GateWayAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view_gate_way)
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        (activity as AppCompatActivity).setSupportActionBar(bottomAppbar)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_home);
//        fabHome.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }

        refresh_layout_home.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                Log.d("!call", "call")
                getGateData()

                Log.d("!call", "end")
            }
        }
        )

        getGateData()

        addNewGateWay.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addGateWayFragment)
        }

        add_node_control.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addRelayFragment)
        }

        add_node_env.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addSensorFragment)
        }
    }

    private fun getGateData() {
        progress_gate_way.visibility = View.VISIBLE
        val userId = Paper.book().read<String>(config.USER_ID_KEY)
        MyApolloClient.getApolloClient().query(
                GetAllGateOfUserQuery.builder()
                        .userID(userId)
                        .build()
        ).enqueue(object : ApolloCall.Callback<GetAllGateOfUserQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!get", e.message)
                progress_gate_way.visibility = View.INVISIBLE
            }

            override fun onResponse(response: Response<GetAllGateOfUserQuery.Data>) {
                val result = response.data()!!.allGatesOfUser()
                if (result != null) {
                    activity!!.runOnUiThread {

                        gateWayAdapter = GateWayAdapter(result, context!!)
                        recyclerView.adapter = gateWayAdapter
                        refresh_layout_home.isRefreshing = false;
                        progress_gate_way.visibility = View.INVISIBLE
                    }
                } else { // không có dữ liệu sẽ nhảy vào đây
                    activity!!.runOnUiThread {
                        val error = response.errors()[0].message()
                        Snackbar.make(view!!, error!!, Snackbar.LENGTH_LONG).show()
                        progress_gate_way.visibility = View.INVISIBLE
                        txt_none_gate_way.visibility = View.VISIBLE
                    }

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.bottom_app_bar_home_menu, menu)
//        inflater!!

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.btn_sign_out -> {
                Paper.book().delete(config.EMAIL_KEY)
                Paper.book().delete(config.PASSWORD_ID_KEY)
                view!!.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
