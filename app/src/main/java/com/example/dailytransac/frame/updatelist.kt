package com.example.dailytransac.frame

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.datafetch_adapter
import com.example.dailytransac.kuna.datafetch_model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class updatelist : Fragment() {

    lateinit var layout_list: LinearLayout
    lateinit var calenderView: LinearLayout
    lateinit var add_button: Button
    lateinit var entry: EditText
    lateinit var expences: TextView
    lateinit var income: TextView
    lateinit var sand: ImageButton
    lateinit var calendarTextView: TextView
    lateinit var sumbit: Button
    private lateinit var handler: Handler
    lateinit var custumDate:String
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var firebaseRefer: DatabaseReference
    private lateinit var firebaseRefer1: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseReferfulldata: DatabaseReference
    private lateinit var firebaseReferfulldata1: DatabaseReference
    private lateinit var firebasedatafetch: DatabaseReference
    private lateinit var firebasedatafetch1: DatabaseReference
    private lateinit var valuefor:String
    var totalMytkl:Int = 0
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    var uid = firebaseAuth.currentUser?.uid!!
    var datepicker = Calendar.getInstance()

    init{
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseReferfulldata = firebaseDatabase.getReference().child("User").child(uid).child("year")
        firebasedatafetch1 = firebaseDatabase.getReference().child("User").child(uid).child("year")
        firebaseReferfulldata1 = firebaseDatabase.getReference().child("User").child(uid).child("year")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_updatelist, container, false)

        calendarTextView = view.findViewById(R.id.calender)
        calenderView = view.findViewById(R.id.Celender_View)
        entry = view.findViewById(R.id.update_entry1)
        expences = view.findViewById(R.id.update_Expenses)
        income = view.findViewById(R.id.update_saving)
        sumbit = view.findViewById(R.id.update_submit)
        layout_list = view.findViewById(R.id.update_Layout_list)
        add_button = view.findViewById(R.id.update_add)


        calenderView.setOnClickListener(){
            showDataPicker(view)

        }

        //DynamicView

        add_button.setOnClickListener() {
            addcard("", "")
        }
        sumbit.setOnClickListener(){
            servedForTheServer()
        }


        return view
    }

    private fun showDataPicker(view: View) {
        val datapickerDialog = DatePickerDialog(requireContext(),{DatePicker, year:Int, monthOfYear:Int, dayOfMonth:Int ->
            val selectedate = Calendar.getInstance()
            selectedate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            custumDate= dateFormat.format(selectedate.time)
            calendarTextView.setText(custumDate)

            var p = calendarTextView.text.toString()
            if (p.isNotEmpty()){
                datafetch(view)
            }
        },
            datepicker.get(Calendar.YEAR),
            datepicker.get(Calendar.MONTH),
            datepicker.get(Calendar.DAY_OF_MONTH)
        )
        datapickerDialog.show()

    }

    private fun datafetch(view: View?) {
        val currentyear = custumDate.substring(6, 10)
        val year = 2100 - currentyear.toInt()
        val currentmonth = custumDate.substring(3, 5)
        val month = 13 - currentmonth.toInt()
        val currentdate = custumDate.substring(0, 2)
        val date = 32 - currentdate.toInt()
        firebasedatafetch = firebaseDatabase.getReference().child("User").child(uid)
            .child("year").child("$year").child("month")
            .child("$month").child("date").child("$date")
        firebasedatafetch.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(io in snapshot.children){
                    var totalexp = io.child("Expenses").getValue(String::class.java) ?: ""
                    var totalrev = io.child("entry").getValue(String::class.java) ?: ""
                    var totalsav = io.child("income").getValue(String::class.java) ?: ""
                    val datevalue = io.child("datevalue").getValue(String::class.java) ?: ""

                    if (totalexp != ""){
                        var exp = totalexp
                        var sav = totalsav
                        var rev = totalrev
                        expences.setText(exp)
                        entry.setText(sav)
                        income.setText(rev)
                        Log.d("mio",""+exp)
                        Log.d("mio","$sav")
                        Log.d("mio","$rev")
                        Log.d("mio",datevalue)
                        Log.d("mio","$year")
                        Log.d("mio","$month")
                        Log.d("mio","$date")



                        firebasedatafetch1.child("$year").child("month")
                            .child("$month").child("date").child("$date").child("dateri")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.children.forEach { dataSnapshot ->
                                        val entry2 = dataSnapshot.child("entry2").getValue(String::class.java) ?: ""
                                        val work = dataSnapshot.child("work").getValue(String::class.java) ?: ""
                                        var wo = work.toString()
                                        var en = entry2.toString()



                                        Log.d("mio","$entry2")
                                        Log.d("mio","$work")

                                        addcard(entry2,work)

                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.e("Firebase", "Additional data fetch failed", error.toException())
                                }
                            })
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun servedForTheServer() {
        val currentyear = custumDate.substring(6, 10)
        val year = 2100 - currentyear.toInt()
        val mysendt: MutableMap<String, Any> = HashMap()
        mysendt["currentyear"] = "$currentyear"
        mysendt["yearvalue"] = "$year"
        firebaseReferfulldata.child("$year").updateChildren(mysendt)
        val currentmonth = custumDate.substring(3, 5)
        val month = 13 - currentmonth.toInt()

        firebaseRefer = firebaseDatabase.getReference().child("User").child(uid).child("year").child("$year").child("month").child("$month")

        var sum = 0
        for (i in 0 until layout_list.childCount) {
            val view: View = layout_list.getChildAt(i)
            val entry2: EditText = view.findViewById(R.id.entry2)
            val work: EditText = view.findViewById(R.id.work)
            val spinnershow: TextView = view.findViewById(R.id.spinnershow)
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
                firebaseRefer.child("date").child(valuefor).child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
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
        allstru["mydateg"] = custumDate

        firebaseRefer.child("date").child(valuefor).updateChildren(allstru)
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

        monthlyp("$year","$month","$currentmonth")
    }

    private fun monthlyp(s: String, s1: String, s2: String) {
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

                    val mysendtp: MutableMap<String, Any> = HashMap()
                    mysendtp["totalexpenses"] = "$totalexpenses1"
                    mysendtp["totalrevenue"] = "$totalrevenue"
                    mysendtp["totalsaving"] = "$totalsaving"
                    mysendtp["currentmonth"] = "$s2"
                    mysendtp["monthvalue"] = "$s1"

                    firebaseReferfulldata1.child("$s").child("month").child("$s1").updateChildren(mysendtp)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //DynamicView
    private fun addcard(entry22: String, work2: String) {
        val view:View = layoutInflater.inflate(R.layout.add_list,null)
        layout_list.addView(view)
        val entry2:EditText = view.findViewById(R.id.entry2)
        val work:EditText = view.findViewById(R.id.entry2)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val spinnershow:TextView= view.findViewById(R.id.spinnershow)
        var category = arrayOf("None","Food","Study","Cloths","Vehicle","Other")
        val arrayAdp = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,category)
        spinner.adapter = arrayAdp
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
            entry2.setText("$entry22")
            work.setText("$work2")
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