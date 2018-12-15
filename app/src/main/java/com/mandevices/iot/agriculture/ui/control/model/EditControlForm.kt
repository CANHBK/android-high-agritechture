package com.mandevices.iot.agriculture.ui.control.model

import com.mandevices.iot.agriculture.R

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.mandevices.iot.agriculture.ui.dashboard.model.EditGateErrorFields
import com.mandevices.iot.agriculture.ui.dashboard.model.EditGateFields

class EditControlForm : BaseObservable() {
    val fields = EditGateFields()
    private val errors = EditGateErrorFields()
    val editGateFields = MutableLiveData<EditGateFields>()

    val isValid: Boolean
        @Bindable
        get() {
            val valid = isNameValid(false)
            notifyPropertyChanged(BR.nameError)
            return valid
        }
    val nameError: Int?
        @Bindable
        get() = errors.name


    fun isNameValid(setMessage: Boolean): Boolean {
        val name = fields.name
        if (name != null && name.isNotEmpty()) {
            errors.name = null
            notifyPropertyChanged(BR.valid)
            return true
        } else {
            if (setMessage) {
                errors.name = R.string.not_empty
                notifyPropertyChanged(BR.valid)
            }
            return false
        }
    }

    fun onClick() {
        if (isValid) {
            editGateFields.value = fields
        }
    }
}
