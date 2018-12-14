package com.example.administrator.glasshouse.ui.monitor


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.FragmentDashboardBinding
import com.example.administrator.glasshouse.databinding.FragmentMonitorBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.ui.dashboard.*
import com.example.administrator.glasshouse.util.AppExecutors
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Const
import com.example.administrator.glasshouse.vo.Status
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.lang.Exception
import javax.inject.Inject

class MonitorFragment : Fragment(), Injectable {
    private lateinit var addBottomSheet: AddNodeBottomSheet
    private lateinit var deleteBottomSheet: DeleteNodeBottomSheet
    private lateinit var editBottomSheet: EditNodeBottomSheet

    private lateinit var serviceTag: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monitorViewModel: MonitorViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentMonitorBinding>()

    private var adapter by autoCleared<MonitorAdapter>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        serviceTag = MonitorFragmentArgs.fromBundle(arguments).serviceTag

        val dataBinding = DataBindingUtil.inflate<FragmentMonitorBinding>(
                inflater,
                R.layout.fragment_monitor,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        monitorViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MonitorViewModel::class.java)

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)
            result = monitorViewModel.monitors
            fabAdd.setOnClickListener {
                addBottomSheet.show(activity!!.supportFragmentManager, addBottomSheet.tag)
            }
        }

        MonitorAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                onDeleteClick = {
                    deleteBottomSheet = DeleteNodeBottomSheet.newInstance(monitor = it, monitorViewModel = monitorViewModel)
                    deleteBottomSheet.show(activity!!.supportFragmentManager, deleteBottomSheet.tag)
                },
                onEditClick = {
                    editBottomSheet = EditNodeBottomSheet.newInstance(monitor = it, monitorViewModel = monitorViewModel)
                    editBottomSheet.show(activity!!.supportFragmentManager, editBottomSheet.tag)
                }
        ).also {
            binding.rvListNode.adapter = it
            adapter = it
        }


        addBottomSheet = AddNodeBottomSheet.newInstance(serviceTag = serviceTag, monitorViewModel = monitorViewModel)

        monitorViewModel.apply {
            loadMonitor(serviceTag)
            monitors.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                    adapter.submitList(it.data)
                }
            })

            addMonitor.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        addBottomSheet.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            deleteMonitor.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        deleteBottomSheet.dismiss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            editMonitor.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        editBottomSheet.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }

    }

}
