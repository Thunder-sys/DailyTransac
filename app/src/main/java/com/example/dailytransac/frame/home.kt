package com.example.dailytransac.frame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.dailytransac.R
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

class home : Fragment() {
    lateinit var layout_list: LinearLayout
    lateinit var add_button: Button
    lateinit var entry:EditText
    lateinit var expences:TextView
    lateinit var income:TextView
    lateinit var sand:ImageButton
    lateinit var updatebotton: Button
    lateinit var calendarTextView:TextView
    lateinit var sumbit: Button
    private lateinit var handler: Handler
    lateinit var currentDate:String
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var firebaseRefer: DatabaseReference
    private lateinit var firebaseRefer1: DatabaseReference
    private lateinit var firebaseRefer2: DatabaseReference
    private lateinit var firebaseRefer3: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseReferfulldata: DatabaseReference
    private lateinit var firebaseReferfulldata1: DatabaseReference
    private lateinit var valuefor:String
    private lateinit var valuefor1:String
    var totalMytkl:Int = 0
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    var uid = firebaseAuth.currentUser?.uid!!

    init{
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseReferfulldata = firebaseDatabase.getReference().child("User").child(uid).child("year")
        firebaseReferfulldata1 = firebaseDatabase.getReference().child("User").child(uid).child("year")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        calendarTextView = view.findViewById(R.id.updatedatelist)
        entry = view.findViewById(R.id.entry1)
        expences = view.findViewById(R.id.Expenses)
        income = view.findViewById(R.id.saving)
        sumbit = view.findViewById(R.id.submit)
        layout_list = view.findViewById(R.id.Layout_list)
        add_button = view.findViewById(R.id.add)
        updatebotton = view.findViewById(R.id.updatebutton)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatte = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatte1 = DateTimeFormatter.ofPattern("MM/yyyy")
        val finalDate = "30/12/2100"
        val finalDate1 = 310012

        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())
                calendarTextView.text = currentDate
                var dataStringm = currentDate.toString()
                var dataStringm2 = currentDate.substring(3,5)
                var dataStringm3 = currentDate.substring(6,10)
                var reversedate = ("$dataStringm3" + "$dataStringm2").toInt()

                valuefor1 = (finalDate1 - reversedate).toString()

                val initiadate = LocalDate.parse(finalDate, formatte)
                val initda = initiadate.toEpochDay().toInt()
                val initiadate2 = LocalDate.parse(dataStringm, formatte)
                val initda2 = initiadate2.toEpochDay().toInt()
                Log.d("Mus", "" + initda)
                valuefor = (initda - initda2).toString()


                // Schedule the next update in 1 second
                handler.postDelayed(this, 1000)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)

        updatebotton.setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_home2_to_updatelist)
        }

        //DynamicView
        addcard()
        add_button.setOnClickListener() {
            addcard()
        }
        sumbit.setOnClickListener() {
            servedForTheServer(view)
        }

    return view
    }
    private fun servedForTheServer(view: View) {
        val currentyear = currentDate.substring(6, 10)
        val year = 2100 - currentyear.toInt()
        val mysendt: MutableMap<String, Any> = HashMap()
        mysendt["currentyear"] = "$currentyear"
        mysendt["yearvalue"] = "$year"
        firebaseReferfulldata.child("$year").updateChildren(mysendt)
        val currentmonth = currentDate.substring(3, 5)
        val month = 13 - currentmonth.toInt()

        val currentdate = currentDate.substring(0,2)
        var date = 32 - currentdate.toInt()

        firebaseRefer = firebaseDatabase.getReference().child("User").child(uid).child("year")
            .child("$year").child("month").child("$month")
        firebaseRefer2 = firebaseDatabase.getReference().child("User").child(uid).child("daily")

        var sum = 0
        for (i in 0 until layout_list.childCount) {
            val view1: View = layout_list.getChildAt(i)
            val entry2: EditText = view1.findViewById(R.id.entry2)
            val work: EditText = view1.findViewById(R.id.work)
            val spinnershow: TextView = view1.findViewById(R.id.spinnershow)
            val myvalie = entry2.text.toString()
            val mycat = work.text.toString()
            val myspin = spinnershow.text.toString()

            if (TextUtils.isEmpty(myvalie)) {
                entry2.error = "Please Enter The Data"
                return
            } else if (TextUtils.isEmpty(mycat)) {
                work.error = "Please Enter The Data"
                return
            } else {
                sum += myvalie.toInt()
                val mysendtipp: MutableMap<String, Any> = HashMap()
                mysendtipp["entry2"] = myvalie
                mysendtipp["work"] = mycat
                mysendtipp["Spinner"] = myspin
                firebaseRefer2.child(valuefor).child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                firebaseRefer.child("date").child("$date").child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Data saved successfully for item $i")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error saving data for item $i", e)
                    }
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

        firebaseRefer.child("date").child("$date").updateChildren(allstru)
        firebaseRefer2.child(valuefor).updateChildren(allstru)


        monthlyp("$year","$month","$currentmonth",view)
    }

    private fun monthlyp(s: String, s1: String, s2: String, view: View) {
        firebaseRefer3 = firebaseDatabase.getReference().child("User").child(uid).child("monthly")
        firebaseRefer1 = firebaseDatabase.getReference().child("User").child(uid).child("year").child("$s").child("month").child("$s1").child("date")

        firebaseRefer1.addListenerForSingleValueEvent(object : ValueEventListener {
            var totalexpenses1:Int = 0
            var totalrevenue:Int = 0
            var totalsaving:Int = 0
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ipo in snapshot.children){
                    val totalex = ipo.child("Expenses").getValue().toString()
                    val totalre = ipo.child("entry").getValue().toString()
                    val totalsa = ipo.child("income").getValue().toString()

                    totalexpenses1+=totalex.toInt()
                    totalrevenue+=totalre.toInt()
                    totalsaving+=totalsa.toInt()

                    var year = currentDate.substring(6,10)
                    var month = currentDate.substring(3,5)

                    var monthvalue = ""
                    var monthvalue1 = ""

                    when(month){
                        "01" ->{monthvalue = "Jan"
                        monthvalue1 = "January"}
                        "02" ->{monthvalue = "Feb"
                        monthvalue1 = "Febrary"}
                        "03" ->{monthvalue = "Mar"
                        monthvalue1 = "March"}
                        "04" ->{monthvalue = "Apr"
                        monthvalue1 = "April"}
                        "05" ->{monthvalue = "May"
                        monthvalue1 = "May"}
                        "06" ->{monthvalue = "Jun"
                        monthvalue1 = "June"}
                        "07" ->{monthvalue = "Jul"
                        monthvalue1 = "July"}
                        "08" ->{monthvalue = "Aug"
                        monthvalue1 = "August"}
                        "09" ->{monthvalue = "Sep"
                        monthvalue1 = "September"}
                        "10" ->{monthvalue = "Oct"
                        monthvalue1 = "October"}
                        "11" ->{monthvalue = "Nov"
                        monthvalue1 = "November"}
                        "12" ->{monthvalue = "Dec"
                        monthvalue1 = "December"}

                    }

                    val mysendtp: MutableMap<String, Any> = HashMap()
                    mysendtp["totalexpenses"] = "$totalexpenses1"
                    mysendtp["totalrevenue"] = "$totalrevenue"
                    mysendtp["totalsaving"] = "$totalsaving"
                    mysendtp["currentmonth"] = "$monthvalue1"
                    mysendtp["monthvalue"] = ("$monthvalue " + "$year")

                    firebaseRefer3.child(valuefor1).setValue(mysendtp)
                    firebaseReferfulldata1.child("$s").child("month").child("$s1").updateChildren(mysendtp)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Data Send Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "There Are Some Problem",
                                Toast.LENGTH_SHORT
                            )
                                .show()
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
        val entry2: EditText = view.findViewById(R.id.entry2)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val spinnershow: TextView = view.findViewById(R.id.spinnershow)
        var category = arrayOf("None","Food","Study","Cloths","Vehicle","Other")
        val items = arrayOf("Item 1", "Item 2", "Item 3")
        val adapter = ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, category)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnershow.text = category[position].toString()
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
        // Safely remove view and update totals
        val entry2: EditText = view.findViewById(R.id.entry2)
        val mytkvl: Int = entry2.text.toString().toIntOrNull() ?: 0
        totalMytkl -= mytkvl
        expences.text = totalMytkl.toString()

        // Update income
        val entryValue: Int = entry.text.toString().toIntOrNull() ?: 0
        val totalIncome = entryValue - totalMytkl
        income.text = if (totalIncome >= 0) totalIncome.toString() else "-${-totalIncome}"

        layout_list.removeView(view)
    }


    //Calender = Date
    override fun onDestroy() {
        super.onDestroy()

        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateTimeRunnable)
    }
}