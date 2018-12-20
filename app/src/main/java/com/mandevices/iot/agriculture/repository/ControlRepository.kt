package com.mandevices.iot.agriculture.repository

import androidx.lifecycle.LiveData
import com.google.gson.GsonBuilder
import com.mandevices.iot.agriculture.api.GraphQL
import com.mandevices.iot.agriculture.db.ControlDao
import com.mandevices.iot.agriculture.db.MonitorDao
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.NetworkState
import com.mandevices.iot.agriculture.util.RateLimiter
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Relay
import com.mandevices.iot.agriculture.vo.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ControlRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val controlDao: ControlDao,
        private val graphQL: GraphQL,
        private val networkState: NetworkState

) : LiveData<Control>() {

    private val repoListRateLimit = RateLimiter<String>(1, TimeUnit.MINUTES)

    fun configTimeControl(
            serviceTag: String,index: Int,state:String,
            controlTag: String,isAuto:Boolean,onHour:String,onMinute:String,
            offHour:String,offMinute:String,
            name: String
    ): LiveData<Resource< Control>> {
        return object : NetworkBoundResource<Control, Control>(appExecutors) {
            override fun saveCallResult(item: Control) {
                controlDao.update(item)
            }

            override fun shouldFetch(data: Control?): Boolean {
                return networkState.hasInternet()
            }

            override fun loadFromDb() = controlDao.loadControl(controlTag)

            override fun createCall() = graphQL.configTimeControl(
                    serviceTag = serviceTag,
                    state = state,
                    index = index ,
                    controlTag = controlTag,
                    isAuto = isAuto,
                    onHour = onHour,
                    onMinute = onMinute,
                    offHour = offHour,
                    offMinute = offMinute,
                    name = name)

        }.asLiveData()

    }
    fun setState(serviceTag: String,index: Int,state:String,tag: String): LiveData<Resource< List<Control>>> {
        return object : NetworkBoundResource<List<Control>, Control>(appExecutors) {
            override fun saveCallResult(item: Control) {
                controlDao.update(item)
            }

            override fun shouldFetch(data: List<Control>?): Boolean {
                return networkState.hasInternet()
            }

            override fun loadFromDb() = controlDao.loadControls(serviceTag)

            override fun createCall() = graphQL.setState(index = index,state=state,tag = tag)

        }.asLiveData()

    }
    fun loadControls(serviceTag: String): LiveData<Resource<List<Control>>> {
        return object : NetworkBoundResource<List<Control>, List<Control>>(appExecutors) {
            override fun saveCallResult(item: List<Control>) {
                controlDao.insertList(item)
            }

            override fun shouldFetch(data: List<Control>?): Boolean {
                return networkState.hasInternet()
//                return networkState.hasInternet() && (data == null || data.isEmpty() || repoListRateLimit.shouldFetch(serviceTag))
            }

            override fun loadFromDb() = controlDao.loadControls(serviceTag)

            override fun createCall() = graphQL.loadControls(serviceTag = serviceTag)

        }.asLiveData()

    }

    fun configTimeControl(serviceTag: String, tag: String, name: String): LiveData<Resource<Control>> {
        return object : NetworkBoundResource<Control, Control>(appExecutors) {
            override fun saveCallResult(item: Control) {

                controlDao.insert(item)

            }

            override fun shouldFetch(data: Control?): Boolean {
                return true
            }

            override fun loadFromDb() = controlDao.loadControl(tag)

            override fun createCall() = graphQL.addControl(serviceTag = serviceTag, tag = tag, name = name)

        }.asLiveData()

    }

    fun addControl(serviceTag: String, tag: String, name: String): LiveData<Resource<Control>> {
        return object : NetworkBoundResource<Control, Control>(appExecutors) {
            override fun saveCallResult(item: Control) {

                controlDao.insert(item)

            }

            override fun shouldFetch(data: Control?): Boolean {
                return true
            }

            override fun loadFromDb() = controlDao.loadControl(tag)

            override fun createCall() = graphQL.addControl(serviceTag = serviceTag, tag = tag, name = name)

        }.asLiveData()

    }

    fun deleteControl(tag: String): LiveData<Resource<Control>> {
        return object : NetworkBoundResource<Control, Control>(appExecutors) {
            override fun saveCallResult(item: Control) {
                controlDao.delete(item)

            }

            override fun shouldFetch(data: Control?): Boolean {
                return true
            }

            override fun loadFromDb() = controlDao.loadControl(tag)

            override fun createCall() = graphQL.deleteControl(tag = tag)

        }.asLiveData()

    }

    fun editControl(tag: String, name: String): LiveData<Resource<Control>> {
        return object : NetworkBoundResource<Control, Control>(appExecutors) {
            override fun saveCallResult(item: Control) {
                controlDao.update(item)
            }

            override fun shouldFetch(data: Control?): Boolean {
                return true
            }

            override fun loadFromDb() = controlDao.loadControl(tag)

            override fun createCall() = graphQL.editControl(tag = tag, name = name)

        }.asLiveData()

    }
}