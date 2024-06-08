package com.example.dailytransac.Saksh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class Adapter_daily(var dailylist:ArrayList<Model_daily>):RecyclerView.Adapter<Adapter_daily.MyviewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_daily.MyviewHolder {
      var daily=LayoutInflater.from(parent.context).inflate(R.layout.dailyreport,parent,false)
       return MyviewHolder(daily)
    }

    override fun onBindViewHolder(holder: Adapter_daily.MyviewHolder, position: Int) {
     holder.dat.setText(dailylist[position].d)
     holder.ex.setText(dailylist[position].e)
     holder.re.setText(dailylist[position].r)
     holder.inc.setText(dailylist[position].i)    }

    override fun getItemCount(): Int {
     return dailylist.size   }
    inner class MyviewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        lateinit var dat:TextView
        lateinit var ex:TextView
        lateinit var re:TextView
        lateinit var inc:TextView

        init {
            dat=itemview.findViewById(R.id.dinak)
            ex=itemview.findViewById(R.id.exp)
            re=itemview.findViewById(R.id.reva)
            inc=itemview.findViewById(R.id.inco)
        }
    }
}