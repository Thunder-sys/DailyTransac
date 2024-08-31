package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class graph_reco_adapter(var item:ArrayList<graph_reco_model>): RecyclerView.Adapter<graph_reco_adapter.dataclass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): graph_reco_adapter.dataclass {
        var month= LayoutInflater.from(parent.context).inflate(R.layout.graph_for_reco,parent,false)
        return dataclass(month)
    }

    override fun onBindViewHolder(holder: graph_reco_adapter.dataclass, position: Int) {
        holder.category.setText(item[position].category)
        holder.value.setText(item[position].value)
    }

    override fun getItemCount(): Int {
        return item.size
    }
    inner class dataclass(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var category: TextView
        lateinit var value: TextView

        init {
            category = itemView.findViewById(R.id.graph_category)
            value = itemView.findViewById(R.id.graph_value)
        }
    }
}