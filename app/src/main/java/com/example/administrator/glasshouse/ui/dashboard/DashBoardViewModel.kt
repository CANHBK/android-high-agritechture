package com.example.administrator.glasshouse.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.administrator.glasshouse.repository.GateRepository
import com.example.administrator.glasshouse.ui.dashboard.model.AddGateFields
import com.example.administrator.glasshouse.ui.dashboard.model.AddGateForm
import com.example.administrator.glasshouse.ui.dashboard.model.EditGateFields
import com.example.administrator.glasshouse.ui.dashboard.model.EditGateForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.util.ObservableViewModel
import com.example.administrator.glasshouse.vo.Const
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Resource
import io.paperdb.Paper
import javax.inject.Inject
import kotlin.random.Random

class DashBoardViewModel @Inject constructor(repository: GateRepository) : ObservableViewModel() {
    private val triggerAddGate = MutableLiveData<Int>()
    private val triggerDeleteGate = MutableLiveData<Int>()
    private val triggerEditGate = MutableLiveData<Int>()
    private val triggerLoadGates = MutableLiveData<Int>()

    private val _userId = MutableLiveData<String>()

    private val serviceTag = MutableLiveData<String>()
    private val gateName = MutableLiveData<String>()

    private var addGateForm = AddGateForm()
    private var editGateForm = EditGateForm()



    val user: LiveData<String>
        get() = _userId



    val gates: LiveData<Resource<List<Gate>>> = Transformations
            .switchMap(triggerLoadGates) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.loadGates(_userId.value!!)
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
                    repository.removeGate(_userId.value!!, serviceTag.value!!)
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


    fun loadGates() {
        _userId.value = Paper.book().read(Const.USER_ID)
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