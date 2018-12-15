package com.mandevices.iot.agriculture.ui.monitor.model

import com.mandevices.iot.agriculture.R
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData

class EditMonitorForm : BaseObservable() {
    val fields = EditMonitorFields()
    private val errors = EditMonitorErrorFields()
    val editMonitorFields = MutableLiveData<EditMonitorFields>()

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
        return if (name != null && name.isNotEmpty()) {
            errors.name = null
            notifyPropertyChanged(BR.valid)
            true
        } else {
            if (setMessage) {
                errors.name = R.string.not_empty
                notifyPropertyChanged(BR.valid)
            }
            false
        }
    }

    fun onClick() {
        if (isValid) {
            editMonitorFields.value = fields
        }
    }
}
