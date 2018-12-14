package com.example.administrator.glasshouse.ui.monitor

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.administrator.glasshouse.repository.MonitorRepository
import com.example.administrator.glasshouse.ui.monitor.model.AddMonitorFields
import com.example.administrator.glasshouse.ui.monitor.model.AddMonitorForm
import com.example.administrator.glasshouse.ui.monitor.model.EditMonitorFields
import com.example.administrator.glasshouse.ui.monitor.model.EditMonitorForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.util.ObservableViewModel
import com.example.administrator.glasshouse.vo.Monitor
import com.example.administrator.glasshouse.vo.Resource
import javax.inject.Inject
import kotlin.random.Random

class MonitorViewModel @Inject constructor(repository: MonitorRepository) : ObservableViewModel() {
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

    val monitors: LiveData<Resource<List<Monitor>>> = Transformations
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