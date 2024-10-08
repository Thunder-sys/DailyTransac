package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class graph_spinner_adapter(var item:ArrayList<graph_spinner_model>, private val onItemClick: (graph_spinner_model) -> Unit):RecyclerView.Adapter<graph_spinner_adapter.viewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): graph_spinner_adapter.viewholder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.graph_spinner_recycleview,parent,false)
        return viewholder(view)
    }
    fun setAdapterList(item: ArrayList<graph_spinner_model>){
        this.item = item
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: graph_spinner_adapter.viewholder, position: Int) {
        val dattaspinner = item[position]
        holder.textView.setText(dattaspinner.text)
        holder.itemView.setOnClickListener {
            onItemClick(dattaspinner)
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var textView: TextView

        init {
            textView = itemView.findViewById(R.id.graph_spinner_recycle_text)

        }
    }
}