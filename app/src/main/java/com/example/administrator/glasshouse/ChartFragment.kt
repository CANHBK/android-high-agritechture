package com.example.administrator.glasshouse

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.angmarch.views.NiceSpinner
import java.util.*

class ChartFragment : Fragment() {
    var compositeDisposable: CompositeDisposable? = null
    lateinit var chart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        // thiết lập cho 2 spinner
        val spinPara = view.findViewById(R.id.spinThongSo) as NiceSpinner
        val spintime = view.findViewById(R.id.spinTime) as NiceSpinner
        val timeAdapter = ArrayAdapter.createFromResource(context!!, R.array.Time, android.R.layout.simple_spinner_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spintime.setAdapter(timeAdapter)
        spintime.showArrow()
        val paraAdapter = ArrayAdapter.createFromResource(context!!, R.array.Parameter, android.R.layout.simple_spinner_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinPara.setAdapter(paraAdapter)
        spinPara.showArrow()

        chart = view.findViewById(R.id.chart) as LineChart

        compositeDisposable = CompositeDisposable()

        val mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val userID = mShared.getString(Config.UserId, null)


        // Đổ thử dữ liệu lên LineChart
        getData()

        // Đổ thử dữ liệu lên Server
        // Hàm này cần được xóa đi khi làm thực tế
        // Có hiện tượng dữ liệu gửi liên tục
        // Có thể do vòng đời của fragment
        //addTemp()

        return view
    }

    private fun addTemp() {
        MyApolloClient.getApolloClient().mutate(
                AddTempMutation.builder().value(Random(30).nextLong()).build()
        ).enqueue(object  : ApolloCall.Callback<AddTempMutation.Data>(){
            override fun onResponse(response: Response<AddTempMutation.Data>) {
                Log.d("!tempAdded", response.data()!!.addTemperature()!!.value().toString())
            }

            override fun onFailure(e: ApolloException) {
                Log.d("!tempAdd",e.message)
            }
        })
    }

    private fun getData() {
        val tempSubcription = TempSubcriptionSubscription.builder().build()
        val tempSubscriptionClient = MyApolloClient.getApolloClient().subscribe(tempSubcription)

        // Sử dụng RxJava để tiện xử lý sự kiện
        compositeDisposable!!.add(Rx2Apollo.from(tempSubscriptionClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : SubscriptionSubcriber() {
                    override fun onComplete() {
                        super.onComplete()
                        Log.d("!temp", "onComplete")
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        Log.d("!temp", t!!.message)
                    }

                    override fun onNext(response: Response<TempSubcriptionSubscription.Data>?) {
                        super.onNext(response)
                        activity!!.runOnUiThread {
                            val list = response!!.data()!!.tempAdded!!
                            val yValues : ArrayList<Entry> = ArrayList()
                            var count = 0
                            for (item in  list) {
//                                val item = list[i]
                                //Toast.makeText(context!!,item.value().toString(),Toast.LENGTH_SHORT).show()
                                val value = item.value()
                                val createTime = item.createdAt
                                yValues.add(Entry(count.toFloat(),value!!.toFloat()))
                                count++
                            }
                            val dataSet : ArrayList<ILineDataSet> = ArrayList()
                            val setY = LineDataSet(yValues,"Temperature")
                            setY.fillAlpha = 60
                            setY.lineWidth = 2F
                            setY.color = Color.RED
                            setY.valueTextColor = Color.BLUE
                            dataSet.add(setY)
                            val data = LineData(dataSet)
                            data.notifyDataChanged()
                            chart.data = data
                            Toast.makeText(context!!,"New data arrived!",Toast.LENGTH_SHORT).show()
                            chart.notifyDataSetChanged()
                            // Force Chart refresh to get new data
                            chart.invalidate()
                        }
                    }
                })
        )

    }

}