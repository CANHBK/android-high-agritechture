package com.example.administrator.glasshouse.ui.dashboard


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.FragmentDashboardBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Const
import com.example.administrator.glasshouse.vo.Status
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject


class DashboardFragment : androidx.fragment.app.Fragment(), Injectable {
    private lateinit var addBottomSheet: AddGateBottomSheet
    private lateinit var deleteBottomSheet: DeleteGateBottomSheet
    private lateinit var editBottomSheet: EditGateBottomSheet
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dashBoardViewModel: DashBoardViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentDashboardBinding>()


    private var adapter by autoCleared<GateAdapter>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);

        val dataBinding = DataBindingUtil.inflate<FragmentDashboardBinding>(
                inflater,
                R.layout.fragment_dashboard,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        dashBoardViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DashBoardViewModel::class.java)

        // Inflate the layout for this fragment
        return dataBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)
        (activity as AppCompatActivity).setSupportActionBar(binding.bottomAppBar)



        dashBoardViewModel.loadGates()
        binding.setLifecycleOwner(viewLifecycleOwner)

        val userId = Paper.book().read<String>(Const.USER_ID)


        val rvAdapter = GateAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                onDeleteClick = {
                    deleteBottomSheet = DeleteGateBottomSheet.newInstance(gate = it, dashBoardViewModel = dashBoardViewModel)
                    deleteBottomSheet.show(activity!!.supportFragmentManager, deleteBottomSheet.tag)
                },
                onEditClick = {
                    editBottomSheet = EditGateBottomSheet.newInstance(gate = it, dashBoardViewModel = dashBoardViewModel)
                    editBottomSheet.show(activity!!.supportFragmentManager, editBottomSheet.tag)
                }
        )
        binding.rvListDevice.adapter = rvAdapter
        adapter = rvAdapter
        binding.gates=dashBoardViewModel.gates
        addBottomSheet = AddGateBottomSheet.newInstance(dashBoardViewModel)

        dashBoardViewModel.gates.observe(viewLifecycleOwner, Observer { it ->

            if(it.status==Status.SUCCESS&& it.data!!.isNotEmpty()){
                adapter.submitList(it.data)
            }
        })

//        dashBoardViewModel.change.observe(viewLifecycleOwner, Observer {
//            adapter.submitList(it.data)
//        })

        dashBoardViewModel.addGate.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                addBottomSheet.dismiss()
            }

        })

        dashBoardViewModel.removeGate.observe(viewLifecycleOwner, Observer {

            if (it.status == Status.SUCCESS) {
                deleteBottomSheet.dismiss()
            }

        })

        dashBoardViewModel.editGate.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                editBottomSheet.dismiss()
            }
        })

        binding.fabAdd.setOnClickListener {

            addBottomSheet.show(activity!!.supportFragmentManager, addBottomSheet.tag)

        }

        if (userId == null) {
            view.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        topToolbar.inflateMenu(R.menu.menu_dashboard)

        binding.topToolbar.findViewById<View>(R.id.action_sync).setOnClickListener {
            dashBoardViewModel.loadGates()
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
