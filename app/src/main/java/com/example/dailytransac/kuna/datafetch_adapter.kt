package com.example.dailytransac.kuna

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class datafetch_adapter(var item:ArrayList<datafetch_model>):RecyclerView.Adapter<datafetch_adapter.dataclass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): datafetch_adapter.dataclass {
        var month= LayoutInflater.from(parent.context).inflate(R.layout.add_list,parent,false)
        return dataclass(month)
    }

    override fun onBindViewHolder(holder: datafetch_adapter.dataclass, position: Int) {
        holder.entry2.setText(item[position].entry2)
        holder.work.setText(item[position].work)
    }

    override fun getItemCount(): Int {
        return item.size
    }
    inner class dataclass(itemView:View):RecyclerView.ViewHolder(itemView){
        lateinit var entry2:EditText
        lateinit var work:EditText

        init {
            entry2 = itemView.findViewById(R.id.entry2)
            work = itemView.findViewById(R.id.work)
        }
    }
}