package com.example.administrator.glasshouse


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.administrator.glasshouse.Adapter.RecyclerMultiAdapter
import com.example.administrator.glasshouse.Adapter.RecyclerOverviewAdapter
import com.example.administrator.glasshouse.ModelTest.Datacheck_1
import com.example.administrator.glasshouse.ModelTest.Datacheck_2


/**
 * A simple [Fragment] subclass.
 *
 */
class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        val recycle_multi = view.findViewById(R.id.recycler_multi) as RecyclerView
        val recycle_overview = view.findViewById(R.id.recycler_Overview) as RecyclerView

        recycle_multi.layoutManager = LinearLayoutManager(context!!,LinearLayout.HORIZONTAL,false)
        recycle_overview.layoutManager = LinearLayoutManager(context!!,LinearLayout.VERTICAL,false)

        val checkList_1 = ArrayList<Datacheck_1>()
        checkList_1.add(Datacheck_1("node_1","20","30%","99 lx",10))
        checkList_1.add(Datacheck_1("node_2","30","40%","209 lx",30))
        checkList_1.add(Datacheck_1("node_3","21","50%","309 lx",50))
        checkList_1.add(Datacheck_1("node_4","22","60%","409 lx",70))
        checkList_1.add(Datacheck_1("node_5","24","70%","509 lx",100))

        val checkList_2 = ArrayList<Datacheck_2>()
        checkList_2.add(Datacheck_2("Quat_1",true))
        checkList_2.add(Datacheck_2("Quat_2",false))
        checkList_2.add(Datacheck_2("Quat_3",false))
        checkList_2.add(Datacheck_2("Quat_4",true))
        checkList_2.add(Datacheck_2("Quat_5",true))
        checkList_2.add(Datacheck_2("Quat_6",false))
        checkList_2.add(Datacheck_2("Quat_7",true))

        val multiAdapter = RecyclerMultiAdapter(checkList_2)
        val overviewAdapter = RecyclerOverviewAdapter(checkList_1,context!!)
        recycle_multi.adapter = multiAdapter
        recycle_overview.adapter = overviewAdapter
        return view
    }


}
