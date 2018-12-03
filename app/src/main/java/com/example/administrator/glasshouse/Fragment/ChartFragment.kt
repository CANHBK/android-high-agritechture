package com.example.administrator.glasshouse.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.Utils.Config
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.disposables.CompositeDisposable
import org.angmarch.views.NiceSpinner

class ChartFragment : androidx.fragment.app.Fragment() {
    var compositeDisposable: CompositeDisposable? = null
    lateinit var chart: LineChart
    var yValues: ArrayList<Entry> = ArrayList()
    var dataSet: ArrayList<ILineDataSet> = ArrayList()
    lateinit var setY: LineDataSet
    lateinit var data: LineData
    var count = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_monitor_detail, container, false)
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

        chart = view.findViewById<View>(R.id.chart) as LineChart

        // Lấy những liệu gần nhất đổ lên chart
        compositeDisposable = CompositeDisposable()
        //getNewData()
        //getLastestTemp()



        val mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val userID = mShared.getString(Config.UserId, null)


        // Đổ thử dữ liệu lên LineChart


        // Đổ thử dữ liệu lên Server
        // Hàm này cần được xóa đi khi làm thực tế
        // Có hiện tượng dữ liệu gửi liên tục
        // Có thể do vòng đời của fragment


        return view
    }

    private fun getLastestTemp() {
//        val getLastestCall = MyApolloClient.getApolloClient().query(
//                GetLastestTempQuery.builder().build())
//
//        compositeDisposable!!.add(Rx2Apollo.from(getLastestCall)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSingleObserver<Response<GetLastestTempQuery.Data>>(){
//                    override fun onSuccess(t: Response<GetLastestTempQuery.Data>) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                    }
//
//
//                    override fun onError(e: Throwable) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                    }
//
//                }))
//        MyApolloClient.getApolloClient().query(
//                GetLastestTempQuery.builder().build())
//                .enqueue(object : ApolloCall.Callback<GetLastestTempQuery.Data>() {
//                    override fun onFailure(e: ApolloException) {
//                        Log.d("!getTemp", e.message)
//                    }
//
//                    override fun onResponse(response: Response<GetLastestTempQuery.Data>) {
//                        activity!!.runOnUiThread {
//                            val list = response.data()!!.temps()!!
//                            for (item in list) {
//                                val value = item!!.value()!!
//                                val createdAt = item.createdAt()
//                                yValues.add(Entry(count.toFloat(), value.toFloat()))
//                                count++
//                            }
//                            Log.d("!getTemp", yValues[0].y.toString())
//                            setY = LineDataSet(yValues, "Temperature")
//                            setY.fillAlpha = 60
//                            setY.lineWidth = 2F
//                            setY.color = Color.RED
//                            setY.valueTextColor = Color.BLUE
//                            dataSet.add(setY)
//                            data = LineData(dataSet)
//                            data.notifyDataChanged()
//                            chart.data = data
//                            chart.notifyDataSetChanged()
//                            // Force Chart refresh to get new data
//                            chart.invalidate()
//                        }
//                    }
//                })
//    }

//    private fun getNewData() {
//        val tempSubcription = TempSubscription.builder().build()
//        val tempSubscriptionClient = MyApolloClient.getApolloClient().subscribe(tempSubcription)
//
//        // Sử dụng RxJava để tiện xử lý sự kiện
//        compositeDisposable!!.add(Rx2Apollo.from(tempSubscriptionClient)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSubscriber<Response<TempSubscription.Data>>() {
//                    override fun onComplete() {
//                        //super.onComplete()
//                        Log.d("!temp", "onComplete")
//                    }
//
//                    override fun onError(t: Throwable?) {
//                        //super.onError(t)
//                        Log.d("!temp", t!!.message)
//                    }
//
//                    override fun onNext(response: Response<TempSubscription.Data>) {
//                        //super.onNext(response)
//                        activity!!.runOnUiThread {
//                        Log.d("!temp","on Next")
//                        val value = response.data()!!.tempAdded()!!.value()
//                        val createdAt = response.data()!!.tempAdded()!!.createdAt()
//                        yValues.add(Entry(count++.toFloat(), value!!.toFloat()))
//                        setY = LineDataSet(yValues, "Temperature")
//                        //setY.fillAlpha = 60
//                        //setY.lineWidth = 2F
//                        //setY.color = Color.RED
//                        //setY.valueTextColor = Color.BLUE
//                        dataSet.add(setY)
//                        data = LineData(dataSet)
//                        data.notifyDataChanged()
//                        chart.data = data
//                        Snackbar.make(context!!, "New data arrived!", Snackbar.LENGTH_SHORT).show()
//                        chart.notifyDataSetChanged()
//                        // Force Chart refresh to get new data
//                        chart.invalidate()
//                        }
//                    }
//                })
//        )
//
    }

}