package com.example.administrator.glasshouse.ui.login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.ui.login.model.LoginFields
import com.example.administrator.glasshouse.ui.login.model.LoginForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.util.ObservableViewModel
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import javax.inject.Inject
import kotlin.random.Random
import android.text.Editable
import android.text.TextWatcher


class LoginViewModel @Inject constructor(val repository: UserRepository) : ObservableViewModel() {
    private val triggerLogin = MutableLiveData<Int>()
    private val email = MutableLiveData<String>()
    private val password = MutableLiveData<String>()


    val user: LiveData<Resource<User>> = Transformations
            .switchMap(triggerLogin) { trigger ->
                if (trigger == null) {
                    AbsentLiveData.create()
                } else {
                    val user = repository.login(email.value!!, password.value!!)
                    user
                }
            }

    fun login(email: String?, password: String?) {
        this.email.value = email
        this.password.value = password
        triggerLogin.value = Random.nextInt(1, 10)
    }

    private var loginForm = LoginForm()

    fun getEmailTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                loginForm.isEmailValid(true)
            }
        }
    }

    fun getPasswordTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                loginForm.isPasswordValid(true)
            }
        }
    }

    fun getLoginForm(): LoginForm? {
        return loginForm
    }

    fun onButtonClick() {
        loginForm.onClick()
    }

    fun getLoginFields(): MutableLiveData<LoginFields>? {
        return loginForm.loginFields
    }

    fun registerAccount(view: View) {
        view.findNavController().navigate(R.id.to_create_account)
    }

}