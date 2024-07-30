package com.example.dailytransac.khush

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

class Budgetscreen : AppCompatActivity() {
    lateinit var linechart:LineChart
    var value:String=""
    var datavalue:String = ""
    var firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseReference: DatabaseReference
    lateinit var firebaseReference123: DatabaseReference
    lateinit var firebaseReference1234: DatabaseReference
    lateinit var firebaseReference_spinner: DatabaseReference
    private lateinit var handler: Handler
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    lateinit var currentDate:String
    lateinit var category: ArrayList<String>
    lateinit var arrayAdp: ArrayAdapter<String>
    lateinit var spinner: Spinner
    lateinit var entry :TextView
    lateinit var expenses :TextView
    lateinit var income :TextView
    var uid = firebaseAuth.currentUser?.uid!!
    init{
        firebaseReference123 = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("year")
        firebaseReference1234 = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("year")
        firebaseReference_spinner = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("year")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budgetscreen)
        entry = findViewById(R.id.rev12)
        expenses = findViewById(R.id.exp12)
        income = findViewById(R.id.inc12)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())


                handler.postDelayed(this, 1000)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)

        firebaseDatabase = FirebaseDatabase.getInstance()
        linechart = findViewById(R.id.Linechart)
        spinner = findViewById(R.id.spinner12)

        category = ArrayList<String>()
        arrayAdp = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,category)

        fun dataValues1(): ArrayList<Entry> {
            val dataVals = ArrayList<Entry>()
            dataVals.add(Entry(0f, 20f))
            dataVals.add(Entry(1f, 24f))
            dataVals.add(Entry(2f, 28f))
            dataVals.add(Entry(3f, 10f))
            dataVals.add(Entry(4f, 20f))
            dataVals.add(Entry(5f, 16f))
            dataVals.add(Entry(6f, 19f))
            dataVals.add(Entry(7f, 9f))
            dataVals.add(Entry(8f, 25f))
            return dataVals
        }

        var lineDataSet:LineDataSet = LineDataSet(dataValues1(),"Data Set 1")

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet)

        val data = LineData(dataSets)
        linechart.data = data
        linechart.invalidate()



        spinner.adapter = arrayAdp
        showData()

        value = spinner.toString()
        when(value) {
            "January" -> datavalue = "01"
            "Febrary" -> datavalue = "02"
            "March" -> datavalue = "03"
            "April" -> datavalue = "04"
            "May" -> datavalue = "05"
            "June" -> datavalue = "06"
            "July" -> datavalue = "07"
            "August" -> datavalue = "08"
            "September" -> datavalue = "09"
            "October" -> datavalue = "10"
            "November" -> datavalue = "11"
            "December" -> datavalue = "12"
        }

        firebaseReference123.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(opiop in snapshot.children){
                    var yeaar = opiop.child("yearop").getValue().toString()
                    firebaseReference1234.child(yeaar).child("month").addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(opiopi in snapshot.children){
                                var monthlu = opiopi.child("currentmonth").getValue().toString()
                                var totalentry = opiopi.child("totalentry").getValue().toString()
                                var totalExpenses = opiopi.child("totalExpenses").getValue().toString()
                                var totalincome = totalentry.toInt() - totalExpenses.toInt()

                                if(datavalue == monthlu){
                                    income.setText(totalincome)
                                    entry.setText(totalentry)
                                    expenses.setText(totalExpenses)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun showData() {
        firebaseReference_spinner.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(opi in snapshot.children){
                    var subString = opi.child("yearop").getValue().toString()
                    firebaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("year").child("$subString").child("spinnermonth")
                    firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(opio in snapshot.children){
                                var spin = opio.child("monthly").getValue().toString()
                                category.add(spin)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}