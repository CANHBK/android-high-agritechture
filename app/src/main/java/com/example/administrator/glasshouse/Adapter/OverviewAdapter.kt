package com.example.administrator.glasshouse.Adapter

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.administrator.glasshouse.ModelTest.Datacheck_1
import com.example.administrator.glasshouse.R
import org.angmarch.views.NiceSpinner


class OverviewAdapter(val dataList: ArrayList<Datacheck_1>, val context: Context) : RecyclerView.Adapter<OverviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDoAm.text = dataList[position].hummi
        holder.txtTemp.text = dataList[position].temp
        holder.txtLight.text = dataList[position].light
        holder.txtPin.text = dataList[position].pin.toString()+"%"
        holder.progessbar.progress = dataList[position].pin
        holder.txtNodeName.text = dataList[position].nodeName
        holder.setNode.setOnClickListener {
            showSetNodeDialog(context, holder)
        }
//        holder.itemView.setOnClickListener {
//            setRelayDialog(context)
//        }
    }

    private fun setRelayDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_dialog_role)
        val btnRelayOk = dialog.findViewById<View>(R.id.btnRelayOk) as Button
        val btnRelayCancel = dialog.findViewById<View>(R.id.btnRelayCancel) as Button
        dialog.setCancelable(true)
        dialog.create()
        btnRelayOk.setOnClickListener {
            dialog.dismiss()
        }
        btnRelayCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        // Set các sự kiện sau
    }

    private fun showSetNodeDialog(context: Context, holder: ViewHolder) {
        //Toast.makeText(context,"setNode Clicked",Toast.LENGTH_LONG).show()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_dialog_node)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnOk = dialog.findViewById<Button>(R.id.btnOk)
        val edtSetNode = dialog.findViewById<EditText>(R.id.edtSetNode)
        val spinReponse = dialog.findViewById<View>(R.id.spinResponse) as NiceSpinner

        val responseAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(context, R.array.Response, android.R.layout.simple_spinner_item)
        responseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinReponse.setAdapter(responseAdapter)

        edtSetNode.setText(holder.txtNodeName.text.toString())

        btnOk.setOnClickListener {
            val nodeName = edtSetNode.text.toString()
            holder.txtNodeName.text = nodeName
            Toast.makeText(context, "Setting Node successfully", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

        val txtNodeName = item.findViewById<View>(R.id.txtNodeName) as TextView
        val txtTemp = item.findViewById<View>(R.id.txtTemp) as TextView
        val txtDoAm = item.findViewById<View>(R.id.txtDoAm) as TextView
        val txtLight = item.findViewById<View>(R.id.txtLight) as TextView
        val txtPin = item.findViewById<View>(R.id.txtPin) as TextView
        val progessbar = item.findViewById<View>(R.id.battery) as ProgressBar
        val setNode: Button = item.findViewById<View>(R.id.btnSetDevice) as Button

    }
}