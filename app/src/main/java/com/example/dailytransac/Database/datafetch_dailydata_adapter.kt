package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class datafetch_dailydata_adapter(var item:ArrayList<datafetch_dailydata>): RecyclerView.Adapter<datafetch_dailydata_adapter.dataclass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): datafetch_dailydata_adapter.dataclass {
        var month= LayoutInflater.from(parent.context).inflate(R.layout.daily_reco,parent,false)
        return dataclass(month)
    }

    override fun onBindViewHolder(holder: datafetch_dailydata_adapter.dataclass, position: Int) {
        holder.rev.setText(item[position].entry)
        holder.exp.setText(item[position].work)
        holder.sav.setText(item[position].spin)
    }

    override fun getItemCount(): Int {
        return item.size
    }
    inner class dataclass(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var exp: TextView
        lateinit var rev: TextView
        lateinit var sav: TextView

        init {
            exp = itemView.findViewById(R.id.daily_t2)
            rev = itemView.findViewById(R.id.daily_t1)
            sav = itemView.findViewById(R.id.daily_t3)
        }
    }
}