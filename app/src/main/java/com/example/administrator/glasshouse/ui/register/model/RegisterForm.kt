package com.example.administrator.glasshouse.ui.register.model

import com.example.administrator.glasshouse.R

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.example.administrator.glasshouse.R.string.email
import com.example.administrator.glasshouse.R.string.password

class RegisterForm : BaseObservable() {
    val fields = RegisterFields()
    private val errors = RegisterErrorFields()
    val registerFields = MutableLiveData<RegisterFields>()

    val isValid: Boolean
        @Bindable
        get() {
            var valid = isEmailValid(false)
            valid = isPasswordValid(false) && valid
            valid = isNameValid(false) && valid
            valid = isRePasswordValid(false) && valid
            notifyPropertyChanged(BR.emailError)
            notifyPropertyChanged(BR.passwordError)
            notifyPropertyChanged(BR.nameError)
            notifyPropertyChanged(BR.rePasswordError)
            return valid
        }

    val emailError: Int?
        @Bindable
        get() = errors.email

    val passwordError: Int?
        @Bindable
        get() = errors.password

    val nameError: Int?
        @Bindable
        get() = errors.name

    val rePasswordError: Int?
        @Bindable
        get() = errors.rePassword

    fun isNameValid(setMessage: Boolean): Boolean {
        val name = fields.name
        if (name != null && name.length > 5) {
            errors.name = null
            notifyPropertyChanged(BR.valid)
            return true
        } else {
            if (setMessage) {
                errors.name = R.string.error_too_short
                notifyPropertyChanged(BR.valid)
            }

            return false
        }
    }

    fun isEmailValid(setMessage: Boolean): Boolean {
        // Minimum a@b.c
        val email = fields.email
        if (email != null && email.length > 5) {
            val indexOfAt = email.indexOf("@")
            val indexOfDot = email.lastIndexOf(".")
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length - 1) {
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

    fun isRePasswordValid(setMessage: Boolean): Boolean {
        val password = fields.password
        val rePassword = fields.rePassword
        if (password == rePassword) {
            errors.rePassword = null
            notifyPropertyChanged(BR.valid)
            return true
        } else {
            if (setMessage) {
                errors.rePassword = R.string.password_not_match
                notifyPropertyChanged(BR.valid)
            }
            return false
        }
    }

    fun onClick() {
        if (isValid) {
            registerFields.value = fields
        }
    }
}
