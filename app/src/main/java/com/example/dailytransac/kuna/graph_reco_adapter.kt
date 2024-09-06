import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.graph_reco_model

class graph_reco_adapter(private val dataList: ArrayList<graph_reco_model>) : RecyclerView.Adapter<graph_reco_adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView.findViewById(R.id.graph_category) // Replace with actual ID
        val valueTextView: TextView = itemView.findViewById(R.id.graph_value) // Replace with actual ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.graph_for_reco, parent, false) // Replace with your layout
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.categoryTextView.text = data.category // Set category as text
        holder.valueTextView.text = data.value.toString() // Set value as text
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
