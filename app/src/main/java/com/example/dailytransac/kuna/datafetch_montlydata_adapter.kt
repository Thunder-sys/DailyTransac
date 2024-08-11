package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class datafetch_montlydata_adapter(var item:ArrayList<datafetch_montlydata>): RecyclerView.Adapter<datafetch_montlydata_adapter.dataclass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): datafetch_montlydata_adapter.dataclass {
        var month= LayoutInflater.from(parent.context).inflate(R.layout.monthly_list,parent,false)
        return dataclass(month)
    }

    override fun onBindViewHolder(holder: datafetch_montlydata_adapter.dataclass, position: Int) {
        holder.date.setText(item[position].date)
        holder.rev.setText(item[position].rev)
        holder.exp.setText(item[position].exp)
        holder.sav.setText(item[position].sav)
    }

    override fun getItemCount(): Int {
        return item.size
    }
    inner class dataclass(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var date: TextView
        lateinit var exp: TextView
        lateinit var rev: TextView
        lateinit var sav: TextView

        init {
            date = itemView.findViewById(R.id.daily_value)
            exp = itemView.findViewById(R.id.total_daily_expenses)
            rev = itemView.findViewById(R.id.total_daily_income)
            sav = itemView.findViewById(R.id.total_daily_saving)
        }
    }
}