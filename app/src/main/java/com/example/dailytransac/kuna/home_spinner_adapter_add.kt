package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class home_spinner_adapter_add(var item:ArrayList<home_spinner_model_add>):RecyclerView.Adapter<home_spinner_adapter_add.viewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): home_spinner_adapter_add.viewholder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.home_spinner_recycleview_add,parent,false)
        return viewholder(view)
    }

    override fun onBindViewHolder(holder: home_spinner_adapter_add.viewholder, position: Int) {
        val dattaspinner = item[position]
        holder.textView.setText(dattaspinner.text1)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var textView: TextView

        init {
            textView = itemView.findViewById(R.id.home_spinner_recycle_text_add)

        }
    }
}