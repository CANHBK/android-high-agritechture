package com.mandevices.iot.agriculture.repository

import androidx.lifecycle.LiveData
import com.mandevices.iot.agriculture.api.GraphQL
import com.mandevices.iot.agriculture.db.MonitorDao
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.NetworkState
import com.mandevices.iot.agriculture.util.RateLimiter
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MonitorRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val monitorDao: MonitorDao,
        private val graphQL: GraphQL,
        private val networkState: NetworkState

) : LiveData<Monitor>() {

    private val repoListRateLimit = RateLimiter<String>(1, TimeUnit.MINUTES)

    fun loadMonitors(serviceTag: String): LiveData<Resource<List<Monitor>>> {
        return object : NetworkBoundResource<List<Monitor>, List<Monitor>>(appExecutors) {
            override fun saveCallResult(item: List<Monitor>) {
                monitorDao.insertList(item)
            }

            override fun shouldFetch(data: List<Monitor>?): Boolean {
                return networkState.hasInternet() && (data == null || data.isEmpty() || repoListRateLimit.shouldFetch(serviceTag))
            }

            override fun loadFromDb() = monitorDao.loadMonitors(serviceTag)

            override fun createCall() = graphQL.loadMonitors(serviceTag = serviceTag)

        }.asLiveData()

    }

    fun addMonitor(serviceTag: String, tag: String, name: String): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, Monitor>(appExecutors) {
            override fun saveCallResult(item: Monitor) {
                monitorDao.insert(item)

            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.addMonitor(serviceTag = serviceTag, tag = tag, name = name)

        }.asLiveData()

    }

    fun deleteMonitor(tag: String): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, Monitor>(appExecutors) {
            override fun saveCallResult(item: Monitor) {
                monitorDao.delete(item)

            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.deleteMonitor(tag = tag)

        }.asLiveData()

    }

    fun editMonitor(tag: String, name: String): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, Monitor>(appExecutors) {
            override fun saveCallResult(item: Monitor) {
                monitorDao.update(item)
            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.editMonitor(tag = tag, name = name)

        }.asLiveData()

    }
}