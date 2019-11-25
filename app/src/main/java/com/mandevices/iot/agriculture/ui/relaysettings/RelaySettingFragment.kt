package com.mandevices.iot.agriculture.ui.relaysettings


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.FragmentRelaySettingBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.ui.control.ControlViewModel
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Relay
import com.mandevices.iot.agriculture.vo.Status
import java.util.*
import javax.inject.Inject


class RelaySettingFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var controlViewModel: ControlViewModel

    private lateinit var control: Control
    private var relayIndex: Int? = null

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentRelaySettingBinding>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        control = RelaySettingFragmentArgs.fromBundle(arguments!!).control
        relayIndex = RelaySettingFragmentArgs.fromBundle(arguments!!).relayIndex

        val dataBinding = DataBindingUtil.inflate<FragmentRelaySettingBinding>(
                inflater,
                R.layout.fragment_relay_setting,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding
        val currentTime = Calendar.getInstance()
        val mHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val mMinute = currentTime.get(Calendar.MINUTE)

        binding.apply {
            selectedOnTimeText.text = "${timeTextFormat(mHour)}:${timeTextFormat(mMinute)}"
            selectedOffTimeText.text = "${timeTextFormat(mHour)}:${timeTextFormat(mMinute)}"
            selectedOnTimeText.setOnClickListener {
                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    this.selectedOnTimeText.text = "${timeTextFormat(hourOfDay)}:${timeTextFormat(minute)}"
                }, mHour, mMinute, true).show()
            }

            selectedOffTimeText.setOnClickListener {
                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    this.selectedOffTimeText.text = "${timeTextFormat(hourOfDay)}:${timeTextFormat(minute)}"
                }, mHour, mMinute, true).show()
            }
        }

        controlViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ControlViewModel::class.java)

        return dataBinding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topToolbar)
        val gson = GsonBuilder().setPrettyPrinting().create()
        var relayList: List<Relay> = gson.fromJson(control.relays, object : TypeToken<List<Relay>>() {}.type)

        relayList = relayList.sortedWith(compareBy {
            it.index
        })
        controlViewModel.apply {
            initEditControl()

            getEditControlFields()?.observe(viewLifecycleOwner, androidx.lifecycle.Observer { })
            configTimeControl.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it.status == Status.SUCCESS) {
                    view.findNavController().popBackStack()
                }
            })
        }
        binding.apply {
            setLifecycleOwner(viewLifecycleOwner)
            topToolbar.setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }
            result = controlViewModel.configTimeControl
            cancelButton.setOnClickListener {
                it.findNavController().popBackStack()
            }
            viewModel = controlViewModel
        }

        controlViewModel.getEditControlForm().fields.name = relayList[relayIndex!! - 1].name
        binding.control = control
        var isRepeat = false
        binding.profileGroup.setOnCheckedChangeListener { group, checkedId ->
            isRepeat = when (binding.profileGroup.checkedRadioButtonId) {
                R.id.repeat_option -> true
                else -> false
            }
        }

        binding.saveButton.setOnClickListener {
            controlViewModel.configTimeControl(
                    serviceTag = control.serviceTag,
                    controlTag = control.tag,
                    index = relayIndex!!,
                    name = binding.relayNameEdit.text.toString(),
                    isRepeat = isRepeat,
                    onHour = binding.selectedOnTimeText.text.toString().split(":")[0],
                    onMinute = binding.selectedOnTimeText.text.toString().split(":")[1],
                    offHour = binding.selectedOffTimeText.text.toString().split(":")[0],
                    offMinute = binding.selectedOffTimeText.text.toString().split(":")[1]
            )
        }
        binding.cancelButton.setOnClickListener {
            it.findNavController().popBackStack()
        }


    }

    private fun timeTextFormat(timeNumber: Int): String {
        return if (timeNumber > 9) "$timeNumber"
        else "0$timeNumber"
    }

}
