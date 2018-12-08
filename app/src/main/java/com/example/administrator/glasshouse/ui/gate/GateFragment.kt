package com.example.administrator.glasshouse.ui.gate


import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.GetAllGateOfUserQuery
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.config.config
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.ui.farm.FarmAdapter
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Farm
import io.paperdb.Paper
import kotlinx.android.synthetic.main.bottom_sheet_home.*
import kotlinx.android.synthetic.main.gate_fragment.*
import javax.inject.Inject


class GateFragment : androidx.fragment.app.Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var gateViewModel: GateViewModel

    @Inject
    lateinit var apolloClient: ApolloClient

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)


    private var adapter by autoCleared<GateAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.gate_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gateViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GateViewModel::class.java)


        val userId = Paper.book().read<String>(config.USER_ID_KEY)


        adapter = GateAdapter(dataBindingComponent, appExecutors)


        gateViewModel.setUserId(userId)
        gateViewModel.gates.observe(viewLifecycleOwner, Observer { gates ->
            adapter.submitList(gates.data)
        })

        (activity as AppCompatActivity).setSupportActionBar(bottomAppbar)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_home);
        fabHome.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        recycler_view_gate_way.adapter = adapter

        refresh_layout_home.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                Log.d("!call", "call")
//                getGateData(userId)

                Log.d("!call", "end")
            }
        }
        )

        if (userId == null) {
            view.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        } else {

        }



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


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.bottom_app_bar_home_menu, menu)

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
