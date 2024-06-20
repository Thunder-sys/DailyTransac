package com.example.dailytransac.Saksh

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.GridLayoutManager
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
        var parentItem = datatransac[position]

        holder.date.setText(parentItem.date)
        holder.expence.setText(parentItem.eprice)
        holder.revanue.setText(parentItem.rprice)
        holder.income.setText(parentItem.iprice)

        holder.childrecycleview.setHasFixedSize(true)
        holder.childrecycleview.layoutManager = GridLayoutManager(holder.itemView.context,1)

        var adapter = Adapter_reco(parentItem.childclass)
        holder.childrecycleview.adapter = adapter


    }

    override fun getItemCount(): Int {
       return datatransac.size
    }
    inner class MyviewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var date : TextView
        lateinit var expence:TextView
        lateinit var revanue:TextView
        lateinit var income:TextView
        lateinit var childrecycleview:RecyclerView
        

        init {
            date=itemView.findViewById(R.id.tareekh)
            expence=itemView.findViewById(R.id.gaye)
            revanue=itemView.findViewById(R.id.aaye)
            income=itemView.findViewById(R.id.kamaye)
            childrecycleview=itemView.findViewById(R.id.recodetail)
        }
    }

}

