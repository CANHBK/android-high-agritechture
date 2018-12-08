package com.example.administrator.glasshouse.ui.farm

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.vo.Farm
import javax.inject.Inject

class FarmControlAdapter(val farmList: List<Farm>) : androidx.recyclerview.widget.RecyclerView.Adapter<FarmControlAdapter.ViewHolder>() {

    //    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_farm_horizontal, parent, false)
//        context=parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return farmList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val farm = farmList[position]
//        holder.image.setImageDrawable(context.getDrawable(farm.image))
        holder.name.text = farm.name
        if (position+1!=itemCount){
            val params = holder.layout.layoutParams as RecyclerView.LayoutParams
            params.setMargins(0, 16, 16, 0)
        }

    }

    inner class ViewHolder(item: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(item) {
        val layout = item.findViewById<LinearLayout>(R.id.item_farm)
        val image = item.findViewById<ImageView>(R.id.image_farm)
        val name = item.findViewById<TextView>(R.id.farm_name)

    }
}