package com.example.administrator.glasshouse


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.administrator.glasshouse.Adapter.MultiAdapter
import com.example.administrator.glasshouse.Adapter.OverviewAdapter
import com.example.administrator.glasshouse.ModelTest.Datacheck_1
import com.example.administrator.glasshouse.ModelTest.Datacheck_2
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class OverviewFragment : Fragment() {
    companion object {
        public fun newInstance(id: Int): OverviewFragment {
            val args = Bundle()
            args.putInt("parameter", id)
            val fragment = OverviewFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        val checkList_1: ArrayList<Datacheck_1> = ArrayList()
        val checkList_2: ArrayList<Datacheck_2> = ArrayList()

        // Phần này có thể bỏ đi khi có Server( đã thay thế bằng SharedPreference)
        // Nhận ID người dùng đã có ở SharePrefểnce
        val mShared = context!!.getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val userID = mShared.getString(Config.UserId,"")
        val id = mShared.getInt(Config.FarmId,0)  // Chọn số ngẫu nhiên
        //Log.d("!idf", userID!!.toString())

        val recycle_multi = view.findViewById(R.id.recycler_multi) as RecyclerView
        val recycle_overview = view.findViewById(R.id.recycler_Overview) as RecyclerView

        recycle_multi.layoutManager = LinearLayoutManager(context!!, LinearLayout.HORIZONTAL, false)
        recycle_overview.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)

        // Phần này có thể bỏ đi khi có Server
        // Đổ dữ liệu khác nhau theo từng ID khác nhau
        when (id) {
            0 -> {
                checkList_1.add(Datacheck_1("node_1", "20", "30", "99", 10))
                checkList_1.add(Datacheck_1("node_2", "30", "40", "209", 60))
                checkList_1.add(Datacheck_1("node_3", "21", "50", "309", 50))
                checkList_1.add(Datacheck_1("node_4", "22", "60", "409", 70))
                checkList_1.add(Datacheck_1("node_5", "24", "70", "509", 100))

                checkList_2.add(Datacheck_2("Quat_1", true))
                checkList_2.add(Datacheck_2("Quat_2", false))
                checkList_2.add(Datacheck_2("Quat_3", false))
                checkList_2.add(Datacheck_2("Quat_4", true))
                checkList_2.add(Datacheck_2("Quat_5", true))
                checkList_2.add(Datacheck_2("Quat_6", false))
                checkList_2.add(Datacheck_2("Quat_7", true))
            }

            1 -> {
                checkList_1.add(Datacheck_1("node_a", "27", "60", "199", 10))
                checkList_1.add(Datacheck_1("node_b", "28", "80", "299", 60))
                checkList_1.add(Datacheck_1("node_c", "29", "95", "399", 50))


                checkList_2.add(Datacheck_2("Quat_a", true))
                checkList_2.add(Datacheck_2("Quat_b", false))
                checkList_2.add(Datacheck_2("Quat_c", false))
            }

            2 -> {
                checkList_1.add(Datacheck_1("node_m", "27", "60", "199", 10))
                checkList_1.add(Datacheck_1("node_n", "28", "80", "299", 60))
                checkList_1.add(Datacheck_1("node_p", "29", "95", "399", 50))


                checkList_2.add(Datacheck_2("Quat_m", true))
                checkList_2.add(Datacheck_2("Quat_n", false))
                checkList_2.add(Datacheck_2("Quat_p", false))
            }
        }

        val multiAdapter = MultiAdapter(checkList_2)
        val overviewAdapter = OverviewAdapter(checkList_1, context!!)
        recycle_multi.adapter = multiAdapter
        recycle_overview.adapter = overviewAdapter
        return view
    }


}
