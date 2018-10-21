package com.example.administrator.glasshouse.Adapter

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.administrator.glasshouse.ModelTest.Datacheck_1
import com.example.administrator.glasshouse.R

class RecyclerOverviewAdapter(val dataList : ArrayList<Datacheck_1>,val context: Context) : RecyclerView.Adapter<RecyclerOverviewAdapter.ViewHolder>(){
    public lateinit var itemClickListener : ItemClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDoAm.text = dataList[position].hummi
        holder.txtTemp.text = dataList[position].temp
        holder.txtLight.text = dataList[position].light
        holder.txtPin.text = dataList[position].pin.toString()
        holder.progessbar.progress = dataList[position].pin
        holder.txtNodeName.text = dataList[position].nodeName
        holder.setNode.setOnClickListener {it ->
            showSetNodeDialog(context,holder)
            }
        }

    private fun showSetNodeDialog(context: Context,holder:ViewHolder) {
        val dialogBuilder = AlertDialog.Builder(context).create()
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val thisDialog = inflater.inflate(R.layout.custom_dialog_node,null)
        //thisDialog.setContentView(R.layout.custom_dialog_node)
        val btnCancel = thisDialog.findViewById<Button>(R.id.btnCancel)
        val btnOk = thisDialog.findViewById<Button>(R.id.btnOk)
        val edtSetNode = thisDialog.findViewById<EditText>(R.id.edtSetNode)
        btnOk.setOnClickListener {
            val nodeName = edtSetNode.text.toString()
            holder.txtNodeName.text = nodeName
            Toast.makeText(context,"Đổi tên Node thành công",Toast.LENGTH_SHORT).show()
            dialogBuilder.dismiss()
        }
        btnCancel.setOnClickListener {
            dialogBuilder.dismiss()
    }
}


    inner class ViewHolder(val item : View) : RecyclerView.ViewHolder(item) {
        val txtNodeName = item.findViewById<View>(R.id.txtNodeName) as TextView
        val txtTemp = item.findViewById<View>(R.id.txtTemp) as TextView
        val txtDoAm = item.findViewById<View>(R.id.txtDoAm) as TextView
        val txtLight = item.findViewById<View>(R.id.txtLight) as TextView
        val txtPin = item.findViewById<View>(R.id.txtProgress) as TextView
        val progessbar = item.findViewById<View>(R.id.progressBar) as ProgressBar
        val setNode : Button = item.findViewById<View>(R.id.btnSetDevice) as Button


//        fun setItemClickListener(itemClickListener : ItemClickedListener)
//        {
//            this.itemClickListener = itemClickListener
//        }
    }
}