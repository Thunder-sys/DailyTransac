package com.example.dailytransac.frame

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class updatelist : Fragment() {
    var datepicker = Calendar.getInstance()
    lateinit var layout_list: LinearLayout
    lateinit var add_button: Button
    lateinit var entry:EditText
    lateinit var expences:TextView
    lateinit var income:TextView
    lateinit var sand:ImageButton
    lateinit var updatebotton: Button
    lateinit var calendar:TextView
    lateinit var calendarTextView:LinearLayout
    lateinit var sumbit: Button
    private lateinit var handler: Handler
    lateinit var currentDate:String
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var spinnerReference: DatabaseReference
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var firebaseRefer: DatabaseReference
    private lateinit var firebaseReferremove: DatabaseReference
    private lateinit var firebaseRefer100: DatabaseReference
    private lateinit var firebaseRefer200: DatabaseReference
    private lateinit var firebaseRefer1: DatabaseReference
    private lateinit var firebaseRefer2: DatabaseReference
    private lateinit var firebaseRefer4: DatabaseReference
    private lateinit var firebaseRefer5: DatabaseReference
    private lateinit var firebaseRefer6: DatabaseReference
    private lateinit var firebaseRefer3: DatabaseReference
    private lateinit var yearspinner: DatabaseReference
    private lateinit var fetchDataforview: DatabaseReference
    private lateinit var add_data_for_spinner: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseReferfulldata: DatabaseReference
    private lateinit var firebaseReferfulldata1: DatabaseReference
    private lateinit var valuefor:String
    private lateinit var valuefor1:String
    private lateinit var valuefor2:String
    private lateinit var listOfMonth: ArrayList<home_spinner_model>
    private lateinit var listOfMonth1: ArrayList<home_spinner_model_add>
    private lateinit var adapter: home_spinner_adapter
    private lateinit var adapter1: home_spinner_adapter_add
    private lateinit var adapter2: home_spinner_adapter_add
    private val cardValuesMap = mutableMapOf<View, Int>()

    val formatte = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val finalDate = "30/12/2100"
    var totalMytkl:Int = 0
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    var uid = firebaseAuth.currentUser?.uid!!

    init{
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseReferfulldata = firebaseDatabase.getReference().child("User").child(uid).child("year")
        firebaseReferfulldata1 = firebaseDatabase.getReference().child("User").child(uid).child("year")
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("homespinner")
        add_data_for_spinner = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("homespinner")
        fetchDataforview = FirebaseDatabase.getInstance().getReference().child("year")
        firebaseRefer2 = firebaseDatabase.getReference().child("User").child(uid).child("daily")
        firebaseRefer4 = firebaseDatabase.getReference().child("User").child(uid).child("daily2")
        firebaseRefer5 = firebaseDatabase.getReference().child("User").child(uid).child("pie1")
        firebaseRefer6 = firebaseDatabase.getReference().child("User").child(uid).child("pie2")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_updatelist, container, false)

        calendar = view.findViewById(R.id.calender)
        calendarTextView = view.findViewById(R.id.Celender_View)
        entry = view.findViewById(R.id.update_entry1)
        expences = view.findViewById(R.id.update_Expenses)
        income = view.findViewById(R.id.update_saving)
        sumbit = view.findViewById(R.id.update_submit)
        layout_list = view.findViewById(R.id.update_Layout_list)
        add_button = view.findViewById(R.id.update_add)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())
                calendar.text = currentDate
                var dataStringm = currentDate.toString()
                var dataStringm2 = currentDate.substring(3,5)
                var dataStringm3 = currentDate.substring(6,10)
                var reversedate = ("$dataStringm3" + "$dataStringm2").toInt()

                valuefor1 = (reversedate).toString()
                if (layout_list.childCount.toString() == "0") {
                    addcard("", "", "None",view)
                }

                val initiadate = LocalDate.parse(finalDate, formatte)
                val initda = initiadate.toEpochDay().toInt()
                val initiadate2 = LocalDate.parse(dataStringm, formatte)
                val initda2 = initiadate2.toEpochDay().toInt()
                Log.d("Mus", "" + initda)
                valuefor = (initda - initda2).toString()
                valuefor2 = initda2.toString()
                fetchdataforview(view)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)


        calendarTextView.setOnClickListener(){
            showDataPicker(view)

        }

        //DynamicView
        add_button.setOnClickListener() {
            addcard("", "", "",view)
        }
        sumbit.setOnClickListener(){
            if (layout_list.childCount.toString()=="0"){
                Toast.makeText(requireContext(),"Please Add Data",Toast.LENGTH_SHORT).show()
            }
            else {
                servedForTheServer(view)
            }
        }


        return view
    }

    private fun showDataPicker(view: View) {
        val datapickerDialog = DatePickerDialog(requireContext(),{DatePicker, year:Int, monthOfYear:Int, dayOfMonth:Int ->
            val selectedate = Calendar.getInstance()
            selectedate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            currentDate= dateFormat.format(selectedate.time)
            calendar.setText(currentDate)
            var dataStringm = currentDate.toString()
            var dataStringm2 = currentDate.substring(3,5)
            var dataStringm3 = currentDate.substring(6,10)
            var reversedate = ("$dataStringm3" + "$dataStringm2").toInt()

            valuefor1 = (reversedate).toString()

            val initiadate = LocalDate.parse(finalDate, formatte)
            val initda = initiadate.toEpochDay().toInt()
            val initiadate2 = LocalDate.parse(dataStringm, formatte)
            val initda2 = initiadate2.toEpochDay().toInt()
            Log.d("Mus", "" + initda)
            valuefor = (initda - initda2).toString()
            valuefor2 = initda2.toString()


            var p = calendar.text.toString()
            if (p.isNotEmpty()){
                entry.setText("")
                expences.setText("0")
                totalMytkl= 0
                fetchdataforview(view)
                layout_list.removeAllViews()
                cardValuesMap.clear()
            }
        },
            datepicker.get(Calendar.YEAR),
            datepicker.get(Calendar.MONTH),
            datepicker.get(Calendar.DAY_OF_MONTH)
        )
        datapickerDialog.show()


    }

    private fun addspinnerdata() {
        val view1 = Dialog(requireContext()).apply {
            setContentView(R.layout.home_add_spinner_data)
            setCancelable(true)
        }

        val addDataOp: EditText = view1.findViewById(R.id.home_adddata_box)
        val addButton: TextView = view1.findViewById(R.id.home_submit)

        addButton.setOnClickListener {
            val dataForSpinner = addDataOp.text.toString().trim()
            if (dataForSpinner.isEmpty()) {
                addDataOp.error = "Enter a value"
            } else {
                checkIfValueExists(dataForSpinner) { exists ->
                    if (exists) {
                        Toast.makeText(requireContext(), "Value already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        val mySendTip: MutableMap<String, Any> = HashMap()
                        mySendTip["homespin"] = dataForSpinner
                        val randomKey = UUID.randomUUID().toString()

                        add_data_for_spinner.child(randomKey).setValue(mySendTip)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Add Data Successful", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        view1.dismiss()
                    }
                }
            }
        }

       view1.show()
    }

    private fun checkIfValueExists(value: String, callback: (Boolean) -> Unit) {
        add_data_for_spinner.orderByChild("homespin").equalTo(value).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                callback(false)
            }
        })
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

        daily_data_remove()
        pie_data_remove(year,month,date)
        full_data_remove(year,month,date,currentdate)

        yearspinner = firebaseDatabase.getReference().child("User").child(uid).child("yearspinner")
        yearspinner.child("$year").setValue(mysendt1)
        var op = 0

        firebaseRefer = firebaseDatabase.getReference().child("User").child(uid).child("year").child("$year").child("month").child("$month")

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
                val mysendtipp1: MutableMap<String, Any> = HashMap()
                mysendtipp1["valueno"] = "$i"
                val mysendtipp: MutableMap<String, Any> = HashMap()
                mysendtipp["entry2"] = myvalie
                mysendtipp["work"] = mycat
                mysendtipp["Spinner"] = myspin
                firebaseRefer6.child("$year"+"$month"+"$date"+"$i").setValue(mysendtipp)
                firebaseRefer2.child(valuefor).child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                firebaseRefer5.child(valuefor2).child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                firebaseRefer.child("date").child("$currentdate").child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                firebaseRefer.child("date1").child("$date").child("dateri").child("Myfirstdata$i").setValue(mysendtipp)
                firebaseRefer.child("date1").child("$date").child("dater").child("op1").child("op2").updateChildren(mysendtipp1)

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
        val allstru1: MutableMap<String, Any> = HashMap()
        allstru1["entry"] = myallsum
        allstru["entry"] = myallsum
        allstru["Expenses"] = allexper
        allstru["income"] = myallsav.toString()
        allstru["datevalue"] = valuefor
        allstru["mydateg"] = currentDate

        firebaseRefer.child("date").child("$currentdate").updateChildren(allstru)
        firebaseRefer.child("date1").child("$date").updateChildren(allstru)
        firebaseRefer.child("date1").child("$date").child("dater").child("op").updateChildren(allstru)
        firebaseRefer2.child(valuefor).updateChildren(allstru)
        firebaseRefer4.child(valuefor2).child("date1").child("date").updateChildren(allstru)
        firebaseRefer4.child(valuefor2).updateChildren(allstru)


        monthlyp("$year","$month","$currentmonth",view)
    }

    private fun full_data_remove(year: Int, month: Int, date: Int, currentdate: String) {
        var note5 = firebaseDatabase.getReference().child("User").child(uid).child("year")
            .child("$year").child("month").child("$month").child("date")
        var note6 = firebaseDatabase.getReference().child("User").child(uid).child("year")
            .child("$year").child("month").child("$month").child("date1")

        note5.child("$currentdate").removeValue()
        note6.child("$date").removeValue()
    }

    private fun pie_data_remove(year: Int, month: Int, date: Int) {
        var note3 = firebaseDatabase.getReference().child("User").child(uid).child("pie1")
        var note4 = firebaseDatabase.getReference().child("User").child(uid).child("pie2")
        var ip = 0
        var opi = 100
        note3.child("$valuefor2").removeValue()

        while (ip <= opi) {
            note4.child("$year" + "$month" + "$date" + "$ip").removeValue()
            ip += 1
        }
    }

    private fun daily_data_remove() {
        var note1 = firebaseDatabase.getReference().child("User").child(uid).child("daily")
        var note2 = firebaseDatabase.getReference().child("User").child(uid).child("daily2")

        note1.child("$valuefor").removeValue()
        note2.child("$valuefor1").removeValue()

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

                    firebaseRefer3.child(valuefor1).updateChildren(mysendtp)
                    firebaseRefer3.child(valuefor1).child("op1").child("op").updateChildren(mysendtp)
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

    private fun fetchdataforview(view: View){
        var minidate = calendar.text.toString()
        var yeardate = minidate.substring(6,10)
        var monthdate = minidate.substring(3,5)
        var datedate = minidate.substring(0,2)
        var year = 2100 - yeardate.toInt()
        var month = 13 - monthdate.toInt()
        var date = 32 - datedate.toInt()
        val formatte = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        firebaseRefer100 = FirebaseDatabase.getInstance().getReference().child("User")
            .child(uid).child("year").child("$year").child("month")
            .child("$month").child("date1").child("$date").child("dater")

        firebaseRefer200 = FirebaseDatabase.getInstance().getReference().child("User")
            .child(uid).child("year").child("$year").child("month")
            .child("$month").child("date1").child("$date").child("dateri")
        var op = ""

        firebaseRefer100.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var entry11 = i.child("entry").getValue().toString()

                    op = entry11.toString()

                    entry.setText("$op")
                    firebaseRefer200.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (ip in snapshot.children) {

                                var entry12 = ip.child("entry2").getValue().toString()
                                var spinner1 = ip.child("Spinner").getValue().toString()
                                var work1 = ip.child("work").getValue().toString()

                                addcard(entry12,work1,spinner1,view)
                            }

                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                if (op==""){
                    if (layout_list.childCount.toString() == "0") {
                        addcard("", "", "None",view)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //DynamicView
    private fun addcard(entry12:String,work12:String,spinner12:String,view1: View) {
        val view: View = layoutInflater.inflate(R.layout.add_list, null)
        val entry2: EditText = view.findViewById(R.id.entry2)
        val work: EditText = view.findViewById(R.id.work)
        val spinnershow: TextView = view.findViewById(R.id.home_spinnershow)
        layout_list.addView(view)

        entry2.setText("$entry12")
        work.setText("$work12")
        spinnershow.setText("$spinner12")


        // Initialize the old value for this new card
        cardValuesMap[view] = 0

        spinnershow.setOnClickListener {

            val view1 = Dialog(requireContext())
            view1.setContentView(R.layout.home_spinner_show)

            var simmerview: ShimmerFrameLayout = view1.findViewById(R.id.shimmer12)
            val recyclerView: RecyclerView = view1.findViewById(R.id.home_recycle)
            val refresh: ImageView = view1.findViewById(R.id.home_spinner_refresh)
            val searchView: androidx.appcompat.widget.SearchView = view1.findViewById(R.id.home_searchView)
            val adddata: TextView = view1.findViewById(R.id.home_adddata)
            adddata.setOnClickListener {
                addspinnerdata()
            }

            simmereffect(simmerview)
            listOfMonth = ArrayList()
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = home_spinner_adapter(listOfMonth) { dattaspinner ->
                spinnershow.text = dattaspinner.text.toString()
                view1.dismiss()
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
            refresh.setOnClickListener { oprefreshdata(simmerview,recyclerView) }

            // Initialize periodic updates
            handler = Handler(Looper.getMainLooper())
            updateTimeRunnable = object : Runnable {
                override fun run() {
                    oprefreshdata(simmerview, recyclerView)
                }
            }
            handler.post(updateTimeRunnable) // Start periodic updates

            view1.show()
        }

        entry2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newValue: Int = try {
                    s.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }

                // Update the value for this specific card
                val oldValue = cardValuesMap[view] ?: 0
                cardValuesMap[view] = newValue

                // Adjust total values
                totalMytkl = totalMytkl - oldValue + newValue
                expences.text = totalMytkl.toString()

                val entryValue: Int = try {
                    entry.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val totalIncome = entryValue - totalMytkl
                when {
                    entryValue > totalIncome -> income.text = "$totalIncome"
                    entryValue == totalIncome -> income.text = "0"
                    else -> income.text = "- $totalIncome"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        if(entry12.isNotEmpty()) {
            val newValue: Int = try {
                entry12.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }

            // Update the value for this specific card
            val oldValue = cardValuesMap[view] ?: 0
            cardValuesMap[view] = newValue

            // Adjust total values
            totalMytkl = totalMytkl - oldValue + newValue
            expences.text = totalMytkl.toString()

            val entryValue: Int = try {
                entry.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }
            val totalIncome = entryValue - totalMytkl
            when {
                entryValue > totalIncome -> income.text = "$totalIncome"
                entryValue == totalIncome -> income.text = "0"
                else -> income.text = "- $totalIncome"
            }
        }

        view.findViewById<ImageButton>(R.id.delete).setOnClickListener {
            removeCard(view)
        }
    }

    private fun oprefreshdata(simmerview: ShimmerFrameLayout, recyclerView: RecyclerView) {
        spinnerReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfMonth.clear()  // Clear existing data
                for (ip in snapshot.children) {
                    val yearSpinner = ip.child("homespin").getValue(String::class.java) ?: ""
                    val id = ip.child("spinid").getValue(String::class.java) ?: ""
                    listOfMonth.add(home_spinner_model(id,yearSpinner))

                    recyclerView.visibility = View.VISIBLE
                    simmerview.visibility = View.GONE
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })    }

    // Function to remove a card and update values
    private fun removeCard(view: View) {
        // Get the old value for this card
        val oldValue = cardValuesMap[view] ?: 0

        // Remove the card view
        layout_list.removeView(view)

        // Remove the old value from the total
        totalMytkl -= oldValue
        expences.text = totalMytkl.toString()

        // Update the income value
        val entryValue: Int = try {
            entry.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val totalIncome = entryValue - totalMytkl
        when {
            entryValue > totalIncome -> income.text = "$totalIncome"
            entryValue == totalIncome -> income.text = "0"
            else -> income.text = "- $totalIncome"
        }

        // Remove the value from the map
        cardValuesMap.remove(view)
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
    private fun simmereffect(
        shimmereffect1: ShimmerFrameLayout
    ) {
        shimmereffect1.visibility = View.VISIBLE

    }
}