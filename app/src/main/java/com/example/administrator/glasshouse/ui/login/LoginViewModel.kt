package com.example.administrator.glasshouse.ui.login

import android.view.View
import android.widget.EditText
import androidx.annotation.VisibleForTesting
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class LoginViewModel @Inject constructor(val repository: UserRepository) : ViewModel() {
    private val _userId = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()


    val user: LiveData<Resource<User>> = Transformations
            .switchMap(_userId) { userId ->
                if (userId == null) {
                    AbsentLiveData.create()
                } else {
                    val user = repository.login(_email.value!!, _password.value!!)
                    user
                }
            }

    fun login(email: String, password: String) {
        _email.value = email
        _password.value = password
        if (_userId.value != email) {
            _userId.value = email
        }

    }

}