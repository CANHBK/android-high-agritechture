package com.example.administrator.glasshouse.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
import javax.inject.Inject

class RegisterViewModel @Inject constructor(repository: UserRepository) : ViewModel() {

    private val _userId = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _name = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val user: LiveData<Resource<User>> = Transformations
            .switchMap(_userId) { userId ->
                if (userId == null) {
                    AbsentLiveData.create()
                } else {
                    val user = repository.register(_email.value!!,_name.value!!, _password.value!!)


                    user
                }
            }


    fun register(email: String, name: String, password: String) {
        _email.value = email
        _password.value = password
        _name.value = name
        if (_userId.value != email) {
            _userId.value = email
        }
    }
}