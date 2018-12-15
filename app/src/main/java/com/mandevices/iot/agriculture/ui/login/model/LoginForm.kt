package com.mandevices.iot.agriculture.ui.login.model

import com.mandevices.iot.agriculture.R

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData

class LoginForm : BaseObservable() {
    val fields = LoginFields()
    private val errors = LoginErrorFields()
    val loginFields = MutableLiveData<LoginFields>()
    val isValid: Boolean
        @Bindable
        get() {
            var valid = isEmailValid(false)
            valid = isPasswordValid(false) && valid
            notifyPropertyChanged(BR.emailError)
            notifyPropertyChanged(BR.passwordError)
            return valid
        }

    val emailError: Int?
        @Bindable
        get() = errors.email

    val passwordError: Int?
        @Bindable
        get() = errors.password

    fun isEmailValid(setMessage: Boolean): Boolean {
        // Minimum a@b.c
        val email = fields.email
        if (email != null && email.length > 5) {
            val indexOfAt = email.indexOf("@")
            val indexOfDot = email.lastIndexOf(".")
            if (indexOfAt in 1..(indexOfDot - 1) && indexOfDot < email.length - 1) {
                errors.email = null
                notifyPropertyChanged(BR.valid)
                return true
            } else {
                if (setMessage) {
                    errors.email = R.string.error_format_invalid
                    notifyPropertyChanged(BR.valid)
                }
                return false
            }
        }
        if (setMessage) {
            errors.email = R.string.error_too_short
            notifyPropertyChanged(BR.valid)
        }

        return false
    }

    fun isPasswordValid(setMessage: Boolean): Boolean {
        val password = fields.password
        if (password != null && password.length > 5) {
            errors.password = null
            notifyPropertyChanged(BR.valid)
            return true
        } else {
            if (setMessage) {
                errors.password = R.string.error_too_short
                notifyPropertyChanged(BR.valid)
            }

            return false
        }
    }

    fun onClick() {
        if (isValid) {
            loginFields.value = fields
        }
    }
}
