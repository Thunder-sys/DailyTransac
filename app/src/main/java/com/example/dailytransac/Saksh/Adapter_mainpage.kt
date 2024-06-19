package com.example.dailytransac.Saksh

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dailytransac.R
import kotlinx.coroutines.NonDisposableHandle.parent

class Adapter_mainpage(var datatransac:ArrayList<Model_mainpage>):RecyclerView.Adapter<Adapter_mainpage.MyviewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_mainpage.MyviewHolder {
    var myla = LayoutInflater.from(parent.context).inflate(R.layout.detailedreport,parent, false)
    return MyviewHolder(myla)
    }

    override fun onBindViewHolder(holder: Adapter_mainpage.MyviewHolder, position: Int) {
       holder.date.setText(datatransac[position].date)
       holder.expence.setText(datatransac[position].eprice)
       holder.revanue.setText(datatransac[position].rprice)
       holder.income.setText(datatransac[position].iprice)
    }

    override fun getItemCount(): Int {
       return datatransac.size
    }
    inner class MyviewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var date : TextView
        lateinit var expence:TextView
        lateinit var revanue:TextView
        lateinit var income:TextView
        

        init {
            date=itemView.findViewById(R.id.tareekh)
            expence=itemView.findViewById(R.id.gaye)
            revanue=itemView.findViewById(R.id.aaye)
            income=itemView.findViewById(R.id.kamaye)
        }
    }

}

