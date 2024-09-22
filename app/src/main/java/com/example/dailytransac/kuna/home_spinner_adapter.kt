package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class home_spinner_adapter(var item:ArrayList<home_spinner_model>,var auth:FirebaseAuth = FirebaseAuth.getInstance(),var db:DatabaseReference = FirebaseDatabase.getInstance().getReference(), private val onItemClick: (home_spinner_model) -> Unit):RecyclerView.Adapter<home_spinner_adapter.viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): home_spinner_adapter.viewholder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.home_spinner_recycleview,parent,false)
        return viewholder(view)
    }
    fun setAdapterList(item: ArrayList<home_spinner_model>){
        this.item = item
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: home_spinner_adapter.viewholder, position: Int) {
        val dattaspinner = item[position]
        holder.textView.setText(dattaspinner.text)
        holder.id.setText(dattaspinner.id)
        holder.textView.setOnClickListener {
            onItemClick(dattaspinner)
        }
        holder.delete.setOnClickListener {

            var op = holder.id.text.toString()
            deleteItem(position,op)
        }
    }
    private fun deleteItem(position: Int, op: String) {
        // Get the item to be deleted
        val itemToDelete = item[position]
        var uid = auth.currentUser?.uid!!

        // Remove from the list
        item.removeAt(position)
        notifyItemRemoved(position)


        // Remove from the database
        db.child("User").child(uid).child("homespinner").child("$op") // Ensure you have the correct path and identifier
            .removeValue()
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var textView: TextView
        lateinit var id: TextView
        lateinit var delete:ImageView

        init {
            id = itemView.findViewById(R.id.home_spinner_recycle_id)
            textView = itemView.findViewById(R.id.home_spinner_recycle_text)
            delete = itemView.findViewById(R.id.home_spinner_recycle_delete)
        }
    }
}