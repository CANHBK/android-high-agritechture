package com.mandevices.iot.agriculture.repository

import androidx.lifecycle.LiveData
import com.mandevices.iot.agriculture.api.GraphQL
import com.mandevices.iot.agriculture.db.MonitorDao
import com.mandevices.iot.agriculture.db.MonitorWithSensorsDao
import com.mandevices.iot.agriculture.db.SensorDao
import com.mandevices.iot.agriculture.db.SensorDataDao
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.NetworkState
import com.mandevices.iot.agriculture.util.RateLimiter
import com.mandevices.iot.agriculture.vo.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MonitorRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val monitorDao: MonitorDao,
        private val sensorDataDao: SensorDataDao,
        private val monitorWithSensorsDao: MonitorWithSensorsDao,
        private val graphQL: GraphQL,
        private val sensorDao: SensorDao,
        private val networkState: NetworkState

) : LiveData<Monitor>() {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)


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

    fun loadMonitors(serviceTag: String): LiveData<Resource<List<MonitorWithSensors>>> {
        return object : NetworkBoundResource<List<MonitorWithSensors>, List<MonitorWithSensorsModel>>(appExecutors) {
            override fun saveCallResult(item: List<MonitorWithSensorsModel>) {
                monitorDao.deleteAllRecord()
                sensorDao.deleteAllRecord()
                for (o in item) {
                    monitorDao.insert(o.monitor)
                    sensorDao.insertList(o.sensors!!)

                }

            }

            override fun shouldFetch(data: List<MonitorWithSensors>?): Boolean {
                return networkState.hasInternet() && (data == null || data.isEmpty() || repoListRateLimit.shouldFetch(serviceTag))
            }

            override fun loadFromDb() = monitorWithSensorsDao.monitorWithSensors()

            override fun createCall() = graphQL.loadMonitors(serviceTag = serviceTag)

        }.asLiveData()

    }

    fun getMonitorParams(serviceTag: String, tag: String, params: List<String> = listOf<String>("1", "2", "3", "4")): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, List<Sensor>>(appExecutors) {
            override fun saveCallResult(item: List<Sensor>) {
//                monitorDao.updateMonitorData(sensors = item.sensors!!,tag = item.tag)
                sensorDao.updateList(item)

            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.getMonitorParams(serviceTag = serviceTag, tag = tag, params = params)

        }.asLiveData()

    }

    fun getNewestMonitorData(tag: String): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, Monitor>(appExecutors) {
            override fun saveCallResult(item: Monitor) {
                monitorDao.update(item)

            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.getNewestMonitorData(tag = tag)

        }.asLiveData()

    }

    fun configTimeMonitor(
            serviceTag: String,
            monitorTag: String,
            index: String,
            isPeriodic: Boolean,
            minute: String,
            hour: String)
            : LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, Sensor>(appExecutors) {
            override fun saveCallResult(item: Sensor) {
                sensorDao.update(item)

            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(monitorTag)

            override fun createCall() = graphQL.configTimeMonitor(
                    serviceTag = serviceTag,
                    monitorTag = monitorTag,
                    index = index,
                    isPeriodic = isPeriodic,
                    minute = minute,
                    hour = hour)

        }.asLiveData()

    }

    fun addMonitor(serviceTag: String, tag: String, name: String): LiveData<Resource<Monitor>> {
        return object : NetworkBoundResource<Monitor, MonitorWithSensorsModel>(appExecutors) {
            override fun saveCallResult(item: MonitorWithSensorsModel) {
                monitorDao.insert(item.monitor)
                sensorDao.insertList(item.sensors!!)

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
                monitorDao.updateNameByTag(name = item.name, tag = item.tag)
            }

            override fun shouldFetch(data: Monitor?): Boolean {
                return true
            }

            override fun loadFromDb() = monitorDao.loadMonitor(tag)

            override fun createCall() = graphQL.editMonitor(tag = tag, name = name)

        }.asLiveData()

    }
}