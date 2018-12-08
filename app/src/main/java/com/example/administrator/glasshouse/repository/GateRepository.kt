package com.example.administrator.glasshouse.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.administrator.glasshouse.AppExecutors
import com.example.administrator.glasshouse.api.GraphQL
import com.example.administrator.glasshouse.db.GateDao
import com.example.administrator.glasshouse.util.RateLimiter
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Resource
import java.util.concurrent.TimeUnit

import javax.inject.Inject



class GateRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val gateDao: GateDao,
        private val graphQL: GraphQL

) : LiveData<Gate>() {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadGates(userId: String): LiveData<Resource<List<Gate>>> {
        return object : NetworkBoundResource<List<Gate>, List<Gate>>(appExecutors) {
            override fun saveCallResult(item: List<Gate>) {
                gateDao.insertGates(item)
            }

            override fun shouldFetch(data: List<Gate>?): Boolean {
                Log.d("!Test", "gate + $data")
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(userId)
            }

            override fun loadFromDb() = gateDao.loadGates(userId)

            override fun createCall() = graphQL.loadGates(userId)

        }.asLiveData()

    }
}