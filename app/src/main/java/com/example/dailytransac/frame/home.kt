package com.example.dailytransac.frame

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.home_spinner_adapter
import com.example.dailytransac.kuna.home_spinner_adapter_add
import com.example.dailytransac.kuna.home_spinner_model
import com.example.dailytransac.kuna.home_spinner_model_add
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
    private lateinit var spinnerReference: DatabaseReference
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var firebaseRefer: DatabaseReference
    private lateinit var firebaseRefer1: DatabaseReference
    private lateinit var firebaseRefer2: DatabaseReference
    private lateinit var firebaseRefer3: DatabaseReference
    private lateinit var yearspinner: DatabaseReference
    private lateinit var fetchDataforview: DatabaseReference
    private lateinit var add_data_for_spinner: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseReferfulldata: DatabaseReference
    private lateinit var firebaseReferfulldata1: DatabaseReference
    private lateinit var valuefor:String
    private lateinit var valuefor1:String
    private lateinit var listOfMonth: ArrayList<home_spinner_model>
    private lateinit var listOfMonth1: ArrayList<home_spinner_model_add>
    private lateinit var adapter: home_spinner_adapter
    private lateinit var adapter1: home_spinner_adapter_add
    var totalMytkl:Int = 0
    var addSpinnervalue:Int = 10000000
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    var uid = firebaseAuth.currentUser?.uid!!

    init{
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseReferfulldata = firebaseDatabase.getReference().child("User").child(uid).child("year")
        firebaseReferfulldata1 = firebaseDatabase.getReference().child("User").child(uid).child("year")
        spinnerReference = FirebaseDatabase.getInstance().getReference()
            .child("User").child(uid).child("homespinner")
        add_data_for_spinner = FirebaseDatabase.getInstance().getReference()
            .child("User").child(uid).child("homespinner")
        fetchDataforview = FirebaseDatabase.getInstance().getReference().child("year")
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
            navigateToFragmentB()
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

    private fun addspinnerdata() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.home_add_spinner_data)

        var recyclerView1:RecyclerView = dialog.findViewById(R.id.home_recent_recycle)
        var adddataop:EditText = dialog.findViewById(R.id.home_adddata_box)
        var add_button:TextView = dialog.findViewById(R.id.home_add)
        var sumbit:TextView = dialog.findViewById(R.id.home_submit)

        listOfMonth1 = ArrayList()
        var layourmanger:LinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        layourmanger.reverseLayout = true
        layourmanger.stackFromEnd = true
        recyclerView1.layoutManager = layourmanger
        adapter1 = home_spinner_adapter_add(listOfMonth1)
        recyclerView1.adapter = adapter

        add_button.setOnClickListener(){
            var dataForSpinner = adddataop.text.toString()
            if (dataForSpinner.isEmpty()){
                adddataop.setError("Enter the value")
            }else{
                adddataop.setText("")
                val mysendtipp: MutableMap<String, Any> = HashMap()
                mysendtipp["homespin"] = dataForSpinner
                addSpinnervalue+=-1
                add_data_for_spinner.child("i$addSpinnervalue").setValue(mysendtipp)
                Toast.makeText(requireContext(),"Add Data Successful",Toast.LENGTH_SHORT).show()
                spinnerReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listOfMonth.clear()  // Clear existing data
                        for (ip in snapshot.children) {
                            val yearSpinner = ip.child("homespin").getValue(String::class.java) ?: ""
                            listOfMonth.add(home_spinner_model(yearSpinner))
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        spinnerReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfMonth1.clear()  // Clear existing data
                for (ip in snapshot.children) {
                    val yearSpinner = ip.child("homespin").getValue(String::class.java) ?: ""
                    listOfMonth1.add(home_spinner_model_add(yearSpinner))
                }
                adapter1.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
        sumbit.setOnClickListener(){
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun navigateToFragmentB() {
        val fragmentB = updatelist()
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame, fragmentB)
            .addToBackStack(null)
            .commit()
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

        val mysendt1: MutableMap<String, Any> = HashMap()
        mysendt1["yearspinner"] = "$currentyear"

        yearspinner = firebaseDatabase.getReference().child("User").child(uid).child("yearspinner")
        yearspinner.child("$year").setValue(mysendt1)

        firebaseRefer = firebaseDatabase.getReference().child("User").child(uid).child("year")
            .child("$year").child("month").child("$month")
        firebaseRefer2 = firebaseDatabase.getReference().child("User").child(uid).child("daily")

        var sum = 0
        for (i in 0 until layout_list.childCount) {
            val view1: View = layout_list.getChildAt(i)
            val entry2: EditText = view1.findViewById(R.id.entry2)
            val work: EditText = view1.findViewById(R.id.work)
            val spinnershow: TextView = view1.findViewById(R.id.home_spinnershow)
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
                firebaseRefer.child("date").child("$currentdate").child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                firebaseRefer.child("date1").child("$date").child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
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

        firebaseRefer.child("date").child("$currentdate").updateChildren(allstru)
        firebaseRefer.child("date1").child("$date").updateChildren(allstru)
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
                    firebaseReferfulldata1.child("$s").child("month1").child("$month").updateChildren(mysendtp)
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
        val entry2: EditText = view.findViewById(R.id.entry2)
        val spinnershow: TextView = view.findViewById(R.id.home_spinnershow)
        layout_list.addView(view)

        var currentyear = currentDate.substring(6,10)
        var currentmonth = currentDate.substring(3,5)
        var currentdate = currentDate.substring(0,2)

        var year = 2100 - currentyear.toInt()
        var month = 13 - currentmonth.toInt()
        var date = 32 - currentdate.toInt()

        fetchDataforview.child("$year").child("month").child("$month").child("date1").child("$date")
        fetchDataforview.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ip in snapshot.children){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        spinnershow.setOnClickListener(){
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.home_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.home_recycle)
            val searchView:androidx.appcompat.widget.SearchView = dialog.findViewById(R.id.home_searchView)
            val adddata :TextView = dialog.findViewById(R.id.home_adddata)
            adddata.setOnClickListener(){
                addspinnerdata()
            }

            listOfMonth = ArrayList()
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = home_spinner_adapter(listOfMonth){ dattaspinner->
                spinnershow.setText(dattaspinner.text.toString())
                dialog.dismiss()
            }
            recyclerView.adapter = adapter

            searchView.clearFocus()
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }
            })

            spinnerReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfMonth.clear()  // Clear existing data
                    for (ip in snapshot.children) {
                        val yearSpinner = ip.child("homespin").getValue(String::class.java) ?: ""
                        listOfMonth.add(home_spinner_model(yearSpinner))
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

            dialog.show()
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
    private fun filter(newText: String?) {
        val filteredList = listOfMonth.filter {
            it.text.toLowerCase(Locale.ROOT).contains(newText?.toLowerCase(Locale.ROOT) ?: "")
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
        }
        adapter.setAdapterList(ArrayList(filteredList))
    }


    //Calender = Date
    override fun onDestroy() {
        super.onDestroy()

        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateTimeRunnable)
    }
}