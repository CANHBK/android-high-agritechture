package com.example.administrator.glasshouse.ui.dashboard

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.administrator.glasshouse.repository.GateRepository
import com.example.administrator.glasshouse.util.AbsentLiveData
import com.example.administrator.glasshouse.util.ObservableViewModel
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Resource
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(repository: GateRepository) : ObservableViewModel() {
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

    fun addGate() {
        Log.d("!Test", "call")
    }

    fun setUserId(userId: String?) {
        if (_userId.value != userId) {
            _userId.value = userId
        }
    }
}