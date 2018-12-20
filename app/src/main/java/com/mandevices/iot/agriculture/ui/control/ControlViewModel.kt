package com.mandevices.iot.agriculture.ui.control

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.repository.ControlRepository
import com.mandevices.iot.agriculture.repository.MonitorRepository
import com.mandevices.iot.agriculture.ui.control.model.AddControlFields
import com.mandevices.iot.agriculture.ui.control.model.AddControlForm
import com.mandevices.iot.agriculture.ui.control.model.EditControlFields
import com.mandevices.iot.agriculture.ui.control.model.EditControlForm
import com.mandevices.iot.agriculture.ui.monitor.model.AddMonitorFields
import com.mandevices.iot.agriculture.ui.monitor.model.AddMonitorForm
import com.mandevices.iot.agriculture.ui.monitor.model.EditMonitorFields
import com.mandevices.iot.agriculture.ui.monitor.model.EditMonitorForm
import com.mandevices.iot.agriculture.util.AbsentLiveData
import com.mandevices.iot.agriculture.util.ObservableViewModel
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Resource
import javax.inject.Inject
import kotlin.random.Random

class ControlViewModel @Inject constructor(repository: ControlRepository) : ObservableViewModel() {
    private val triggerAddControl = MutableLiveData<Int>()
    private val triggerDeleteControl = MutableLiveData<Int>()
    private val triggerEditControl = MutableLiveData<Int>()
    private val triggerLoadControls = MutableLiveData<Int>()
    private val triggerSetState = MutableLiveData<Int>()
    private val triggerConfig = MutableLiveData<Int>()

    private val serviceTag = MutableLiveData<String>()
    private val tag = MutableLiveData<String>()
    private val isAuto = MutableLiveData<Boolean>()
    private val name = MutableLiveData<String>()
    private val onHour = MutableLiveData<String>()
    private val onMinute = MutableLiveData<String>()
    private val offHour = MutableLiveData<String>()
    private val offMinute = MutableLiveData<String>()
    private val controlName = MutableLiveData<String>()
    private val index = MutableLiveData<Int>()
    private val state = MutableLiveData<String>()

    private lateinit var addControlForm: AddControlForm
    private lateinit var editControlForm: EditControlForm

    fun initAddControl() {
        addControlForm = AddControlForm()
    }

    fun initEditControl() {
        editControlForm = EditControlForm()
    }

    val controls: LiveData<Resource<List<Control>>> = Transformations
            .switchMap(triggerLoadControls) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.loadControls(serviceTag.value!!)
                }
            }

    val configTimerControl: LiveData<Resource<Control>> = Transformations
            .switchMap(triggerConfig) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.configTimeControl(
                            serviceTag = serviceTag.value!!,
                            controlTag = tag.value!!,
                            index = index.value!!,
                            state = state.value!!,
                            isAuto = isAuto.value!!,
                            onMinute = onMinute.value!!,
                            onHour = onHour.value!!,
                            offMinute = offMinute.value!!,
                            offHour = offHour.value!!,
                            name = name.value!!)
                }
            }
    val addControl: LiveData<Resource<Control>> = Transformations
            .switchMap(triggerAddControl) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.addControl(serviceTag.value!!, tag.value!!, controlName.value!!)
                }
            }

    val deleteControl: LiveData<Resource<Control>> = Transformations
            .switchMap(triggerDeleteControl) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.deleteControl(tag.value!!)
                }
            }
    val setStateRelay: LiveData<Resource<List<Control>>> = Transformations
            .switchMap(triggerSetState) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.setState(tag = tag.value!!, index = index.value!!, state = state.value!!, serviceTag = serviceTag.value!!)
                }
            }

    val editControl: LiveData<Resource<Control>> = Transformations
            .switchMap(triggerEditControl) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.editControl(tag.value!!, controlName.value!!)
                }
            }


    fun getAddControlForm(): AddControlForm {

        return addControlForm
    }

    fun getEditControlForm(): EditControlForm {

        return editControlForm
    }


    fun onAddControlClick() {
        addControlForm.isNameValid(true)
        addControlForm.isTagValid(true)
        addControlForm.onClick()
    }

    fun onEditControlClick() {
        editControlForm.isNameValid(true)
        editControlForm.onClick()
    }


    fun getAddControlFields(): MutableLiveData<AddControlFields>? {
        return addControlForm.addControlFields
    }

    fun getEditControlFields(): MutableLiveData<EditControlFields>? {
        return editControlForm.editControlFields
    }


    fun loadControls(serviceTag: String) {
        this.serviceTag.value = serviceTag
        triggerLoadControls.value = Random.nextInt(1, 10)
    }

    fun addControl(serviceTag: String, tag: String, name: String) {
        this.serviceTag.value = serviceTag
        controlName.value = name
        this.tag.value = tag
        triggerAddControl.value = Random.nextInt(1, 10)
    }

    fun deleteControl(tag: String) {
        this.tag.value = tag
        triggerDeleteControl.value = Random.nextInt(1, 10)
    }

    fun editControl(tag: String, name: String) {
        this.tag.value = tag
        controlName.value = name
        triggerEditControl.value = Random.nextInt(1, 10)
    }

//    fun getImageStateOn(): Int {
//        return R.drawable.ic_power_settings_new_black_48dp
//    }
//
//    fun getImageStateOff(): Int {
//        return R.drawable.ic_power_settings_new_orange_400_48dp
//    }

    fun configTimeControl(
            serviceTag: String, controlTag: String, index: Int,
            name: String = "Chưa hoan thanh", onHour: String, onMinute: String, offHour: String, offMinute: String, state: String, isAuto: Boolean) {
        this.serviceTag.value = serviceTag
        this.tag.value = controlTag
        this.index.value = index
        this.name.value = name
        this.onHour.value = onHour
        this.onMinute.value = onMinute
        this.offHour.value = offHour
        this.offMinute.value = offMinute
        this.state.value = state
        this.isAuto.value = isAuto
        triggerConfig.value = Random.nextInt(1, 10)

    }

    fun setState(serviceTag: String, tag: String, index: Int, state: String) {
        this.index.value = index
        this.tag.value = tag
        this.serviceTag.value = serviceTag
        this.state.value = if (state == "F") "O" else "F"
        triggerSetState.value = Random.nextInt(1, 10)
    }
}