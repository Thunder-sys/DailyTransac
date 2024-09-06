package com.example.dailytransac.analysis

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.datafetch_dailydata
import com.example.dailytransac.kuna.datafetch_dailydata_adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class daily : Fragment() {
    lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var firebaseReference1: DatabaseReference

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var reco1: RecyclerView
    lateinit var textView1: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView
    lateinit var textView4: TextView

    private lateinit var dateTextView: TextView
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
        var view = inflater.inflate(R.layout.fragment_daily, container, false)

        dateTextView = view.findViewById(R.id.daily_value)
        reco1 = view.findViewById(R.id.daily_recodetail)
        textView1 = view.findViewById(R.id.daily_e1)
        textView2 = view.findViewById(R.id.daily_e2)
        textView3 = view.findViewById(R.id.daily_e3)

        val incrementButton: ImageView = view.findViewById(R.id.daily_greater_than)
        val decrementButton: ImageView = view.findViewById(R.id.daily_less_than)
        updateDateDisplay(view)

        dateTextView.setOnClickListener(){
            showDataPicker(view)
        }

        incrementButton.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            updateDateDisplay(view)
        }

        decrementButton.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            updateDateDisplay(view)
        }

        return view
    }

    private fun showDataPicker(view: View) {
        val datapickerDialog = DatePickerDialog(requireContext(),{DatePicker, year:Int, monthOfYear:Int, dayOfMonth:Int ->
            calendar.set(year, monthOfYear, dayOfMonth)
            updateDateDisplay(view)
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datapickerDialog.show()

    }

    private fun updateDateDisplay(view: View?) {
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val dateFormat1 = SimpleDateFormat("MM", Locale.getDefault())
        val dateFormat2 = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time).toString()
        val dateString1 = dateFormat1.format(calendar.time).toString()
        val dateString2 = dateFormat2.format(calendar.time).toString()

        dateTextView.setText("$dateString-$dateString1-$dateString2")
        fethdata(dateString1,dateString2,dateString)


    }
    private fun fethdata(dateString:String,dateString1: String,dateString3: String){
        textView1.setText("0")
        textView2.setText("0")
        textView3.setText("0")
        val listOfMonthData = ArrayList<datafetch_dailydata>()
        reco1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = datafetch_dailydata_adapter(listOfMonthData)
        reco1.adapter = adapter

        val year = 2100 - dateString1.toInt()
        val month = 13 - dateString.toInt()
        val date = 32 - dateString3.toInt()
        Log.d("mio", "Year: $year, Month: $month, date: $date ")

        firebaseReference1 = firebaseDatabase.getReference("User/$uid/year/$year/month/$month/date1/$date/dater")

        firebaseReference1.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ip in snapshot.children) {
                    val entry = ip.child("entry").getValue(String::class.java)?.toIntOrNull() ?: 0

                    textView1.setText("$entry")

                    firebaseReference = firebaseDatabase.getReference("User/$uid/year/$year/month/$month/date1/$date/dateri")

                    firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        var sum = 0

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (ip in snapshot.children) {
                                val expenses = ip.child("entry2").getValue(String::class.java)?.toIntOrNull() ?: 0
                                val revenue = ip.child("work").getValue(String::class.java)?: ""
                                val saving = ip.child("Spinner").getValue(String::class.java)?: ""

                                sum+=expenses
                                textView2.setText("$sum")
                                var totali = entry.toInt() - sum
                                textView3.setText("$totali")

                                listOfMonthData.add(datafetch_dailydata("$expenses",revenue,saving))

                            }
                            adapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext(), "There was an error", Toast.LENGTH_SHORT).show()
                        }
                    })

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "There was an error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}