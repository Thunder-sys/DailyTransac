package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class datafetch_yearlydata_adapter(var item:ArrayList<datafetch_yearlydata>): RecyclerView.Adapter<datafetch_yearlydata_adapter.dataclass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): datafetch_yearlydata_adapter.dataclass {
        var month= LayoutInflater.from(parent.context).inflate(R.layout.yearly_list,parent,false)
        return dataclass(month)
    }

    override fun onBindViewHolder(holder: datafetch_yearlydata_adapter.dataclass, position: Int) {
        holder.month.setText(item[position].month)
        holder.rev.setText(item[position].rev)
        holder.exp.setText(item[position].exp)
        holder.sav.setText(item[position].sav)
    }

    override fun getItemCount(): Int {
        return item.size
    }
    inner class dataclass(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var month: TextView
        lateinit var exp: TextView
        lateinit var rev: TextView
        lateinit var sav: TextView

        init {
            month = itemView.findViewById(R.id.month_value)
            exp = itemView.findViewById(R.id.total_month_expenses)
            rev = itemView.findViewById(R.id.total_month_income)
            sav = itemView.findViewById(R.id.total_month_saving)
        }
    }
}