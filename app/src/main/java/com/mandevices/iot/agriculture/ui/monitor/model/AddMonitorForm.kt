package com.mandevices.iot.agriculture.ui.monitor.model

import com.mandevices.iot.agriculture.R
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData

class AddMonitorForm : BaseObservable() {
    val fields = AddMonitorFields()
    private val errors = AddMonitorErrorFields()
    val addMonitorFields = MutableLiveData<AddMonitorFields>()

    val isValid: Boolean
        @Bindable
        get() {
            var valid = isNameValid(false)
            valid = isTagValid(false) && valid
            notifyPropertyChanged(BR.nameError)
            return valid
        }
    val nameError: Int?
        @Bindable
        get() = errors.name
    val tagError: Int?
        @Bindable
        get() = errors.tag


    fun isTagValid(setMessage: Boolean): Boolean {
        val regex = """^E-(?:\d|[A-F]){3}-(?:\d|[A-F]){1}:(?:\d|[A-F]){1}\b""".toRegex()
        val tag = fields.tag
        if (tag != null && regex.containsMatchIn(tag)) {
            errors.tag = null
            notifyPropertyChanged(BR.valid)
            return true
        } else {
            if (setMessage) {
                errors.tag = R.string.error_format_invalid
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
            addMonitorFields.value = fields
        }
    }
}
