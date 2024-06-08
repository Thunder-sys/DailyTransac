package com.example.dailytransac.Saksh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class Adapter_monthly(var monthlist:ArrayList<Model_monthly>):RecyclerView.Adapter<Adapter_monthly.MyviewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_monthly.MyviewHolder {
        var month=LayoutInflater.from(parent.context).inflate(R.layout.monthlyreport,parent,false)
        return MyviewHolder(month)
       }

    override fun onBindViewHolder(holder: Adapter_monthly.MyviewHolder, position: Int) {
    holder.incz1.setText(monthlist[position].i1)
     holder.date1.setText(monthlist[position].d1)
     holder.expz1.setText(monthlist[position].e1)
     holder.revz1.setText(monthlist[position].r1)
      }
    override fun getItemCount(): Int {
     return monthlist.size
    }
    inner class MyviewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        lateinit var date1:TextView
        lateinit var revz1:TextView
        lateinit var expz1:TextView
        lateinit var incz1:TextView

        init {
            date1=itemview.findViewById(R.id.dat1)
            revz1=itemview.findViewById(R.id.rev1)
            expz1=itemview.findViewById(R.id.exp1)
            incz1=itemview.findViewById(R.id.inc1)


        }
    }
}