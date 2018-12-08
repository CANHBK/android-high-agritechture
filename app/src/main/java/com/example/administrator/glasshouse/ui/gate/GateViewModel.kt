package com.example.administrator.glasshouse.ui.gate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.example.administrator.glasshouse.R.string.login
import com.example.administrator.glasshouse.repository.GateRepository
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Resource
import javax.inject.Inject

class GateViewModel @Inject constructor(repository: GateRepository) : ViewModel() {
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    val gates: LiveData<Resource<List<Gate>>> = Transformations
            .switchMap(_userId) { userId ->
                if (userId == null) {
                    AbsentLiveData.create()
                } else {
                    repository.loadGates(userId)
                }
            }

    fun setUserId(userId: String?) {
        if (_userId.value != userId) {
            _userId.value = userId
        }
    }
}