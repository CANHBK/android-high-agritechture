package com.mandevices.iot.agriculture.api

import androidx.lifecycle.LiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mandevices.iot.agriculture.*
import com.mandevices.iot.agriculture.R.id.monitor
import com.mandevices.iot.agriculture.db.RelayDao
import com.mandevices.iot.agriculture.type.ServiceInput
import com.mandevices.iot.agriculture.type.UserInput
import com.mandevices.iot.agriculture.vo.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class Apollo @Inject constructor(
        private val apolloClient: ApolloClient
) : GraphQL, LiveData<ApiResponse<User>>() {
    override fun getMonitorParams(serviceTag: String, tag: String, params: List<String>): LiveData<ApiResponse<Monitor>> {
        val query = GetMonitorParamsQuery.builder()
                .serviceTag(serviceTag)
                .tag(tag)
                .params(params)
                .build()
        val call = apolloClient.query(query)
        return object : LiveData<ApiResponse<Monitor>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : ApolloCall.Callback<GetMonitorParamsQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<GetMonitorParamsQuery.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.monitorParams!!

                                val monitor = Monitor(
                                        id = data.id()!!,
                                        name = data.name()!!,
                                        serviceTag = data.serviceTag()!!,
                                        tag = data.tag()!!,
                                        lastTemp = data.data()!![0].value()!![0],
                                        lastLight = data.data()!![0].value()!![1],
                                        lastAirHumi = data.data()!![0].value()!![2],
                                        lastGndHumi = data.data()!![0].value()!![3]
                                )

                                postValue(ApiResponse.create(monitor))
                            } else {
                                postValue(ApiResponse.createError(errors[0].message()!!))
                            }


                        }

                    })
                }

            }

        }
    }

    override fun getMonitorDataByDate(tag: String, year: Int, month: Int, day: Int): LiveData<ApiResponse<SensorData>> {
        val query = GetMonitorDataByDateQuery
                .builder()
                .tag(tag)
                .year(year)
                .month(month)
                .day(day)
                .build()
        val call = apolloClient.query(query)
        return object : LiveData<ApiResponse<SensorData>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : ApolloCall.Callback<GetMonitorDataByDateQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<GetMonitorDataByDateQuery.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.monitorDataByDate!!


                                val gson = GsonBuilder().setPrettyPrinting().create()

                                val test: String = gson.toJson(data.data())


                                val result = SensorData(id = data.id()!!, content = test, day = data.day(), year = data.year(), month = data.month(), monitorTag = data.tag()!!

                                )

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

    override fun setState(index: Int, tag: String, state: String): LiveData<ApiResponse<Control>> {
        val mutation = apolloClient.mutate(
                SetStateRelayMutation.builder()
                        .index(index)
                        .tag(tag)
                        .state(state)
                        .build()
        )
        return object : LiveData<ApiResponse<Control>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<SetStateRelayMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<SetStateRelayMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.setStateRelay()!!
                                val relaysList = mutableListOf<Relay>()
                                data.relays()!!.forEach {
                                    val relay = Relay(
                                            id = it.id()!!,
                                            index = it.index()!!,
                                            name = it.name()!!,
                                            controlTag = it.controlTag()!!,
                                            serviceTag = it.serviceTag()!!,
                                            minute = it.minute(),
                                            hour = it.hour(),
                                            isPeriodic = it.isPeriodic!!,
                                            state = it.state()!!
                                    )
                                    relaysList.add(relay)
                                }

                                val gson = GsonBuilder().setPrettyPrinting().create()

                                val test: String = gson.toJson(relaysList)
                                val result = Control(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!, relays = test)
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


    override fun loadControls(serviceTag: String): LiveData<ApiResponse<List<Control>>> {
        val query = AllControlsQuery
                .builder()
                .serviceTag(serviceTag)
                .build()
        val call = apolloClient.query(query)
        return object : LiveData<ApiResponse<List<Control>>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : ApolloCall.Callback<AllControlsQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<AllControlsQuery.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.allControls()!!
                                val result = ArrayList<Control>()
                                data.forEach {
                                    val relaysList = mutableListOf<Relay>()
                                    it.relays()!!.forEach {
                                        val relay = Relay(
                                                id = it.id()!!,
                                                index = it.index()!!,
                                                name = it.name()!!,
                                                controlTag = it.controlTag()!!,
                                                serviceTag = it.serviceTag()!!,
                                                minute = it.minute(),
                                                hour = it.hour(),
                                                isPeriodic = it.isPeriodic!!,
                                                state = it.state()!!
                                        )
                                        relaysList.add(relay)
                                    }

                                    val gson = GsonBuilder().setPrettyPrinting().create()

                                    val test: String = gson.toJson(relaysList)


                                    val control = Control(
                                            id = it.id()!!,
                                            name = it.name()!!,
                                            serviceTag = it.serviceTag()!!,
                                            tag = it.tag()!!,
                                            relays = test

                                    )

                                    result.add(control)
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

    override fun addControl(serviceTag: String, tag: String, name: String): LiveData<ApiResponse<Control>> {
        val mutation = apolloClient.mutate(
                AddControlMutation.builder()
                        .serviceTag(serviceTag)
                        .tag(tag)
                        .name(name)
                        .build()
        )
        return object : LiveData<ApiResponse<Control>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<AddControlMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<AddControlMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.addControl()!!
                                val relaysList = mutableListOf<Relay>()
                                data.relays()!!.forEach {
                                    val relay = Relay(
                                            id = it.id()!!,
                                            index = it.index()!!,
                                            name = it.name()!!,
                                            controlTag = it.controlTag()!!,
                                            serviceTag = it.serviceTag()!!,
                                            minute = it.minute(),
                                            hour = it.hour(),
                                            isPeriodic = it.isPeriodic!!,
                                            state = it.state()!!
                                    )
                                    relaysList.add(relay)
                                }

                                val gson = GsonBuilder().setPrettyPrinting().create()

                                val test: String = gson.toJson(relaysList)
                                val result = Control(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!, relays = test)
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

    override fun deleteControl(tag: String): LiveData<ApiResponse<Control>> {
        val mutation = apolloClient.mutate(
                DeleteControlMutation.builder()
                        .tag(tag)
                        .build()
        )
        return object : LiveData<ApiResponse<Control>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<DeleteControlMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<DeleteControlMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.deleteControl()!!
                                val result = Control(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!)
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

    override fun editControl(tag: String, name: String): LiveData<ApiResponse<Control>> {
        val mutation = apolloClient.mutate(
                EditControlMutation.builder()
                        .tag(tag)
                        .name(name)
                        .build()
        )
        return object : LiveData<ApiResponse<Control>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    mutation.enqueue(object : ApolloCall.Callback<EditControlMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<EditControlMutation.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.editControl()!!
                                val result = Control(id = data.id()!!, name = data.name()!!, tag = data.tag()!!, serviceTag = data.serviceTag()!!)
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


    override fun loadUser(userId: String): LiveData<ApiResponse<User>> {
        val query = UserQuery
                .builder()
                .id(userId)
                .build()
        val call = apolloClient.query(query)
        return object : LiveData<ApiResponse<User>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()

                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            postValue(ApiResponse.create(e))
                        }

                        override fun onResponse(response: Response<UserQuery.Data>) {
                            val errors = response.errors()
                            if (errors.isEmpty()) {
                                val data = response.data()!!.user()!!

                                val user = User(
                                        id = data.id()!!,
                                        fullName = data.name()!!,
                                        email = data.email()
                                )

                                postValue(ApiResponse.create(user))
                            } else {
                                postValue(ApiResponse.createError(errors[0].message()!!))
                            }


                        }

                    })
                }

            }

        }
    }

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
                                    if (it.data()!!.size != 0) {
                                        val monitor = Monitor(
                                                id = it.id()!!,
                                                name = it.name()!!,
                                                serviceTag = it.serviceTag()!!,
                                                tag = it.tag()!!,
                                                lastTemp = it.data()!![0].value()!![0],
                                                lastLight = it.data()!![0].value()!![1],
                                                lastAirHumi = it.data()!![0].value()!![2],
                                                lastGndHumi = it.data()!![0].value()!![3]

                                        )
                                        result.add(monitor)
                                    } else {
                                        val monitor = Monitor(
                                                id = it.id()!!,
                                                name = it.name()!!,
                                                serviceTag = it.serviceTag()!!,
                                                tag = it.tag()!!

                                        )
                                        result.add(monitor)
                                    }


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