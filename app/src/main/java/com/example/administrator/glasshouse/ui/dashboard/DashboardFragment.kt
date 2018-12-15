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
import com.example.administrator.glasshouse.util.AppExecutors
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.FragmentDashboardBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Const
import com.example.administrator.glasshouse.vo.Status
import com.example.administrator.glasshouse.vo.User
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.lang.Exception
import javax.inject.Inject


class DashboardFragment : androidx.fragment.app.Fragment(), Injectable {
    private var addBottomSheet: AddGateBottomSheet? = null
    private var deleteBottomSheet: DeleteGateBottomSheet? = null
    private var editBottomSheet: EditGateBottomSheet? = null
    private var userBottomSheet: UserBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dashBoardViewModel: DashBoardViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var user: User

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

        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)
        (activity as AppCompatActivity).setSupportActionBar(binding.bottomAppBar)


        dashBoardViewModel.loadGates()
        userViewModel.loadUser()

        binding.setLifecycleOwner(viewLifecycleOwner)

        val userId = Paper.book().read<String>(Const.USER_ID)


        val rvAdapter = GateAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                onDeleteClick = {
                    deleteBottomSheet = DeleteGateBottomSheet.newInstance(
                            gate = it, dashBoardViewModel = dashBoardViewModel)
                    deleteBottomSheet?.show(activity!!.supportFragmentManager, deleteBottomSheet?.tag)
                },
                onEditClick = {
                    editBottomSheet = EditGateBottomSheet.newInstance(gate = it, dashBoardViewModel = dashBoardViewModel)
                    editBottomSheet?.show(activity!!.supportFragmentManager, editBottomSheet?.tag)
                }
        )
        binding.rvListDevice.adapter = rvAdapter
        adapter = rvAdapter
        binding.gates = dashBoardViewModel.gates
        addBottomSheet = AddGateBottomSheet.newInstance(dashBoardViewModel)

        dashBoardViewModel.apply {
            gates.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                    adapter.submitList(it.data)
                }
            })

            addGate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        addBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            removeGate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        deleteBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            editGate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        editBottomSheet?.dismiss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }

        binding.fabAdd.setOnClickListener {

            addBottomSheet?.show(activity!!.supportFragmentManager, addBottomSheet?.tag)

        }

        userViewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                user = it.data!!
            }
        })

        if (userId == null) {
            view.findNavController().navigate(R.id.log_out)
        }

        topToolbar.inflateMenu(R.menu.menu_dashboard)

        binding.topToolbar.findViewById<View>(R.id.action_sync).setOnClickListener {
            dashBoardViewModel.loadGates(true)
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            //                Paper.book().delete(Const.USER_ID)
            userBottomSheet = UserBottomSheet.newInstance(
                    user = user,
                    userViewModel = userViewModel) {
                try {
                    userBottomSheet?.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                view.findNavController().navigate(R.id.log_out)
            }
            userBottomSheet?.show(activity!!.supportFragmentManager, userBottomSheet?.tag)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        activity!!.menuInflater.inflate(R.menu.bottom_app_bar_home_menu, menu)
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        when (item!!.itemId) {
//            R.id.btn_sign_out -> {
////                Paper.book().delete(Const.USER_ID)
//                userBottomSheet = UserBottomSheet.newInstance(
//                        user = user,
//                        userViewModel = userViewModel)
//                userBottomSheet?.show(activity!!.supportFragmentManager, userBottomSheet?.tag)
////                view!!.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

}
