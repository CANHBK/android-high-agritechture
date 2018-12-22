package com.mandevices.iot.agriculture.api

import androidx.lifecycle.LiveData
import com.mandevices.iot.agriculture.vo.*

interface GraphQL {

    fun loadGates(userId: String): LiveData<ApiResponse<List<Gate>>>
    fun addGate(userId: String, idGate: String, gateName: String): LiveData<ApiResponse<Gate>>
    fun deleteGate(userId: String, idGate: String): LiveData<ApiResponse<Gate>>
    fun editGate(userId: String, idGate: String, gateName: String): LiveData<ApiResponse<Gate>>

    fun loadMonitors(serviceTag: String): LiveData<ApiResponse<List<Monitor>>>
    fun configTimeMonitor(
            serviceTag: String,
            monitorTag: String,
            index: String,
            isAuto: Boolean,
            minute: String,
            hour: String
    ): LiveData<ApiResponse<Monitor>>

    fun addMonitor(serviceTag: String, tag: String, name: String): LiveData<ApiResponse<Monitor>>
    fun getMonitorParams(serviceTag: String, tag: String, params: List<String>): LiveData<ApiResponse<Monitor>>
    fun getNewestMonitorData(tag: String): LiveData<ApiResponse<Monitor>>
    fun deleteMonitor(tag: String): LiveData<ApiResponse<Monitor>>
    fun editMonitor(tag: String, name: String): LiveData<ApiResponse<Monitor>>
    fun getMonitorDataByDate(tag: String, year: Int, month: Int, day: Int): LiveData<ApiResponse<SensorData>>


    fun loadControls(serviceTag: String): LiveData<ApiResponse<List<Control>>>
    fun addControl(serviceTag: String, tag: String, name: String): LiveData<ApiResponse<Control>>
    fun configTimeControl(serviceTag: String, controlTag: String,
                          index: Int, isRepeat: Boolean, name: String, onHour: String, onMinute: String,
                          offHour: String, offMinute: String): LiveData<ApiResponse<Control>>

    fun deleteControl(tag: String): LiveData<ApiResponse<Control>>
    fun editControl(tag: String, name: String): LiveData<ApiResponse<Control>>
    fun setState(index: Int, tag: String, state: String): LiveData<ApiResponse<Control>>

    fun login(email: String, password: String): LiveData<ApiResponse<User>>
    fun register(email: String, name: String, password: String): LiveData<ApiResponse<User>>
    fun loadUser(userId: String): LiveData<ApiResponse<User>>

    fun subscribeStateRelay(controlTag: String)
}
