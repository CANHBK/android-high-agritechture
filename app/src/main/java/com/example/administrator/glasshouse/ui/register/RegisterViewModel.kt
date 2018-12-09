package com.example.administrator.glasshouse.ui.register

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.ui.login.model.LoginFields
import com.example.administrator.glasshouse.ui.login.model.LoginForm
import com.example.administrator.glasshouse.ui.register.model.RegisterFields
import com.example.administrator.glasshouse.ui.register.model.RegisterForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject
import kotlin.random.Random

class RegisterViewModel @Inject constructor(repository: UserRepository) : ViewModel() {

    private val _trigger = MutableLiveData<Int>()
    private val _email = MutableLiveData<String>()
    private val _name = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    private var registerForm: RegisterForm? = null
    private var onFocusEmail: View.OnFocusChangeListener? = null
    private var onFocusPassword: View.OnFocusChangeListener? = null
    private var onFocusName: View.OnFocusChangeListener? = null
    private var onFocusRePassword: View.OnFocusChangeListener? = null

    val user: LiveData<Resource<User>> = Transformations
            .switchMap(_trigger) { trigger ->
                if (trigger == null) {
                    AbsentLiveData.create()
                } else {
                    val user = repository.register(_email.value!!, _name.value!!, _password.value!!)


                    user
                }
            }


    @VisibleForTesting
    fun init() {
        registerForm = RegisterForm()
        onFocusName = View.OnFocusChangeListener { view, focused ->
            val et = view as TextInputEditText
            if (et.text!!.isNotEmpty() && !focused) {
                registerForm?.isNameValid(true)
            }
        }

        onFocusEmail = View.OnFocusChangeListener { view, focused ->
            val et = view as TextInputEditText
            if (et.text!!.isNotEmpty() && !focused) {
                registerForm?.isEmailValid(true)
            }
        }

        onFocusPassword = View.OnFocusChangeListener { view, focused ->
            val et = view as TextInputEditText
            if (et.text!!.isNotEmpty() && !focused) {
                registerForm?.isPasswordValid(true)
            }
        }

        onFocusRePassword = View.OnFocusChangeListener { view, focused ->
            val et = view as TextInputEditText
            if (et.text!!.isNotEmpty() && !focused) {
                registerForm?.isRePasswordValid(true)
            }
        }
    }

    fun getRegisterForm(): RegisterForm? {
        return registerForm
    }

    fun getEmailOnFocusChangeListener(): View.OnFocusChangeListener? {
        return onFocusEmail
    }

    fun getPasswordOnFocusChangeListener(): View.OnFocusChangeListener? {
        return onFocusPassword
    }

    fun getNameOnFocusChangeListener(): View.OnFocusChangeListener? {
        return onFocusName
    }

    fun onRegisterClick() {
        registerForm?.onClick()
    }

    fun getRePasswordOnFocusChangeListener(): View.OnFocusChangeListener? {
        return onFocusRePassword
    }

    fun getRegisterFields(): MutableLiveData<RegisterFields>? {
        return registerForm?.registerFields
    }

    fun login(view: View) {
        view.findNavController().popBackStack()
    }

    fun register(email: String, name: String, password: String) {
        _email.value = email
        _password.value = password
        _name.value = name
        _trigger.value = Random.nextInt(1, 10)
    }
}