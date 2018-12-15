package com.example.administrator.glasshouse.api

import androidx.lifecycle.LiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.*
import com.example.administrator.glasshouse.type.ServiceInput
import com.example.administrator.glasshouse.type.UserInput
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Monitor
import com.example.administrator.glasshouse.vo.User
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class Apollo @Inject constructor(private val apolloClient: ApolloClient) : GraphQL, LiveData<ApiResponse<User>>() {

    override fun subscribeStateRelay(controlTag: String) {

    }

    override fun loadMonitors(serviceTag: String): LiveData<ApiResponse<List<Monitor>>> {
        val query = AllMonitorsQuery
                .builder()
                .serviceTag(serviceTag)
                .build()
        val call = apolloClient.query(query)
        return object : LiveData<ApiResponse<List<Monitor>>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : ApolloCall.Callback<AllMonitorsQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<AllMonitorsQuery.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.allMonitors()!!
                                val result = ArrayList<Monitor>()
                                data.forEach {
                                    val monitor = Monitor(
                                            id = it.id()!!,
                                            name = it.name()!!,
                                            serviceTag = it.serviceTag()!!,
                                            tag = it.tag()!!

                                    )
                                    result.add(monitor)
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

    override fun addMonitor(serviceTag: String, tag: String, name: String): LiveData<ApiResponse<Monitor>> {
        val mutation = apolloClient.mutate(
                AddMonitorMutation.builder()
                        .serviceTag(serviceTag)
                        .tag(tag)
                        .name(name)
                        .build()
        )
        return object : LiveData<ApiResponse<Monitor>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<AddMonitorMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<AddMonitorMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.addMonitor()!!
                                val result = Monitor(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!)
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

    override fun deleteMonitor(tag: String): LiveData<ApiResponse<Monitor>> {
        val mutation = apolloClient.mutate(
                DeleteMonitorMutation.builder()
                        .tag(tag)
                        .build()
        )
        return object : LiveData<ApiResponse<Monitor>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<DeleteMonitorMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<DeleteMonitorMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.deleteMonitor()!!
                                val result = Monitor(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!)
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

    override fun editMonitor(tag: String, name: String): LiveData<ApiResponse<Monitor>> {
        val mutation = apolloClient.mutate(
                EditMonitorMutation.builder()
                        .tag(tag)
                        .name(name)
                        .build()
        )
        return object : LiveData<ApiResponse<Monitor>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<EditMonitorMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<EditMonitorMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.editMonitor()!!
                                val result = Monitor(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!)
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

    override fun deleteGate(userId: String, idGate: String): LiveData<ApiResponse<Gate>> {

        val mutation = apolloClient.mutate(
                RemoveGateMutation.builder()
                        .userId(userId)
                        .serviceTag(idGate)
                        .build()
        )
        return object : LiveData<ApiResponse<Gate>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<RemoveGateMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<RemoveGateMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.removeGate()!!
                                val result = Gate(id = data.id()!!, name = data.name()!!, owner = data.owner(), serviceTag = data.serviceTag())
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

    override fun editGate(userId: String, idGate: String, gateName: String): LiveData<ApiResponse<Gate>> {
        val mutation = apolloClient.mutate(
                EditGateMutation.builder()
                        .userId(userId)
                        .serviceTag(idGate)
                        .name(gateName)
                        .build()
        )
        return object : LiveData<ApiResponse<Gate>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<EditGateMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<EditGateMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.editGate()!!
                                val result = Gate(id = data.id()!!, name = data.name()!!, owner = data.owner(), serviceTag = data.serviceTag())
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

    override fun addGate(userId: String, idGate: String, gateName: String): LiveData<ApiResponse<Gate>> {
        val input = ServiceInput.builder()
                .serviceTag(idGate)
                .name(gateName)
                .userId(userId).build()

        val mutation = apolloClient.mutate(
                AddGateMutation.builder()
                        .params(input)
                        .build()
        )
        return object : LiveData<ApiResponse<Gate>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<AddGateMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<AddGateMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.addGate()!!
                                val result = Gate(id = data.id()!!, name = data.name()!!, owner = data.owner(), serviceTag = data.serviceTag())
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
                                    val gate = Gate(
                                            id = it.id()!!, name = it.name()!!,
                                            serviceTag = it.serviceTag(),
                                            monitors = it.monitors().toString(),
                                            controls = it.controls().toString(),
                                            owner = it.owner()
                                    )
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