package com.example.administrator.glasshouse.ui.register

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.ui.register.model.RegisterFields
import com.example.administrator.glasshouse.ui.register.model.RegisterForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import javax.inject.Inject
import kotlin.random.Random

class RegisterViewModel @Inject constructor(repository: UserRepository) : ViewModel() {

    private val triggerRegister = MutableLiveData<Int>()
    private val email = MutableLiveData<String>()
    private val name = MutableLiveData<String>()
    private val password = MutableLiveData<String>()

    private var registerForm = RegisterForm()

    val user: LiveData<Resource<User>> = Transformations
            .switchMap(triggerRegister) { trigger ->
                if (trigger == null) {
                    AbsentLiveData.create()
                } else {
                    val user = repository.register(email.value!!, name.value!!, password.value!!)


                    user
                }
            }

//    fun getEmailTextWatcher(): TextWatcher {
//        return object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                // Do nothing.
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                registerForm.isEmailValid(true)
//            }
//        }
//    }

//    fun getNameTextWatcher(): TextWatcher {
//        return object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                // Do nothing.
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                registerForm.isNameValid(true)
//            }
//        }
//    }

//    fun getPasswordTextWatcher(): TextWatcher {
//        return object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                // Do nothing.
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                registerForm.isPasswordValid(true)
//            }
//        }
//    }
//
//    fun getRePasswordTextWatcher(): TextWatcher {
//        return object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                // Do nothing.
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                registerForm.isRePasswordValid(true)
//            }
//        }
//    }

    fun getRegisterForm(): RegisterForm? {
        return registerForm
    }


    fun onRegisterClick() {
        registerForm.isNameValid(true)
        registerForm.isEmailValid(true)
        registerForm.isPasswordValid(true)
        registerForm.isRePasswordValid(true)
        registerForm.onClick()
    }


    fun getRegisterFields(): MutableLiveData<RegisterFields>? {
        return registerForm.registerFields
    }

    fun login(view: View) {
        view.findNavController().popBackStack()
    }

    fun register(email: String, name: String, password: String) {
        this.email.value = email
        this.password.value = password
        this.name.value = name
        triggerRegister.value = Random.nextInt(1, 10)
    }
}