package com.example.dailytransac.transaction

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.datafetch_montlydata
import com.example.dailytransac.kuna.datafetch_montlydata_adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class monthly : Fragment() {
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var dateTextView: TextView

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseReference: DatabaseReference

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView

    private lateinit var reco1: RecyclerView

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid: String = firebaseAuth.currentUser?.uid ?: ""

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_monthly, container, false)

        reco1 = view.findViewById(R.id.month_reco)
        dateTextView = view.findViewById(R.id.monthly_value)

        val incrementButton: ImageView = view.findViewById(R.id.monthly_greater_than)
        val decrementButton: ImageView = view.findViewById(R.id.monthly_less_than)

        textView1 = view.findViewById(R.id.daily_income)
        textView2 = view.findViewById(R.id.daily_expenses)
        textView3 = view.findViewById(R.id.daily_saving)

        dateTextView.setOnClickListener {
            monthPicker(view, calendar)
        }

        updateDateDisplay(view)

        incrementButton.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateDateDisplay(view)
        }

        decrementButton.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateDateDisplay(view)
        }

        return view
    }

    private fun monthPicker(view: View, calendar: Calendar) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.month_picker)

        val yearPicker = dialog.findViewById<NumberPicker>(R.id.monthpicker_year)
        yearPicker.minValue = 2000
        yearPicker.maxValue = 2100
        yearPicker.wrapSelectorWheel = true

        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val monthPicker = dialog.findViewById<NumberPicker>(R.id.monthpicker_month)
        monthPicker.minValue = 0
        monthPicker.maxValue = 11
        monthPicker.wrapSelectorWheel = true
        monthPicker.displayedValues = months

        yearPicker.value = calendar.get(Calendar.YEAR)
        monthPicker.value = calendar.get(Calendar.MONTH)

        val cancel: TextView = dialog.findViewById(R.id.month_cancel)
        val ok: TextView = dialog.findViewById(R.id.month_ok)

        ok.setOnClickListener {
            val selectedYear = yearPicker.value
            val selectedMonth = monthPicker.value

            calendar.set(Calendar.YEAR, selectedYear)
            calendar.set(Calendar.MONTH, selectedMonth)

            val formattedMonth = months[selectedMonth]
            val formattedYear = selectedYear

            dateTextView.text = "$formattedMonth $formattedYear"
            dialog.dismiss()
            updateDateDisplay(view)
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateDateDisplay(view: View) {
        val dateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dateFormat1 = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time)
        val dateString1 = dateFormat1.format(calendar.time)

        val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val monthvalue = monthNames[dateString.toInt() - 1]

        textView1.text = ""
        textView2.text = ""
        textView3.text = ""

        dateTextView.text = "$monthvalue $dateString1"

        fetchdata(view, dateString, dateString1)
    }

    private fun fetchdata(view: View, dateString: String, dateString1: String) {

        Log.d("mio","$dateString")
        Log.d("mio","$dateString1")
        val listOfMonthData = ArrayList<datafetch_montlydata>()
        reco1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = datafetch_montlydata_adapter(listOfMonthData)
        reco1.adapter = adapter

        val year = 2100 - dateString1.toInt()
        val month = 13 - dateString.toInt()
        Log.d("mio", "Year: $year, Month: $month")

        firebaseReference = firebaseDatabase.getReference("User/$uid/year/$year/month/$month/date")

        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            var value1 = 0
            var value2 = 0
            var value3 = 0

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ip in snapshot.children) {
                    val expenses = ip.child("Expenses").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val revenue = ip.child("entry").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val saving = ip.child("income").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val mydate = ip.child("mydateg").getValue(String::class.java) ?: ""

                    Log.d("month","$expenses")
                    Log.d("month","$revenue")
                    Log.d("month","$saving")
                    Log.d("month","$mydate")

                    value1 += revenue
                    value2 += expenses
                    value3 += saving

                    val current = mydate.substring(0, 2)
                    listOfMonthData.add(datafetch_montlydata(current, revenue.toString(), expenses.toString(), saving.toString()))

                    textView1.text = value1.toString()
                    textView2.text = value2.toString()
                    textView3.text = value3.toString()
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "There was an error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
