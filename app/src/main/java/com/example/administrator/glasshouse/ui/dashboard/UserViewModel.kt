package com.example.administrator.glasshouse.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.administrator.glasshouse.repository.GateRepository
import com.example.administrator.glasshouse.repository.UserRepository
import com.example.administrator.glasshouse.ui.dashboard.model.AddGateFields
import com.example.administrator.glasshouse.ui.dashboard.model.AddGateForm
import com.example.administrator.glasshouse.ui.dashboard.model.EditGateFields
import com.example.administrator.glasshouse.ui.dashboard.model.EditGateForm
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.util.ObservableViewModel
import com.example.administrator.glasshouse.vo.Const
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Resource
import com.example.administrator.glasshouse.vo.User
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