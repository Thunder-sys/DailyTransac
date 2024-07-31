package com.example.dailytransac.kuna

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.Mainpage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : AppCompatActivity(){

    lateinit var layout_list:LinearLayout
    lateinit var add_button:Button
    lateinit var entry:EditText
    lateinit var expences:TextView
    lateinit var income:TextView
    lateinit var sand:ImageButton
    lateinit var updatebotton:Button
    lateinit var calendarTextView:TextView
    lateinit var sumbit:Button
    private lateinit var handler: Handler
    lateinit var currentDate:String
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var firebaseRefer: DatabaseReference
    private lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var valuefor:String
    var totalMytkl:Int = 0
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    var uid = firebaseAuth.currentUser?.uid!!

    init{
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseReference = firebaseDatabase.getReference().child("User").child(uid).child("data")
        firebaseRefer = firebaseDatabase.getReference().child("User").child(uid).child("data")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        calendarTextView = findViewById(R.id.updatedatelist)
        entry = findViewById(R.id.entry1)
        expences = findViewById(R.id.expences)
        income = findViewById(R.id.income)
        sumbit = findViewById(R.id.submit)
        layout_list = findViewById(R.id.Layout_list)
        add_button = findViewById(R.id.add)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatte=DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val finalDate = "30/12/2100"

        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())
                calendarTextView.text = currentDate
                var dataStringm= currentDate.toString()

                val initiadate= LocalDate.parse(finalDate,formatte)
                val initda=initiadate.toEpochDay().toInt()
                val initiadate2= LocalDate.parse(dataStringm,formatte)
                val initda2=initiadate2.toEpochDay().toInt()
                Log.d("Mus",""+initda)
                valuefor = (initda - initda2).toString()


                // Schedule the next update in 1 second
                handler.postDelayed(this, 1000)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)

        //DynamicView
        sand = findViewById(R.id.secondActivityButton)
        updatebotton = findViewById(R.id.updatebutton)
        sand.setOnClickListener() {
            var dhh = Intent(this, Mainpage::class.java)
            dhh.putExtra("uid","$uid")
            startActivity(dhh)
        }
        updatebotton.setOnClickListener() {
            var dhh = Intent(this, UpdateList::class.java)
            startActivity(dhh)
        }
        addcard()
        add_button.setOnClickListener() {
            addcard()
        }
        sumbit.setOnClickListener(){
            servedForTheServer()
        }


    }
    private fun servedForTheServer() {
        firebaseRefer.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (yut in snapshot.children) {
                    var dateg = yut.child("mydateg").getValue().toString()
                    var dateop = dateg.substring(3, 5)
                    var datop = currentDate.substring(3, 5)
                    if (dateop != datop) {
                        var sum = 0
                        for (i in 0 until layout_list.childCount) {
                            val view: View = layout_list.getChildAt(i)
                            val entry2: EditText = view.findViewById(R.id.entry2)
                            val work: EditText = view.findViewById(R.id.work)
                            val spinnerresult: TextView = view.findViewById(R.id.spinner)
                            val myvalie = entry2.text.toString()
                            val mycat = work.text.toString()
                            var myspin = spinnerresult.text.toString()

                            if (TextUtils.isEmpty(myvalie)) {
                                entry2.error = "Please Enter The Data"
                                return
                            } else if (TextUtils.isEmpty(mycat)) {
                                work.error = "Please Enter The Data"
                                return
                            } else {
                                sum += myvalie.toInt()
                                val mysendti: MutableMap<String, Any> = HashMap()
                                mysendti["entry2"] = myvalie
                                mysendti["work"] = mycat
                                mysendti["Spinner"] = myspin
                                firebaseReference.child(valuefor).child("dateri")
                                    .child("Myfirstdata$i")
                                    .setValue(mysendti)
                            }
                        }
                        val myallsum = entry.text.toString()
                        val ik = myallsum.toInt()
                        expences.text = sum.toString()
                        val myallsav = ik - sum
                        income.text = myallsav.toString()

                        val allexper = sum.toString()
                        val allstru: MutableMap<String, Any> = HashMap()
                        allstru["entry"] = myallsum
                        allstru["Expenses"] = allexper
                        allstru["income"] = myallsav.toString()
                        allstru["datevalue"] = valuefor
                        allstru["mydateg"] = currentDate
                        allstru["totalEntry"] = 0
                        allstru["totalExpenses"] = 0

                        firebaseReference.child(valuefor).updateChildren(allstru)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Data Send Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "There Are Some Problem",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                    } else {
                        var sum = 0
                        for (i in 0 until layout_list.childCount) {
                            val view: View = layout_list.getChildAt(i)
                            val entry2: EditText = view.findViewById(R.id.entry2)
                            val work: EditText = view.findViewById(R.id.work)
                            val spinnerresult: TextView = view.findViewById(R.id.spinner)
                            val myvalie = entry2.text.toString()
                            val mycat = work.text.toString()
                            var myspin = spinnerresult.text.toString()

                            if (TextUtils.isEmpty(myvalie)) {
                                entry2.error = "Please Enter The Data"
                                return
                            } else if (TextUtils.isEmpty(mycat)) {
                                work.error = "Please Enter The Data"
                                return
                            } else {
                                sum += myvalie.toInt()
                                val mysendti: MutableMap<String, Any> = HashMap()
                                mysendti["entry2"] = myvalie
                                mysendti["work"] = mycat
                                mysendti["Spinner"] = myspin
                                firebaseReference.child(valuefor).child("dateri")
                                    .child("Myfirstdata$i")
                                    .setValue(mysendti)
                            }
                        }
                        val myallsum = entry.text.toString()
                        val ik = myallsum.toInt()
                        expences.text = sum.toString()
                        val myallsav = ik - sum
                        income.text = myallsav.toString()

                        val allexper = sum.toString()
                        val allstru: MutableMap<String, Any> = HashMap()
                        allstru["entry"] = myallsum
                        allstru["Expenses"] = allexper
                        allstru["income"] = myallsav.toString()
                        allstru["datevalue"] = valuefor
                        allstru["mydateg"] = currentDate
                        allstru["totalEntry"] = 2
                        allstru["totalExpenses"] = 2

                        firebaseReference.child(valuefor).updateChildren(allstru)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Data Send Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "There Are Some Problem",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //DynamicView
    private fun addcard() {
        val view:View = layoutInflater.inflate(R.layout.add_list,null)
        layout_list.addView(view)
        val entry2:EditText = view.findViewById(R.id.entry2)
        val spinner:Spinner = view.findViewById(R.id.spinner)
        val spinnerresult:TextView= view.findViewById(R.id.spinner)
        var category = arrayOf("None","Food","Study","Cloths","Vehicle","Other")
        val arrayAdp = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,category)
        spinner.adapter = arrayAdp
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerresult.text = category[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        entry2.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val mytkvl: Int = try {
                    entry2.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                totalMytkl += mytkvl
                expences.text = totalMytkl.toString()

                val entryValue: Int = try {
                    entry.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val totalIncome = entryValue - totalMytkl
                if (entryValue>totalIncome) income.setText("$totalIncome") else if(entryValue==totalIncome) income.setText("0") else income.setText("- $totalIncome")
            }
        }

        view.findViewById<ImageButton>(R.id.delete).setOnClickListener(){
            removeCard(view)
        }
    }

    private fun removeCard(view: View) {
        layout_list.removeView(view)
    }


    //Calender = Date
    override fun onDestroy() {
        super.onDestroy()

        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateTimeRunnable)
    }
}