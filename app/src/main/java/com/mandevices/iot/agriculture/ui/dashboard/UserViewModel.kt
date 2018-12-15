package com.mandevices.iot.agriculture.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mandevices.iot.agriculture.repository.UserRepository
import com.mandevices.iot.agriculture.util.AbsentLiveData
import com.mandevices.iot.agriculture.util.ObservableViewModel
import com.mandevices.iot.agriculture.vo.Const
import com.mandevices.iot.agriculture.vo.Resource
import com.mandevices.iot.agriculture.vo.User
import io.paperdb.Paper
import javax.inject.Inject
import kotlin.random.Random

class UserViewModel @Inject constructor(repository: UserRepository) : ObservableViewModel() {

    private val triggerLoadUser = MutableLiveData<Int>()

    private val refresh = MutableLiveData<Boolean>()

    private val _userId = MutableLiveData<String>()


    val user: LiveData<Resource<User>> = Transformations
            .switchMap(triggerLoadUser) { it ->
                if (it == null) {
                    AbsentLiveData.create()
                } else {
                    repository.loadUser(_userId.value!!)
                }
            }


    fun loadUser(refresh: Boolean = false) {
        _userId.value = Paper.book().read(Const.USER_ID)
        this.refresh.value = refresh
        triggerLoadUser.value = Random.nextInt(1, 10)
    }


}