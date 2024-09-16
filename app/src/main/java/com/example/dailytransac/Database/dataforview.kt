import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.dailytransac.R

class CardManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // Save card data
    fun saveCardData(uniqueId: String, entry2: String, work: String, spinnershow: String) {
        editor.putString("${uniqueId}_entry2", entry2)
        editor.putString("${uniqueId}_work", work)
        editor.putString("${uniqueId}_spinnershow", spinnershow)
        editor.apply()
    }

    // Remove card data
    fun removeCard(uniqueId: String) {
        editor.remove("${uniqueId}_entry2")
        editor.remove("${uniqueId}_work")
        editor.remove("${uniqueId}_spinnershow")
        editor.apply()
    }

    // Load all card data and initialize views
    fun loadCardValues(layout_list: ViewGroup) {
        val allEntries = prefs.all
        val cardIds = allEntries.keys.filter { it.endsWith("_entry2") }
            .map { it.substringBefore("_entry2") }
            .distinct()

        cardIds.forEach { uniqueId ->
            val entry2 = prefs.getString("${uniqueId}_entry2", "") ?: ""
            val work = prefs.getString("${uniqueId}_work", "") ?: ""
            val spinnershow = prefs.getString("${uniqueId}_spinnershow", "") ?: ""

            val newView: View = inflater.inflate(R.layout.add_list, null)
            val entry2View: EditText = newView.findViewById(R.id.entry2)
            val workView: EditText = newView.findViewById(R.id.work)
            val spinnershowView: TextView = newView.findViewById(R.id.home_spinnershow)

            entry2View.setText(entry2)
            workView.setText(work)
            spinnershowView.text = spinnershow
            layout_list.addView(newView)

            // Set up delete button
            newView.findViewById<ImageButton>(R.id.delete).setOnClickListener {
                removeCard(uniqueId)
                layout_list.removeView(newView)
            }

            // Set up text change listeners
            setupTextWatchers(uniqueId, entry2View, workView, spinnershowView)
        }
    }

    fun setupTextWatchers(uniqueId: String, entry2: EditText, work: EditText, spinnershow: TextView) {
        entry2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveCardData(uniqueId, entry2.text.toString(), work.text.toString(), spinnershow.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        work.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveCardData(uniqueId, entry2.text.toString(), work.text.toString(), spinnershow.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // If necessary, you can add a TextWatcher for spinnershow

    }
}
