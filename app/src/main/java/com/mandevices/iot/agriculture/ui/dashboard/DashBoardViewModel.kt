package com.mandevices.iot.agriculture.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mandevices.iot.agriculture.repository.GateRepository
import com.mandevices.iot.agriculture.ui.dashboard.model.AddGateFields
import com.mandevices.iot.agriculture.ui.dashboard.model.AddGateForm
import com.mandevices.iot.agriculture.ui.dashboard.model.EditGateFields
import com.mandevices.iot.agriculture.ui.dashboard.model.EditGateForm
import com.mandevices.iot.agriculture.util.AbsentLiveData
import com.mandevices.iot.agriculture.util.ObservableViewModel
import com.mandevices.iot.agriculture.vo.Const
import com.mandevices.iot.agriculture.vo.Gate
import com.mandevices.iot.agriculture.vo.Resource
import io.paperdb.Paper
import javax.inject.Inject
import kotlin.random.Random

class DashBoardViewModel @Inject constructor(repository: GateRepository) : ObservableViewModel() {
    private val triggerAddGate = MutableLiveData<Int>()
    private val triggerDeleteGate = MutableLiveData<Int>()
    private val triggerEditGate = MutableLiveData<Int>()
    private val triggerLoadGates = MutableLiveData<Int>()

    private val refresh = MutableLiveData<Boolean>()

    private val _userId = MutableLiveData<String>()

    private val serviceTag = MutableLiveData<String>()
    private val gateName = MutableLiveData<String>()

    private lateinit var addGateForm: AddGateForm
    private lateinit var editGateForm: EditGateForm

    fun initAddGate() {
        addGateForm = AddGateForm()
    }

    fun initEditGate() {
        editGateForm = EditGateForm()
    }

    val user: LiveData<String>
        get() = _userId


    val gates: LiveData<Resource<List<Gate>>> = Transformations
            .switchMap(triggerLoadGates) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.loadGates(_userId.value!!, refresh.value!!)
                }
            }
    val addGate: LiveData<Resource<Gate>> = Transformations
            .switchMap(triggerAddGate) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.addGate(_userId.value!!, serviceTag.value!!, gateName.value!!)
                }
            }

    val removeGate: LiveData<Resource<Gate>> = Transformations
            .switchMap(triggerDeleteGate) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.deleteGate(_userId.value!!, serviceTag.value!!)
                }
            }

    val editGate: LiveData<Resource<Gate>> = Transformations
            .switchMap(triggerEditGate) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.editGate(_userId.value!!, serviceTag.value!!, gateName.value!!)
                }
            }


    fun getAddGateForm(): AddGateForm? {

        return addGateForm
    }

    fun getEditGateForm(): EditGateForm? {

        return editGateForm
    }


    fun onAddGateClick() {
        addGateForm.isNameValid(true)
        addGateForm.isIdValid(true)
        addGateForm.onClick()
    }

    fun onEditGateClick() {
        editGateForm.isNameValid(true)
        editGateForm.onClick()
    }


    fun getAddGateFields(): MutableLiveData<AddGateFields>? {
        return addGateForm.addGateFields
    }

    fun getEditGateFields(): MutableLiveData<EditGateFields>? {
        return editGateForm.editGateFields
    }


    fun loadGates(refresh: Boolean = false) {
        _userId.value = Paper.book().read(Const.USER_ID)
        this.refresh.value = refresh
        triggerLoadGates.value = Random.nextInt(1, 10)
    }

    fun addGate(serviceTag: String, name: String) {
        this.serviceTag.value = serviceTag
        gateName.value = name
        _userId.value = Paper.book().read(Const.USER_ID)
        triggerAddGate.value = Random.nextInt(1, 10)
    }

    fun removeGate(serviceTag: String) {
        this.serviceTag.value = serviceTag
        _userId.value = Paper.book().read(Const.USER_ID)
        triggerDeleteGate.value = Random.nextInt(1, 10)
    }

    fun editGate(serviceTag: String, name: String) {
        this.serviceTag.value = serviceTag
        gateName.value = name
        _userId.value = Paper.book().read(Const.USER_ID)
        triggerEditGate.value = Random.nextInt(1, 10)
    }
}