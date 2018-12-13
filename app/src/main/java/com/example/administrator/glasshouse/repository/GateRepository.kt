package com.example.administrator.glasshouse.repository

import androidx.lifecycle.LiveData
import com.example.administrator.glasshouse.util.AppExecutors
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

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadGates(userId: String,refresh:Boolean=false): LiveData<Resource<List<Gate>>> {
        return object : NetworkBoundResource<List<Gate>, List<Gate>>(appExecutors) {
            override fun saveCallResult(item: List<Gate>) {
                gateDao.insertList(item)
            }

            override fun shouldFetch(data: List<Gate>?): Boolean {
                return (networkState.hasInternet() && (data == null || data.isEmpty() || repoListRateLimit.shouldFetch(userId)))||(refresh&&networkState.hasInternet())

            }

            override fun loadFromDb() = gateDao.loadGates(userId)

            override fun createCall() = graphQL.loadGates(userId)

        }.asLiveData()

    }

    fun addGate(userId: String, idGate: String, gateName: String): LiveData<Resource<Gate>> {
        return object : NetworkBoundResource<Gate, Gate>(appExecutors) {
            override fun saveCallResult(item: Gate) {
                gateDao.insert(item)
            }

            override fun shouldFetch(data: Gate?): Boolean {
                return networkState.hasInternet() && (data == null || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGate(idGate)

            override fun createCall() = graphQL.addGate(userId, idGate, gateName)

        }.asLiveData()

    }

    fun deleteGate(userId: String, idGate: String): LiveData<Resource<Gate>> {
        return object : NetworkBoundResource<Gate, Gate>(appExecutors) {
            override fun saveCallResult(item: Gate) {
                gateDao.delete(item)
            }

            override fun shouldFetch(data: Gate?): Boolean {
                return networkState.hasInternet() && (data == null || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGate(idGate)

            override fun createCall() = graphQL.deleteGate(userId, idGate)

        }.asLiveData()

    }

    fun editGate(userId: String, idGate: String, gateName: String): LiveData<Resource<Gate>> {
        return object : NetworkBoundResource<Gate, Gate>(appExecutors) {
            override fun saveCallResult(item: Gate) {
                gateDao.update(item)
            }

            override fun shouldFetch(data: Gate?): Boolean {
                return networkState.hasInternet() && (data == null || repoListRateLimit.shouldFetch(userId))
            }

            override fun loadFromDb() = gateDao.loadGate(idGate)

            override fun createCall() = graphQL.editGate(userId, idGate, gateName)

        }.asLiveData()

    }
}