package com.mandevices.iot.agriculture.ui.control.model

import com.mandevices.iot.agriculture.R

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.mandevices.iot.agriculture.ui.dashboard.model.AddGateErrorFields
import com.mandevices.iot.agriculture.ui.dashboard.model.AddGateFields


class AddControlForm : BaseObservable() {
    val fields = AddControlFields()
    private val errors = AddControlErrorFields()
    val addControlFields = MutableLiveData<AddControlFields>()

    val isValid: Boolean
        @Bindable
        get() {
            var valid = isTagValid(false)
            valid = isNameValid(false) && valid
            notifyPropertyChanged(BR.idError)
            notifyPropertyChanged(BR.nameError)
            return valid
        }

    val tagError: Int?
        @Bindable
        get() = errors.id

    val nameError: Int?
        @Bindable
        get() = errors.name


    fun isTagValid(setMessage: Boolean): Boolean {
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
            addControlFields.value = fields
        }
    }
}
