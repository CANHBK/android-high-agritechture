package com.example.administrator.glasshouse.ui.login

import android.view.View
import android.widget.EditText
import androidx.annotation.VisibleForTesting
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.R.id.view
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.ui.login.model.LoginFields
import com.example.administrator.glasshouse.ui.login.model.LoginForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.util.ObservableViewModel
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject
import kotlin.random.Random

class LoginViewModel @Inject constructor(val repository: UserRepository) : ObservableViewModel() {
    private val _trigger = MutableLiveData<Int>()
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()


    val user: LiveData<Resource<User>> = Transformations
            .switchMap(_trigger) { trigger ->
                if (trigger == null) {
                    AbsentLiveData.create()
                } else {
                    val user = repository.login(_email.value!!, _password.value!!)
                    user
                }
            }

    fun login(email: String?, password: String?) {
        _email.value = email
        _password.value = password
        _trigger.value = Random.nextInt(1, 10)
    }

    private var loginForm: LoginForm? = null
    private var onFocusEmail: View.OnFocusChangeListener? = null
    private var onFocusPassword: View.OnFocusChangeListener? = null

    @VisibleForTesting
    fun init() {
        loginForm = LoginForm()
        onFocusEmail = View.OnFocusChangeListener { view, focused ->
            val et = view as TextInputEditText
            if (et.text!!.isNotEmpty() && !focused) {
                loginForm?.isEmailValid(true)
            }
        }

        onFocusPassword = View.OnFocusChangeListener { view, focused ->
            val et = view as TextInputEditText
            if (et.text!!.isNotEmpty() && !focused) {
                loginForm?.isPasswordValid(true)
            }
        }
    }

    fun getLoginForm(): LoginForm? {
        return loginForm
    }

    fun getEmailOnFocusChangeListener(): View.OnFocusChangeListener? {
        return onFocusEmail
    }

    fun getPasswordOnFocusChangeListener(): View.OnFocusChangeListener? {
        return onFocusPassword
    }

    fun onButtonClick() {
        loginForm?.onClick()
    }

    fun getLoginFields(): MutableLiveData<LoginFields>? {
        return loginForm?.loginFields
    }

    fun registerAccount(view: View) {
        view.findNavController().navigate(R.id.to_create_account)
    }


}