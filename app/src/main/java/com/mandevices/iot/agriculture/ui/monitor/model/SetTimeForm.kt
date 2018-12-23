package com.mandevices.iot.agriculture.ui.monitor.model

import com.mandevices.iot.agriculture.R
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData

class SetTimeForm : BaseObservable() {
    val fields = SetTimeFields()
    private val errors = SetTimeErrorFields()
    val setTimeFields = MutableLiveData<SetTimeFields>()

    val isValid: Boolean
        @Bindable
        get() {
            val valid = isTimeValid(false)
            notifyPropertyChanged(BR.nameError)
            return valid
        }
    val timeError: Int?
        @Bindable
        get() = errors.time


    fun isTimeValid(setMessage: Boolean): Boolean {
        val time = fields.time
        return if (time != null && time.toInt()<60) {
            errors.time = null
            notifyPropertyChanged(BR.valid)
            true
        } else {
            if (setMessage) {
                errors.time = R.string.not_valid_time
                notifyPropertyChanged(BR.valid)
            }
            false
        }
    }

    fun onClick() {
        if (isValid) {
            setTimeFields.value = fields
        }
    }
}
