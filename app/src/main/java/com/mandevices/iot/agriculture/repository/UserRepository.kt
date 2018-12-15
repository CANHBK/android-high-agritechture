package com.mandevices.iot.agriculture.repository

import androidx.lifecycle.LiveData
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.api.GraphQL
import com.mandevices.iot.agriculture.db.UserDao
import com.mandevices.iot.agriculture.vo.Resource
import com.mandevices.iot.agriculture.vo.User
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val userDao: UserDao,
        private val graphQL: GraphQL

) : LiveData<User>() {

    fun loadUser(
            userId: String
    ): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.update(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true
            }

            override fun loadFromDb() = userDao.loadUser()

            override fun createCall() = graphQL.loadUser(userId)
        }.asLiveData()

    }

    fun login(
            email: String, password: String
    ): LiveData<Resource<User>> {

        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.resetTable()
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true
            }

            override fun loadFromDb() = userDao.loadUser()

            override fun createCall() = graphQL.login(email, password)
        }.asLiveData()

    }

    fun register(
            email: String, name: String, password: String
    ): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.resetTable()
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true
            }

            override fun loadFromDb() = userDao.loadUser()

            override fun createCall() = graphQL.register(email, name, password)
        }.asLiveData()

    }
}