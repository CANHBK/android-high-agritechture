package com.mandevices.iot.agriculture.ui.dashboard.model

import com.mandevices.iot.agriculture.R

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData

class AddGateForm : BaseObservable() {
    val fields = AddGateFields()
    private val errors = AddGateErrorFields()
    val addGateFields = MutableLiveData<AddGateFields>()

    val isValid: Boolean
        @Bindable
        get() {
            var valid = isIdValid(false)
            valid = isNameValid(false) && valid
            notifyPropertyChanged(BR.idError)
            notifyPropertyChanged(BR.nameError)
            return valid
        }

    val idError: Int?
        @Bindable
        get() = errors.id

    val nameError: Int?
        @Bindable
        get() = errors.name


    fun isIdValid(setMessage: Boolean): Boolean {
        val regex = """(?:^G)(?:\d){3}\b""".toRegex()
        val id = fields.id
        if (id != null && regex.containsMatchIn(id)) {
            errors.id = null
            notifyPropertyChanged(BR.valid)
            return true
        } else {
            if (setMessage) {
                errors.id = R.string.error_format_invalid
                notifyPropertyChanged(BR.valid)
            }
            return false
        }
    }

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
            addGateFields.value = fields
        }
    }
}
