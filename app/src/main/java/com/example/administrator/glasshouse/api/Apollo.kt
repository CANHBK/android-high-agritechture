package com.example.administrator.glasshouse.api

import androidx.lifecycle.LiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.GetAllGateOfUserQuery
import com.example.administrator.glasshouse.LoginMutation
import com.example.administrator.glasshouse.RegisterMutation
import com.example.administrator.glasshouse.type.UserInput
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.User
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class Apollo @Inject constructor(private val apolloClient: ApolloClient) : GraphQL, LiveData<ApiResponse<User>>() {

    override fun login(email: String, password: String): LiveData<ApiResponse<User>> {
        val mutation = apolloClient.mutate(
                LoginMutation.builder()
                        .email(email)
                        .password(password)
                        .build())
        return object : LiveData<ApiResponse<User>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<LoginMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.login()!!
                                val result = User(data.id()!!, data.name()!!, data.email())
                                postValue(ApiResponse.create(result))
                            } else {
                                postValue(ApiResponse.createError(errors[0].message()!!))
                            }


                        }

                    })
                }

            }

        }
    }

    override fun register(email: String, name: String, password: String): LiveData<ApiResponse<User>> {
        val userInput = UserInput.builder()
                .email(email)
                .name(name)
                .password(password)
                .build()
        val mutation = apolloClient.mutate(
                RegisterMutation.builder()
                        .params(userInput)
                        .build())
        return object : LiveData<ApiResponse<User>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<RegisterMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<RegisterMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.register()!!
                                val result = User(data.id()!!, data.name()!!, data.email())
                                postValue(ApiResponse.create(result))
                            } else {
                                postValue(ApiResponse.createError(errors[0].message()!!))
                            }

                        }

                    })
                }
            }

        }
    }

    override fun loadGates(userId: String): LiveData<ApiResponse<List<Gate>>> {
        val query = GetAllGateOfUserQuery.builder().userID(userId).build()
        val call = apolloClient.query(query)
        return object : LiveData<ApiResponse<List<Gate>>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : ApolloCall.Callback<GetAllGateOfUserQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<GetAllGateOfUserQuery.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.allGatesOfUser()!!
                                val result = ArrayList<Gate>()
                                data.forEach {
                                    val gate = Gate("id", it.name()!!, it.serviceTag(), it.monitors().toString(), it.controls().toString(), userId)
                                    result.add(gate)
                                }
                                postValue(ApiResponse.create(result))
                            } else {
                                postValue(ApiResponse.createError(errors[0].message()!!))
                            }


                        }

                    })
                }

            }

        }

    }


}