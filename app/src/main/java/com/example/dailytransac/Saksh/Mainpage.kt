package com.example.dailytransac.Saksh

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.Database.monthly_data
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.Adapter_daily
import com.example.dailytransac.Saksh.Adapter_mainpage
import com.example.dailytransac.Saksh.Adapter_monthly
import com.example.dailytransac.Saksh.Model_daily
import com.example.dailytransac.Saksh.Model_mainpage
import com.example.dailytransac.Saksh.Model_monthly
import com.example.dailytransac.Saksh.Model_reco
import com.example.dailytransac.khush.Budgetscreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class Mainpage : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebase_for_daily: DatabaseReference
    private lateinit var firebase_for_month: DatabaseReference
    private lateinit var firebase_for_dailyfull_data: DatabaseReference
    private lateinit var firebaseRefrence: DatabaseReference

    private lateinit var reco1: RecyclerView
    private lateinit var reco2: RecyclerView
    private lateinit var reco3: RecyclerView

    private lateinit var handler: Handler
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    lateinit var currentDate:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)
        var bundle: Bundle? = intent.extras
        var uid = bundle?.getString("uid") ?: ""

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebase_for_daily = firebaseDatabase.getReference("User").child(uid).child("data")
        firebase_for_month = firebaseDatabase.getReference("User").child(uid).child("data")
        firebaseRefrence = firebaseDatabase.getReference().child("User").child(uid).child("year")

        reco1 = findViewById(R.id.recy1)
        reco2 = findViewById(R.id.recy2)
        reco3 = findViewById(R.id.recy)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())

                // Schedule the next update in 1 second
                handler.postDelayed(this, 1000)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)


        var grap = findViewById<ImageButton>(R.id.imageButton)
        grap.setOnClickListener() {
            var intent = Intent(this, Budgetscreen::class.java)
            startActivity(intent)
        }

        val myman = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reco1.layoutManager = myman
        val mythisn = ArrayList<Model_daily>()
        val myadap = Adapter_daily(mythisn)
        reco1.adapter = myadap

        var listofdata: ArrayList<Model_mainpage> = ArrayList()
        reco3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var adaptt = Adapter_mainpage(listofdata)
        reco3.adapter = adaptt

        firebase_for_daily.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (myds in dataSnapshot.children) {
                    var entry = myds.child("entry").getValue().toString()
                    var Expenses = myds.child("Expenses").getValue().toString()
                    var income = myds.child("income").getValue().toString()
                    var mydateg = myds.child("mydateg").getValue().toString()
                    var datevalue = myds.child("datevalue").getValue().toString()
                    var dateVlaue = datevalue.toInt()
                    mythisn.add(Model_daily(mydateg, entry, Expenses, income))

                    var childitem = ArrayList<Model_reco>()
                    firebase_for_dailyfull_data =
                        firebaseDatabase.getReference("User").child(uid).child("data")
                            .child("$dateVlaue").child("dateri")

                    firebase_for_dailyfull_data.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (mydata in datasnapshot.children) {
                                var entry2 = mydata.child("entry2").getValue().toString()
                                var work = mydata.child("work").getValue().toString()
                                var spinner = mydata.child("Spinner").getValue().toString()
                                childitem.add(Model_reco(entry2, work, spinner))
                            }
                            adaptt.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@Mainpage,
                                "There Are some error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                    listofdata.add(Model_mainpage(mydateg, entry, Expenses, income, childitem))
                }
                myadap.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Mainpage, "There Are some error", Toast.LENGTH_SHORT).show()
            }
        })

        var listofmonth = ArrayList<Model_monthly>()
        reco2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var adap = Adapter_monthly(listofmonth)
        reco2.adapter = adap

        firebase_for_month.addListenerForSingleValueEvent(object : ValueEventListener {

            var totalEntry = 0
            var totalExpenses = 0

            override fun onDataChange(snapshot: DataSnapshot) {
                for (mype in snapshot.children) {
                    var date = mype.child("mydateg").getValue().toString()
                    var short_date = date.substring(3, 5)
                    var short_year = date.substring(6, 10)
                    var entry = mype.child("entry").getValue().toString()
                    var expenses = mype.child("Expenses").getValue().toString()

                    totalEntry+=entry.toInt()
                    totalExpenses+=expenses.toInt()

                    var child_month_data = monthly_data("$totalEntry","$totalExpenses",short_date)
                    firebaseRefrence.child("$short_year").child("month").child("$short_date").setValue(child_month_data)
                    var totalentry = mype.child("totalEntry").getValue().toString().toInt()
                    var totalexpenses = mype.child("totalExpenses").getValue().toString().toInt()


                    if (totalentry == 0 && totalexpenses == 0) {
                        totalEntry = totalentry

                        totalExpenses = totalexpenses
                    }


                }

                adap.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Mainpage, "There Are some error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateTimeRunnable)
    }
}
