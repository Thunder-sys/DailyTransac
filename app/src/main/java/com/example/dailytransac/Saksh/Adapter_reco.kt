package com.example.dailytransac.Saksh

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class Adapter_reco(var recolist:ArrayList<Model_reco>):RecyclerView.Adapter<Adapter_reco.MyviewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_reco.MyviewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: Adapter_reco.MyviewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    inner class MyviewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        lateinit var expen1:TextView
        lateinit var descri:TextView
        lateinit var tpe:TextView

        init {
            expen1=itemview.findViewById(R.id.t1)
            descri=itemview.findViewById(R.id.t2)
            tpe=itemview.findViewById(R.id.t3)
        }
    }
}