package com.example.dailytransac.analysis

import android.app.Dialog
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

    lateinit var a1:TextView
    lateinit var a2:TextView
    lateinit var a3:TextView
    lateinit var a4:TextView
    lateinit var a5:TextView
    lateinit var a6:TextView
    lateinit var a7:TextView
    lateinit var a8:TextView
    lateinit var a9:TextView
    lateinit var a10:TextView
    lateinit var a11:TextView
    lateinit var a12:TextView
    lateinit var show_month:TextView
    lateinit var show_year:TextView
    lateinit var less:ImageView
    lateinit var greater:ImageView
    lateinit var yearvalue:TextView

    lateinit var cancel:TextView
    lateinit var ok:TextView

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
        dialog.setContentView(R.layout.monthpicker_data)

        a1 = dialog.findViewById(R.id.monthpicker_jan)
        a2 = dialog.findViewById(R.id.monthpicker_feb)
        a3 = dialog.findViewById(R.id.monthpicker_mar)
        a4 = dialog.findViewById(R.id.monthpicker_apr)
        a5 = dialog.findViewById(R.id.monthpicker_may)
        a6 = dialog.findViewById(R.id.monthpicker_jun)
        a7 = dialog.findViewById(R.id.monthpicker_jul)
        a8 = dialog.findViewById(R.id.monthpicker_aug)
        a9 = dialog.findViewById(R.id.monthpicker_sep)
        a10 = dialog.findViewById(R.id.monthpicker_oct)
        a11 = dialog.findViewById(R.id.monthpicker_nov)
        a12 = dialog.findViewById(R.id.monthpicker_dec)

        show_month = dialog.findViewById(R.id.monthpicker_show_month)
        show_year = dialog.findViewById(R.id.monthpicker_show_year)

        less = dialog.findViewById(R.id.monthpicker_yearly_less_than)
        greater = dialog.findViewById(R.id.monthpicker_yearly_greater_than)
        yearvalue = dialog.findViewById(R.id.monthpicker_year_value)

        cancel = dialog.findViewById(R.id.monthpicker_cancel)
        ok = dialog.findViewById(R.id.monthpicker_ok)

        calendar.get(Calendar.MONTH)
        calendar.get(Calendar.YEAR)

        a1.setOnClickListener(){
            setcolor("1")
        }
        a2.setOnClickListener(){
            setcolor("2")
        }
        a3.setOnClickListener(){
            setcolor("3")
        }
        a4.setOnClickListener(){
            setcolor("4")
        }
        a5.setOnClickListener(){
            setcolor("5")
        }
        a6.setOnClickListener(){
            setcolor("6")
        }
        a7.setOnClickListener(){
            setcolor("7")
        }
        a8.setOnClickListener(){
            setcolor("8")
        }
        a9.setOnClickListener(){
            setcolor("9")
        }
        a10.setOnClickListener(){
            setcolor("10")
        }
        a11.setOnClickListener(){
            setcolor("11")
        }
        a12.setOnClickListener(){
            setcolor("12")
        }

        greater.setOnClickListener {
            calendar.add(Calendar.YEAR, 1)
            updatedateDisplay()
        }

        less.setOnClickListener {
            calendar.add(Calendar.YEAR, -1)
            updatedateDisplay()
        }

        ok.setOnClickListener(){
            var op1 = show_month.text.toString()
            var op2 = show_year.text.toString()
            var yearvalue = op2.toInt()

            when(op1) {
                "January" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 0)
                }

                "February" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 1)
                }

                "March" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 2)
                }

                "April" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 3)
                }

                "May" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 4)
                }

                "June" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 5)
                }

                "July" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 6)
                }

                "August" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 7)
                }

                "September" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 8)
                }

                "October" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 9)
                }

                "November" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 10)
                }

                "December" -> {
                    calendar.set(Calendar.YEAR, yearvalue)
                    calendar.set(Calendar.MONTH, 11)
                }
            }
            updateDateDisplay(view)

            dialog.dismiss()

        }
        cancel.setOnClickListener(){
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updatedateDisplay() {
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time)
        show_year.text = "$dateString"
        yearvalue.text = "$dateString"


    }

    private fun setcolor(s: String) {

        a1.setBackgroundResource(R.drawable.curve_white)
        a2.setBackgroundResource(R.drawable.curve_white)
        a3.setBackgroundResource(R.drawable.curve_white)
        a4.setBackgroundResource(R.drawable.curve_white)
        a5.setBackgroundResource(R.drawable.curve_white)
        a6.setBackgroundResource(R.drawable.curve_white)
        a7.setBackgroundResource(R.drawable.curve_white)
        a8.setBackgroundResource(R.drawable.curve_white)
        a9.setBackgroundResource(R.drawable.curve_white)
        a10.setBackgroundResource(R.drawable.curve_white)
        a11.setBackgroundResource(R.drawable.curve_white)
        a12.setBackgroundResource(R.drawable.curve_white)

        when(s) {
            "1" -> {
                a1.setBackgroundResource(R.drawable.light_green)
                setdatavalue("January")
            }

            "2" -> {
                a2.setBackgroundResource(R.drawable.light_green)
                setdatavalue("February")
            }

            "3" -> {
                a3.setBackgroundResource(R.drawable.light_green)
                setdatavalue("March")
            }

            "4" -> {
                a4.setBackgroundResource(R.drawable.light_green)
                setdatavalue("April")
            }

            "5" -> {
                a5.setBackgroundResource(R.drawable.light_green)
                setdatavalue("May")
            }

            "6" -> {
                a6.setBackgroundResource(R.drawable.light_green)
                setdatavalue("June")
            }

            "7" -> {
                a7.setBackgroundResource(R.drawable.light_green)
                setdatavalue("July")
            }

            "8" -> {
                a8.setBackgroundResource(R.drawable.light_green)
                setdatavalue("August")
            }

            "9" -> {
                a9.setBackgroundResource(R.drawable.light_green)
                setdatavalue("September")
            }

            "10" -> {
                a10.setBackgroundResource(R.drawable.light_green)
                setdatavalue("October")
            }

            "11" -> {
                a11.setBackgroundResource(R.drawable.light_green)
                setdatavalue("November")
            }

            "12" -> {
                a12.setBackgroundResource(R.drawable.light_green)
                setdatavalue("December")
            }
        }

    }

    private fun setdatavalue(s: String) {
        show_month.setText("$s")
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
