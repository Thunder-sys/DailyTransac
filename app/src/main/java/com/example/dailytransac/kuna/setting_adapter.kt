package com.example.dailytransac.kuna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R

class setting_adapter(
    private val items: ArrayList<setting_modle>,
    private val onItemClick: (String) -> Unit // Change to pass a String
) : RecyclerView.Adapter<setting_adapter.SettingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.setting_view, parent, false)
        return SettingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val currentItem = items[position]
        holder.image.setBackgroundResource(currentItem.image)
        holder.text.text = currentItem.text

        // Set the click listener on the TextView to pass the text value
        holder.text.setOnClickListener {
            onItemClick(currentItem.text) // Pass only the text value
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SettingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.setting_image)
        val text: TextView = itemView.findViewById(R.id.setting_text)
    }
}
