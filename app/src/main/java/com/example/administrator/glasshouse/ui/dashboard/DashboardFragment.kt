package com.example.administrator.glasshouse.ui.dashboard


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Const
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject


class DashboardFragment : androidx.fragment.app.Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dashBoardViewModel: DashBoardViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)


    private var adapter by autoCleared<GateAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashBoardViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DashBoardViewModel::class.java)


        val userId = Paper.book().read<String>(Const.USER_ID)


        adapter = GateAdapter(dataBindingComponent, appExecutors)


        dashBoardViewModel.setUserId(userId)
        dashBoardViewModel.gates.observe(viewLifecycleOwner, Observer { gates ->

            adapter.submitList(gates.data)
        })

        (activity as AppCompatActivity).setSupportActionBar(bottom_app_bar)
        fab_add.setOnClickListener {
            val addBottomSheet = AddGateBottomSheet()
            addBottomSheet.show(activity!!.supportFragmentManager, addBottomSheet.tag)
        }

        rv_list_device.adapter = adapter

        if (userId == null) {
            view.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.bottom_app_bar_home_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.btn_sign_out -> {
                Paper.book().delete(Const.USER_ID)
                view!!.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
