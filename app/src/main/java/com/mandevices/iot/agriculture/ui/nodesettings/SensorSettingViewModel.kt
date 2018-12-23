package com.mandevices.iot.agriculture.ui.nodesettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mandevices.iot.agriculture.repository.MonitorRepository
import com.mandevices.iot.agriculture.ui.monitor.model.AddMonitorFields
import com.mandevices.iot.agriculture.ui.monitor.model.AddMonitorForm
import com.mandevices.iot.agriculture.ui.monitor.model.EditMonitorFields
import com.mandevices.iot.agriculture.ui.monitor.model.EditMonitorForm
import com.mandevices.iot.agriculture.util.AbsentLiveData
import com.mandevices.iot.agriculture.util.ObservableViewModel
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.MonitorWithSensors
import com.mandevices.iot.agriculture.vo.Resource
import javax.inject.Inject
import kotlin.random.Random

class SensorSettingViewModel @Inject constructor(repository: MonitorRepository) : ObservableViewModel() {
    private val triggerAddMonitor = MutableLiveData<Int>()
    private val triggerDeleteMonitor = MutableLiveData<Int>()
    private val triggerEditMonitor = MutableLiveData<Int>()
    private val triggerLoadMonitors = MutableLiveData<Int>()

    private val serviceTag = MutableLiveData<String>()
    private val tag = MutableLiveData<String>()
    private val monitorName = MutableLiveData<String>()

    private lateinit var addMonitorForm: AddMonitorForm
    private lateinit var editMonitorForm: EditMonitorForm

    fun initAddMonitor() {
        addMonitorForm = AddMonitorForm()
    }

    fun initEditMonitor() {
        editMonitorForm = EditMonitorForm()
    }

    val monitors: LiveData<Resource<List<MonitorWithSensors>>> = Transformations
            .switchMap(triggerLoadMonitors) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.loadMonitors(serviceTag.value!!)
                }
            }
    val addMonitor: LiveData<Resource<Monitor>> = Transformations
            .switchMap(triggerAddMonitor) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.addMonitor(serviceTag.value!!, tag.value!!, monitorName.value!!)
                }
            }

    val deleteMonitor: LiveData<Resource<Monitor>> = Transformations
            .switchMap(triggerDeleteMonitor) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.deleteMonitor(tag.value!!)
                }
            }

    val editMonitor: LiveData<Resource<Monitor>> = Transformations
            .switchMap(triggerEditMonitor) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.editMonitor(tag.value!!, monitorName.value!!)
                }
            }


    fun getAddMonitorForm(): AddMonitorForm {

        return addMonitorForm
    }

    fun getEditMonitorForm(): EditMonitorForm {

        return editMonitorForm
    }


    fun onAddMonitorClick() {
        addMonitorForm.isNameValid(true)
        addMonitorForm.isTagValid(true)
        addMonitorForm.onClick()
    }

    fun onEditMonitorClick() {
        editMonitorForm.isNameValid(true)
        editMonitorForm.onClick()
    }


    fun getAddMonitorFields(): MutableLiveData<AddMonitorFields>? {
        return addMonitorForm.addMonitorFields
    }

    fun getEditMonitorFields(): MutableLiveData<EditMonitorFields>? {
        return editMonitorForm.editMonitorFields
    }


    fun loadMonitor(serviceTag: String) {
        this.serviceTag.value = serviceTag
        triggerLoadMonitors.value = Random.nextInt(1, 10)
    }

    fun addMonitor(serviceTag: String, tag: String, name: String) {
        this.serviceTag.value = serviceTag
        monitorName.value = name
        this.tag.value = tag
        triggerAddMonitor.value = Random.nextInt(1, 10)
    }

    fun deleteMonitor(tag: String) {
        this.tag.value = tag
        triggerDeleteMonitor.value = Random.nextInt(1, 10)
    }

    fun editMonitor(tag: String, name: String) {
        this.tag.value = tag
        monitorName.value = name
        triggerEditMonitor.value = Random.nextInt(1, 10)
    }
}