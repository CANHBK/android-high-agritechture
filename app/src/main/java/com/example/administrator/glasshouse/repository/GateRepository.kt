package com.example.administrator.glasshouse.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.api.GraphQL
import com.example.administrator.glasshouse.db.GateDao
import com.example.administrator.glasshouse.util.NetworkState
import com.example.administrator.glasshouse.util.RateLimiter
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Resource
import java.util.concurrent.TimeUnit

import javax.inject.Inject


class GateRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val gateDao: GateDao,
        private val graphQL: GraphQL,
        private val networkState: NetworkState

) : LiveData<Gate>() {

    private val repoListRateLimit = RateLimiter<String>(1, TimeUnit.MINUTES)

    fun loadGates(userId: String): LiveData<Resource<List<Gate>>> {
        return object : NetworkBoundResource<List<Gate>, List<Gate>>(appExecutors) {
            override fun saveCallResult(item: List<Gate>) {
                gateDao.updateList(item)
            }

            override fun shouldFetch(data: List<Gate>?): Boolean {
                return networkState.hasInternet() && (data == null || data.isEmpty() || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGates(userId)

            override fun createCall() = graphQL.loadGates(userId)

        }.asLiveData()

    }

    fun addGate(userId: String, idGate: String, gateName: String): LiveData<Resource<Gate>> {
        return object : NetworkBoundResource<Gate, Gate>(appExecutors) {
            override fun saveCallResult(item: Gate) {
                gateDao.insert(item)
//                gateDao.insertGate(item)
            }

            override fun shouldFetch(data: Gate?): Boolean {
                return networkState.hasInternet() && (data == null || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGate(idGate)

            override fun createCall() = graphQL.addGate(userId, idGate, gateName)

        }.asLiveData()

    }

    fun removeGate(userId: String, idGate: String): LiveData<Resource<Gate>> {
        return object : NetworkBoundResource<Gate, Gate>(appExecutors) {
            override fun saveCallResult(item: Gate) {
                gateDao.delete(item)
//                gateDao.deleteByServiceTag(item.serviceTag)
            }

            override fun shouldFetch(data: Gate?): Boolean {
                return networkState.hasInternet() && (data == null || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGate(idGate)

            override fun createCall() = graphQL.removeGate(userId, idGate)

        }.asLiveData()

    }

    fun editGate(userId: String, idGate: String, gateName: String): LiveData<Resource<Gate>> {
        return object : NetworkBoundResource<Gate, Gate>(appExecutors) {
            override fun saveCallResult(item: Gate) {
                gateDao.update(item)
//                gateDao.updateGate(gateName, idGate)
            }

            override fun shouldFetch(data: Gate?): Boolean {
                return networkState.hasInternet() && (data == null || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGate(idGate)

            override fun createCall() = graphQL.editGate(userId, idGate, gateName)

        }.asLiveData()

    }
}