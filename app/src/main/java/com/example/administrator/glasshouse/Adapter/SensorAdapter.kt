package com.example.administrator.glasshouse.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.card.MaterialCardView
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.administrator.glasshouse.*
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.type.AllSensorsInput
import com.example.administrator.glasshouse.type.NodeEnvInput
import com.example.administrator.glasshouse.type.StateRelayInput
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class SensorAdapter(val sensors: List<AllSensorsQuery.AllSensor>, val context: Context, val activity: Activity,val recyclerViewSensor:RecyclerView) : RecyclerView.Adapter<SensorAdapter.ViewHolder>() {
lateinit var view:View;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        view = layoutInflater.inflate(R.layout.item_sensor, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sensors.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        trackNewEnvParam(view,sensors[0].serviceTag()!!,sensors[0].nodeEnv(),holder)
        val sensor = sensors[position];

        holder.txtNameSensor.text = sensor.name()

        holder.txtValue.text = sensor.lastData().toString()
        when (sensor.index()!!.toInt()) {
            1 -> {
                holder.icon.background = context.getDrawable(R.drawable.ic_thermometer_black)
                holder.txtUnit.text = "°C"
                holder.layout.background = context.getDrawable(R.drawable.temp_background)
            }
            2 -> {
                holder.icon.background = context.getDrawable(R.drawable.ic_sun_black)
                holder.txtUnit.text = "lx"
                holder.layout.background = context.getDrawable(R.drawable.light_background)
            }
            3 -> {
                holder.icon.background = context.getDrawable(R.drawable.ic_drop)
                holder.txtUnit.text = "%"
                holder.layout.background = context.getDrawable(R.drawable.doam_background)
            }
            4 -> {
                holder.icon.background = context.getDrawable(R.drawable.ic_thermometer_black)
                holder.txtUnit.text = "%"
                holder.layout.background = context.getDrawable(R.drawable.all_background)
            }
        }

//

    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val txtNameSensor: TextView = item.findViewById<View>(R.id.txt_name_sensor) as TextView
        val txtValue: TextView = item.findViewById<View>(R.id.txt_value) as TextView
        val txtUnit: TextView = item.findViewById<View>(R.id.txt_unit) as TextView
        val layout = item.findViewById<View>(R.id.layout_sensor) as ConstraintLayout
        val icon = item.findViewById<View>(R.id.icon) as ImageView
    }

    private fun trackNewEnvParam(view: View, serviceTag: String, nodeEnv: String,holder: ViewHolder) {
        var compositeDisposable: CompositeDisposable? = null
        compositeDisposable = CompositeDisposable()
        val input = NodeEnvInput.builder().serviceTag(serviceTag).nodeEnv(nodeEnv).build()

        val newEnvSub = AutoUpdatedEnvironmentSubSubscription.builder().params(input).build()
        val envSubscriptionClient = MyApolloClient.getApolloClient().subscribe(newEnvSub)
        // Sử dụng RxJava để tiện xử lý sự kiện
        compositeDisposable.add(Rx2Apollo.from(envSubscriptionClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Response<AutoUpdatedEnvironmentSubSubscription.Data>>() {
                    override fun onComplete() {
                        //super.onComplete()
                        Log.d("!subOnCompelete", "onComplete")
                    }

                    override fun onError(t: Throwable?) {
                        //super.onError(t)
                        Log.d("!subOnError", t!!.message)
                    }

                    override fun onNext(response: Response<AutoUpdatedEnvironmentSubSubscription.Data>) {
                        getSensorData(view, serviceTag, nodeEnv,holder)
                    }
                })
        )
    }

    private fun getSensorData(view: View, serviceTag: String, nodeEnv: String,holder: ViewHolder) {
        val input = AllSensorsInput.builder().serviceTag(serviceTag).nodeEnv(nodeEnv).build()
        MyApolloClient.getApolloClient().query(
                AllSensorsQuery.builder().params(input)
                        .build()
        ).enqueue(object : ApolloCall.Callback<AllSensorsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!SensorAdapter", e.message)
            }

            override fun onResponse(response: Response<AllSensorsQuery.Data>) {
                activity.runOnUiThread {
                    val error = response.errors()
                    if (error.isEmpty()) {
                        val layoutManager = GridLayoutManager(context, 4)
                        recyclerViewSensor.layoutManager = layoutManager
                        val adapter = SensorAdapter(response.data()!!.allSensors()!!, context,activity,recyclerViewSensor)
                        recyclerViewSensor.adapter = adapter
                    } else {
                        Snackbar.make(view, error[0].message()!!, Snackbar.LENGTH_LONG).show()
                    }


                }
            }
        })
    }

}