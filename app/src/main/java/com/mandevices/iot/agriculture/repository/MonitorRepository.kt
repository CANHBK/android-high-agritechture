package com.mandevices.iot.agriculture.repository

import androidx.lifecycle.LiveData
import com.mandevices.iot.agriculture.api.GraphQL
import com.mandevices.iot.agriculture.db.MonitorDao
import com.mandevices.iot.agriculture.db.SensorDataDao
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.NetworkState
import com.mandevices.iot.agriculture.util.RateLimiter
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Resource
import com.mandevices.iot.agriculture.vo.SensorData
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MonitorRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val monitorDao: MonitorDao,
        private val sensorDataDao: SensorDataDao,
        private val graphQL: GraphQL,
        private val networkState: NetworkState

) : LiveData<Monitor>() {

    private val repoListRateLimit = RateLimiter<String>(1, TimeUnit.MINUTES)


    fun getMonitorDataByDate(tag: String, year: Int, month: Int, day: Int): LiveData<Resource<SensorData>> {
        return object : NetworkBoundResource<SensorData, SensorData>(appExecutors) {
            override fun saveCallResult(item: SensorData) {
                sensorDataDao.insert(item)
            }

            override fun shouldFetch(data: SensorData?): Boolean {
                return networkState.hasInternet()
            }

            override fun loadFromDb() = sensorDataDao.loadSensorDataByDate(tag = tag, year = year, month = month, day = day)

            override fun createCall() = graphQL.getMonitorDataByDate(tag = tag, year = year, month = month, day = day)

        }.asLiveData()

    }

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

    fun getMonitorParams(serviceTag: String, tag: String, params: List<String> = listOf<String>("1","2","3","4")): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, Monitor>(appExecutors) {
            override fun saveCallResult(item: Monitor) {
                monitorDao.update(item)

            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.getMonitorParams(serviceTag = serviceTag, tag = tag, params = params)

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