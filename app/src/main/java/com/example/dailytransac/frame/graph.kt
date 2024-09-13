package com.example.dailytransac.frame

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.security.identity.CredentialDataResult.Entries
import android.util.Log
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.Database.ModelClassformaiji
import com.example.dailytransac.Database.ModelClassformainn
import com.example.dailytransac.R
import com.example.dailytransac.kuna.graph_reco_model
import com.example.dailytransac.kuna.graph_spinner_adapter
import com.example.dailytransac.kuna.graph_spinner_model
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import graph_reco_adapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class graph : Fragment() {

    private val calendar: Calendar = Calendar.getInstance()
    private val calendar1: Calendar = Calendar.getInstance()


    val dately = mutableListOf<String>()
    lateinit var a1: TextView
    lateinit var a2: TextView
    lateinit var a3: TextView
    lateinit var a4: TextView
    lateinit var a5: TextView
    lateinit var a6: TextView
    lateinit var a7: TextView
    lateinit var a8: TextView
    lateinit var a9: TextView
    lateinit var a10: TextView
    lateinit var a11: TextView
    lateinit var a12: TextView
    lateinit var show_month: TextView
    lateinit var show_year: TextView
    lateinit var less: ImageView
    lateinit var greater: ImageView
    lateinit var yearvalue: TextView

    lateinit var cancel: TextView
    lateinit var ok: TextView

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid: String = firebaseAuth.currentUser?.uid ?: ""
    private lateinit var handler: Handler
    private lateinit var updateTimeRunnable: Runnable

    private lateinit var spinnerReference: DatabaseReference
    private lateinit var spinnerReference1: DatabaseReference
    private lateinit var pieReference: DatabaseReference
    private lateinit var fetchdataReference: DatabaseReference
    private lateinit var showSpinner: TextView
    private lateinit var listOfMonth: ArrayList<graph_spinner_model>
    private lateinit var adapter: graph_spinner_adapter
    private lateinit var barchart: BarChart
    private lateinit var linechart: LineChart
    private lateinit var piechart: PieChart
    lateinit var reco: RecyclerView
    lateinit var spinner: Spinner

    lateinit var text_rev: TextView
    lateinit var text_exp: TextView
    lateinit var spinnershowq: TextView

    lateinit var monthyear_less: ImageView
    lateinit var monthyear_great: ImageView
    lateinit var monthyear_valueshow: TextView
    lateinit var monthyear_Linear: LinearLayout
    lateinit var year_less: ImageView
    lateinit var year_great: ImageView
    lateinit var year_valueshow: TextView
    lateinit var year_Linear: LinearLayout

    lateinit var firstdateShow: TextView
    lateinit var lastdateshow: TextView
    lateinit var dateshowLinear: LinearLayout


    val entries = mutableListOf<PieEntry>()
    val categoryMap = mutableMapOf<String, Float>()
    var barlist_1 = ArrayList<BarEntry>()
    var barlist_2 = ArrayList<BarEntry>()
    var linelist_1 = ArrayList<Entry>()
    var linelist_2 = ArrayList<Entry>()

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)
        sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        reco = view.findViewById(R.id.graph_reco)
        spinner = view.findViewById(R.id.appCompatSpinner)
        spinnershowq = view.findViewById(R.id.graph_select_catergory_item)
        monthyear_less = view.findViewById(R.id.graph_month_and_year_less_than)
        monthyear_great = view.findViewById(R.id.graph_month_and_year_greater_than)
        monthyear_Linear = view.findViewById(R.id.Linear_month_and_year)
        monthyear_valueshow = view.findViewById(R.id.graph_month_and_year_value)
        year_less = view.findViewById(R.id.graph_year_less_than)
        year_great = view.findViewById(R.id.graph_year_greater_than)
        year_Linear = view.findViewById(R.id.Linear_year)
        year_valueshow = view.findViewById(R.id.graph_year_value)

        firstdateShow = view.findViewById(R.id.Linear_first_date)
        lastdateshow = view.findViewById(R.id.Linear_last_date)
        dateshowLinear = view.findViewById(R.id.Linear_for_date)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Handle selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view1: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String
                spinnershowq.setText("$selectedItem")
                when ("$selectedItem") {
                    "Month" -> {
                        monthyear_Linear.visibility = View.VISIBLE
                        dateshowLinear.visibility = View.GONE
                        year_Linear.visibility = View.GONE
                        updateDateDisplay(view)
                    }

                    "Custom" -> {
                        monthyear_Linear.visibility = View.GONE
                        dateshowLinear.visibility = View.VISIBLE
                        year_Linear.visibility = View.GONE
                    }

                    "Year" -> {
                        monthyear_Linear.visibility = View.GONE
                        dateshowLinear.visibility = View.GONE
                        year_Linear.visibility = View.VISIBLE
                        calendar.add(Calendar.YEAR, -0)
                        updateDatedisplay(view)
                    }

                    "OverAll" -> {
                        monthyear_Linear.visibility = View.GONE
                        dateshowLinear.visibility = View.GONE
                        year_Linear.visibility = View.GONE

                        fetchfulldataValueset(view, "0", "0")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no item is selected if necessary
            }
        }

        dateshowLinear.setOnClickListener {
            showDateRangePicker(view)
        }
        monthyear_Linear.setOnClickListener {
            monthPicker(view, calendar)
        }
        monthyear_great.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateDateDisplay(view)
        }
        monthyear_less.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateDateDisplay(view)
        }
        year_great.setOnClickListener {
            calendar.add(Calendar.YEAR, 1)
            updateDatedisplay(view)
        }
        year_less.setOnClickListener {
            calendar.add(Calendar.YEAR, -1)
            updateDatedisplay(view)
        }
        text_rev = view.findViewById(R.id.graph_revenue)
        text_exp = view.findViewById(R.id.graph_expenses)

        spinnerReference = FirebaseDatabase.getInstance().getReference().child("User")
            .child(uid).child("yearspinner")

        return view
    }

    private fun updateDatedisplay(view: View) {
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time)

        year_valueshow.text = "$dateString"

        var op = year_valueshow.text.toString()

        var a1 = "01/01/$op"
        var a2 = "31/12/$op"
        Log.d("year","$a1")
        Log.d("year","$a2")

        fetchfulldataValueset(view, a1, a2)

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

        var op = calendar.get(Calendar.MONTH).toString()
        calendar.get(Calendar.YEAR)
        updatedateDisplay()
        when (op) {
            "1" -> setcolor("2")
            "2" -> setcolor("3")
            "3" -> setcolor("4")
            "4" -> setcolor("5")
            "5" -> setcolor("6")
            "6" -> setcolor("7")
            "7" -> setcolor("8")
            "8" -> setcolor("9")
            "9" -> setcolor("10")
            "10" -> setcolor("11")
            "11" -> setcolor("12")
            "12" -> setcolor("13")
        }


        a1.setOnClickListener() {
            setcolor("1")
        }
        a2.setOnClickListener() {
            setcolor("2")
        }
        a3.setOnClickListener() {
            setcolor("3")
        }
        a4.setOnClickListener() {
            setcolor("4")
        }
        a5.setOnClickListener() {
            setcolor("5")
        }
        a6.setOnClickListener() {
            setcolor("6")
        }
        a7.setOnClickListener() {
            setcolor("7")
        }
        a8.setOnClickListener() {
            setcolor("8")
        }
        a9.setOnClickListener() {
            setcolor("9")
        }
        a10.setOnClickListener() {
            setcolor("10")
        }
        a11.setOnClickListener() {
            setcolor("11")
        }
        a12.setOnClickListener() {
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

        ok.setOnClickListener() {
            var op1 = show_month.text.toString()
            var op2 = show_year.text.toString()
            var yearvalue = op2.toInt()

            when (op1) {
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
        cancel.setOnClickListener() {
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

        when (s) {
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
        val dateString = dateFormat.format(calendar.time).toString().toInt()
        val dateString1 = dateFormat1.format(calendar.time).toString()

        val monthNames = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        val monthvalue = monthNames[dateString.toInt() - 1]

        monthyear_valueshow.text = "$monthvalue $dateString1"

        var a1 = "01/0$dateString/$dateString1"
        var a2 = "31/0$dateString/$dateString1"
        Log.d("month","$a1")
        Log.d("month","$a2")

        fetchfulldataValueset(view, a1, a2)

    }

    private fun showDateRangePicker(view: View) {
        // Create a MaterialDatePicker instance for date range selection
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .build()

        // Set up listener for when the user selects a date range
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            // Convert and display the selected date range
            firstdateShow.text = formatDate(startDate)
            lastdateshow.text = formatDate(endDate)

            var a1 = formatDate(startDate).toString()
            var a2 = formatDate(endDate).toString()
            Log.d("range","$a1")
            Log.d("range","$a2")

            fetchfulldataValueset(view, a1, a2)
        }

        // Show the date range picker
        dateRangePicker.show(parentFragmentManager, "date_range_picker")
    }

    private fun formatDate(millis: Long?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return millis?.let { dateFormat.format(it) } ?: "Not selected"
    }

    private fun fetchfulldataValueset(view: View, fino: String, firo: String) {
        val formatte1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val finalDate = "30/12/2100"
        val initiadate = LocalDate.parse(finalDate, formatte1)
        val main = initiadate.toEpochDay().toInt()
        var iop = spinnershowq.text.toString()

        if (iop == "Month" || iop == "Custom") {
            var first = fino.substring(0, 10)
            var end = firo.substring(0, 10)
            var inaidate = "$first"
            var final = "$end"

            val initiadate1 = LocalDate.parse(inaidate, formatte1)
            val dfirst = initiadate1.toEpochDay().toInt()
            val initiadat2 = LocalDate.parse(final, formatte1)
            val dend = initiadat2.toEpochDay().toInt()

            var fin = dfirst
            var fir = dend
            Log.d("miop", "$fir")
            Log.d("miop", "$fin")
            fetchdataformfirebase(view, fir, fin)
        }
        else if (iop=="Year"){

            var first = fino.substring(0, 10)
            var end = firo.substring(0, 10)
            var firstmo = fino.substring(3, 5).toInt()
            var endmo = firo.substring(3, 5).toInt()
            var firstmon = fino.substring(6,10).toInt()
            var endmon = firo.substring(6, 10).toInt()
            var inaidate = "$first"
            var final = "$end"

            val initiadate1 = LocalDate.parse(inaidate, formatte1)
            val dfirst = initiadate1.toEpochDay().toInt()
            val initiadat2 = LocalDate.parse(final, formatte1)
            val dend = initiadat2.toEpochDay().toInt()

            var lastmo = ("$endmon"+"$endmo").toInt()
            var startmo = ("$firstmon"+"0$firstmo").toInt()

            var fin = dfirst
            var fir = dend
            Log.d("miop", "$fir")
            Log.d("miop", "$fin")
            fetchdataformonth(view,fir,fin,startmo,lastmo)
        }
        else {
            var fin = 0
            var fir = 0
            fetchdataformfirebase(view, fir, fin)
        }

    }

    private fun fetchdataformonth(view: View, fir: Int, fin: Int, month: Int, lastmo: Int) {
        linelist_1.clear()
        linelist_2.clear()
        barlist_1.clear()
        barlist_2.clear()
        entries.clear()
        var total_exp = 0
        var total_rev = 0

        var opw = 310012

        var opq = month.toInt()
        var opq1 = lastmo.toInt()

        var small = fin.toInt()
        var big = fir.toInt()
        val tasksCount = big - small + 1 // Number of tasks to complete
        var completedTasks = 0 // Counter for completed tasks
        val categoryMap = mutableMapOf<String, Float>()
        var sumd = 0
        var sumex = 0
        var i = 0
        var opw1 = (opq).toInt()
        var opw2 = (opq1).toInt()
        Log.d("month1","$opw1")
        Log.d("month1","$opq")
        Log.d("month1","$opq1")

        while (opw1 <= opw2) {

            fetchdataReference =
                FirebaseDatabase.getInstance().getReference().child("User").child(uid)
                    .child("monthly").child("$opw1").child("op1")
            fetchdataReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var ismonth = true
                    for (myds in snapshot.children) {
                        val myteo = myds.child("totalrevenue").value.toString()
                        val myexper = myds.child("totalexpenses").value.toString()
                        val mydatev = myds.child("monthvalue").value.toString()

                        total_exp += myexper.toInt()
                        total_rev += myteo.toInt()

                        text_rev.setText("$total_rev")
                        text_exp.setText("$total_exp")

                        var op = mydatev.substring(4, 8)
                        var month = mydatev.substring(0, 3)//month show in three words
                        if(month.isNotEmpty()){
                            ismonth = false
                        }

                        val subdate = op.toInt()
                        val subdate2 = (subdate + i + 1) - subdate
                        val sumd2 = myteo.toInt()
                        val myconvertdate2 = myexper.toInt()
                        val mymodl = ModelClassformainn(subdate2, sumd2)
                        val myjd = ModelClassformaiji(subdate2, myconvertdate2)
                        barlist_1.add(
                            BarEntry(
                                mymodl.datevalue.toFloat(),
                                mymodl.expenses.toFloat()
                            )
                        )
                        barlist_2.add(
                            BarEntry(
                                myjd.datevalue.toFloat(),
                                myjd.expenses.toFloat()
                            )
                        )
                        linelist_1.add(
                            Entry(
                                mymodl.datevalue.toFloat(),
                                mymodl.expenses.toFloat()
                            )
                        )
                        linelist_2.add(
                            Entry(
                                myjd.datevalue.toFloat(),
                                myjd.expenses.toFloat()
                            )
                        )
                        sumd = sumd2
                        sumex = myconvertdate2
                        i++
                    }
                    if(ismonth){
                        barlist_1.add(BarEntry(0f,0f))
                        barlist_2.add(BarEntry(0f,0f))
                    }

                    showlinechart(view, linelist_1, linelist_2)
                    showbarchart(view, barlist_1, barlist_2)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            opw1 += 1
        }
        while (small<=big){
            val pieReference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid)
                .child("pie1").child("$small").child("dateri")

            pieReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ipo in snapshot.children) {
                        val spinner = ipo.child("Spinner").getValue(String::class.java) ?: ""
                        val spinvalueStr =
                            ipo.child("entry2").getValue(String::class.java) ?: ""

                        if (spinvalueStr.isNotEmpty()) {
                            try {
                                val value = spinvalueStr.toFloat()
                                categoryMap[spinner] =
                                    categoryMap.getOrDefault(spinner, 0f) + value
                            } catch (e: NumberFormatException) {
                                Log.e(
                                    "NumberFormatError",
                                    "Failed to parse value: $spinvalueStr",
                                    e
                                )
                            }
                        }
                    }

                    // Increment completed tasks counter
                    completedTasks++

                    // If all tasks are completed, show the pie chart
                    if (completedTasks == tasksCount) {
                        showpiechart(view, categoryMap)
                        showpiereco(view, categoryMap)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Failed to fetch data", error.toException())
                }
            })
            small += 1

        }
    }

    private fun fetchdataformfirebase(view: View, fir: Int, fin: Int) {

        linelist_1.clear()
        linelist_2.clear()
        barlist_1.clear()
        barlist_2.clear()
        entries.clear()
        dately.clear()
        var opq = spinnershowq.text.toString()

        if (fir != 0 && fin != 0) {

            var total_exp = 0
            var total_rev = 0

            var small = fin.toInt()
            var big = fir.toInt()
            var sumd = 0
            var sumex = 0
            var i = 0
            val tasksCount = big - small + 1 // Number of tasks to complete
            var completedTasks = 0 // Counter for completed tasks
            val categoryMap = mutableMapOf<String, Float>()
            val categoryMap1 = mutableMapOf<String, Float>()


            while (small <= big) {
                fetchdataReference =
                    FirebaseDatabase.getInstance().getReference().child("User").child(uid)
                        .child("daily2").child("$small").child("date1")
                fetchdataReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var ismonth = true
                        for (myds in snapshot.children) {
                            val myteo = myds.child("entry").value.toString()
                            val myexper = myds.child("Expenses").value.toString()
                            val mydatev = myds.child("datevalue").value.toString()
                            val mydate = myds.child("mydateg").value.toString()

                            total_exp += myexper.toInt()
                            total_rev += myteo.toInt()

                            text_rev.setText("$total_rev")
                            text_exp.setText("$total_exp")

                            var op = mydatev.toInt()
                            var month = myteo.toString()//month show in three words
                            if (month.isNotEmpty()) {
                                ismonth = false
                            }

                            val subdate = op.toInt()
                            val subdate2 = (subdate + i + 1) - subdate
                            val sumd2 = myteo.toInt()
                            val myconvertdate2 = myexper.toInt()
                            val mymodl = ModelClassformainn(subdate2, sumd2)
                            val myjd = ModelClassformaiji(subdate2, myconvertdate2)

                            var oipu = mydate.substring(3, 5).toString()
                            var day = mydate.substring(0, 2).toString()
                            val monthName = when (oipu) {
                                "01" -> "Jan"
                                "02" -> "Feb"
                                "03" -> "Mar"
                                "04" -> "Apr"
                                "05" -> "May"
                                "06" -> "Jun"
                                "07" -> "Jul"
                                "08" -> "Aug"
                                "09" -> "Sep"
                                "10" -> "Oct"
                                "11" -> "Nov"
                                "12" -> "Dec"
                                else -> ""
                            }
                            if (monthName.isNotEmpty()) {
                                dately.add("$day $monthName")
                            }

                            barlist_1.add(
                                BarEntry(
                                    mymodl.datevalue.toFloat(),
                                    mymodl.expenses.toFloat()
                                )
                            )
                            barlist_2.add(
                                BarEntry(
                                    myjd.datevalue.toFloat(),
                                    myjd.expenses.toFloat()
                                )
                            )
                            linelist_1.add(
                                Entry(
                                    mymodl.datevalue.toFloat(),
                                    mymodl.expenses.toFloat()
                                )
                            )
                            linelist_2.add(
                                Entry(
                                    myjd.datevalue.toFloat(),
                                    myjd.expenses.toFloat()
                                )
                            )
                            sumd = sumd2
                            sumex = myconvertdate2
                            i++
                        }
                        if (opq == "Month") {
                            if (ismonth) {
                                barlist_1.add(BarEntry(0f, 0f))
                                barlist_2.add(BarEntry(0f, 0f))
                            }
                        }

                        showlinechart(view, linelist_1, linelist_2)
                        showbarchart(view, barlist_1, barlist_2)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                val pieReference = FirebaseDatabase.getInstance().getReference()
                    .child("User").child(uid)
                    .child("pie1").child("$small").child("dateri")

                pieReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ipo in snapshot.children) {
                            val spinner = ipo.child("Spinner").getValue(String::class.java) ?: ""
                            val spinvalueStr =
                                ipo.child("entry2").getValue(String::class.java) ?: ""

                            if (spinvalueStr.isNotEmpty()) {
                                try {
                                    val value = spinvalueStr.toFloat()
                                    categoryMap[spinner] =
                                        categoryMap.getOrDefault(spinner, 0f) + value
                                } catch (e: NumberFormatException) {
                                    Log.e(
                                        "NumberFormatError",
                                        "Failed to parse value: $spinvalueStr",
                                        e
                                    )
                                }
                            }
                        }

                        // Increment completed tasks counter
                        completedTasks++

                        // If all tasks are completed, show the pie chart
                        if (completedTasks == tasksCount) {
                            showpiechart(view, categoryMap)
                            showpiereco(view, categoryMap)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FirebaseError", "Failed to fetch data", error.toException())
                    }
                })
                small += 1
            }

        }
        else {
            var total_exp = 0
            var total_rev = 0

            var small = fin.toInt()
            var big = fir.toInt()
            val tasksCount = big - small + 1 // Number of tasks to complete
            var completedTasks = 0 // Counter for completed tasks
            val categoryMap = mutableMapOf<String, Float>()
            var sumd = 0
            var sumex = 0
            var i = 0

            fetchdataReference =
                FirebaseDatabase.getInstance().getReference().child("User").child(uid)
                    .child("daily2")
            fetchdataReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var ismonth = true
                    for (myds in snapshot.children) {
                        val myteo = myds.child("entry").value.toString()
                        val myexper = myds.child("Expenses").value.toString()
                        val mydatev = myds.child("datevalue").value.toString()
                        val mydate = myds.child("mydateg").value.toString()

                        total_exp += myexper.toInt()
                        total_rev += myteo.toInt()

                        text_rev.setText("$total_rev")
                        text_exp.setText("$total_exp")

                        var op = mydatev.toInt()
                        var month = mydatev.toString()//month show in three words
                        if(month.isNotEmpty()){
                            ismonth = false
                        }

                        val subdate = op.toInt()
                        val subdate2 = (subdate + i + 1) - subdate
                        val sumd2 = myteo.toInt()
                        val myconvertdate2 = myexper.toInt()
                        val mymodl = ModelClassformainn(subdate2, sumd2)
                        var oipu = mydate.substring(3,5).toString()
                        var day = mydate.substring(0,2).toString()
                        val myjd = ModelClassformaiji(subdate2, myconvertdate2)
                        val monthName = when (oipu) {
                            "01" -> "Jan"
                            "02" -> "Feb"
                            "03" -> "Mar"
                            "04" -> "Apr"
                            "05" -> "May"
                            "06" -> "Jun"
                            "07" -> "Jul"
                            "08" -> "Aug"
                            "09" -> "Sep"
                            "10" -> "Oct"
                            "11" -> "Nov"
                            "12" -> "Dec"
                            else -> ""
                        }
                        if (monthName.isNotEmpty()) {
                            dately.add("$day $monthName")
                        }
                        barlist_1.add(
                            BarEntry(
                                mymodl.datevalue.toFloat(),
                                mymodl.expenses.toFloat()
                            )
                        )
                        barlist_2.add(
                            BarEntry(
                                myjd.datevalue.toFloat(),
                                myjd.expenses.toFloat()
                            )
                        )
                        linelist_1.add(
                            Entry(
                                mymodl.datevalue.toFloat(),
                                mymodl.expenses.toFloat()
                            )
                        )
                        linelist_2.add(
                            Entry(
                                myjd.datevalue.toFloat(),
                                myjd.expenses.toFloat()
                            )
                        )
                        sumd = sumd2
                        sumex = myconvertdate2
                        i++
                    }
                    if(ismonth){
                        barlist_1.add(BarEntry(0f,0f))
                        barlist_2.add(BarEntry(0f,0f))
                    }

                    showlinechart(view, linelist_1, linelist_2)
                    showbarchart(view, barlist_1, barlist_2)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            val pieReference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid)
                .child("pie2")

            pieReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ipo in snapshot.children) {
                        val spinner = ipo.child("Spinner").getValue(String::class.java) ?: ""
                        val spinvalueStr = ipo.child("entry2").getValue(String::class.java) ?: ""

                        if (spinvalueStr.isNotEmpty()) {
                            try {
                                val value = spinvalueStr.toFloat()
                                categoryMap[spinner] =
                                    categoryMap.getOrDefault(spinner, 0f) + value
                            } catch (e: NumberFormatException) {
                                Log.e(
                                    "NumberFormatError",
                                    "Failed to parse value: $spinvalueStr",
                                    e
                                )
                            }
                        }
                    }

                    // Increment completed tasks counter
                    completedTasks++

                    // If all tasks are completed, show the pie chart
                    if (completedTasks == tasksCount) {
                        showpiechart(view, categoryMap)
                        showpiereco(view, categoryMap)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Failed to fetch data", error.toException())
                }
            })
        }
    }

    private fun showbarchart(view: View, barList1: ArrayList<BarEntry>, barList2: ArrayList<BarEntry>) {

        var op = spinnershowq.text.toString()
        val barChart = view.findViewById<BarChart>(R.id.barchart)

        // Define colors
        val incomeColor = Color.parseColor("#FF0000") // Red color
        val expenseColor = Color.parseColor("#0000FF") // Blue color

        val incomeDataSet = BarDataSet(barList1, "Income").apply {
            color = incomeColor
            valueTextColor = Color.WHITE
            valueTextSize = 10f
        }
        val expenseDataSet = BarDataSet(barList2, "Expense").apply {
            color = expenseColor
            valueTextColor = Color.WHITE
            valueTextSize = 10f
        }


        // Combine datasets into BarData
        val barData = BarData(incomeDataSet, expenseDataSet)
        barData.barWidth = 0.2f // Set the width of the bars

        // Set data to chart
        barChart.data = barData

        // Configure X-Axis
        val xAxis = barChart.xAxis

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        // Adjust X-Axis to fit the number of months
        val groupSpace = 0.4f // Space between groups
        val barSpace = 0.1f // Space between bars in a group
        xAxis.axisMinimum = -0.5f

        barChart.setVisibleXRangeMaximum(4F)


        // Group bars and set the width
        barChart.groupBars(0f, groupSpace, barSpace) // Adjust to fit full width starting from 0
        when (op) {
            "Year" -> {
                val months = arrayOf(
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                )
                xAxis.valueFormatter = IndexAxisValueFormatter(months)
                xAxis.axisMaximum = barData.getGroupWidth(0.4f, 0.1f) * months.size + 0.5f
            }
            "Month" -> {
                val days = Array(31) { (it + 1).toString().padStart(2, '0') }
                xAxis.valueFormatter = IndexAxisValueFormatter(days)
                xAxis.axisMaximum = barData.getGroupWidth(0.4f, 0.1f) * days.size + 0.5f
            }
            "Custom", "OverAll" -> {
                xAxis.valueFormatter = IndexAxisValueFormatter(dately)
                xAxis.axisMaximum = barData.getGroupWidth(0.4f, 0.1f) * dately.size + 0.5f
            }
            else -> {
                Log.w("ChartConfig", "Unsupported chart type: $op")
            }
        }

        // Configure the chart
        barChart.isDragEnabled = true
        barChart.setFitBars(true) // Make the chart fit the bars

        barChart.description.isEnabled = false
        barChart.legend.isEnabled = true

        // Set up auto zoom and fixed ratio
        barChart.setScaleEnabled(true)
        barChart.isScaleXEnabled = true
        barChart.isScaleYEnabled = true
        barChart.isHighlightPerTapEnabled = true
        barChart.isHighlightPerDragEnabled = true

        barChart.setDrawValueAboveBar(true)

        barChart.invalidate() // Refresh the chart
    }

    // perfect
    private fun showpiechart(view: View, fir: MutableMap<String, Float>) {
        piechart = view.findViewById(R.id.piechart)

        // Clear previous entries
        entries.clear()

        // Calculate the total value
        val totalValue = fir.values.sum()

        // Define the 8% threshold
        val threshold = totalValue * 0.06f

        // Sort entries by value in descending order
        val sortedEntries = fir.entries.sortedByDescending { it.value }

        // Find the largest value that meets the 8% threshold
        val opq = sortedEntries.find { it.value >= threshold }?.value ?: 0f

        // Take entries that are equal to or greater than this threshold
        val topEntries =
            sortedEntries.filter { it.value >= threshold }.map { PieEntry(it.value, it.key) }

        // Calculate total of remaining values
        val remainingTotal =
            sortedEntries.filter { it.value < threshold }.sumByDouble { it.value.toDouble() }
                .toFloat()

        // Add top entries to the list
        entries.addAll(topEntries)

        // Add the "Others" entry for remaining values if any
        if (remainingTotal > 0) {
            entries.add(PieEntry(remainingTotal, "Others"))
        }

        // Log entries for debugging
        Log.d("PieChartData", entries.toString())

        val pieDataSet: PieDataSet
        if (piechart.data != null && piechart.data.dataSetCount > 0) {
            pieDataSet = piechart.data.getDataSetByIndex(0) as PieDataSet
            pieDataSet.values = entries
            piechart.data.notifyDataChanged()
            piechart.notifyDataSetChanged()
        } else {
            pieDataSet = PieDataSet(entries, "Pie Data Set")
            pieDataSet.setColors(*ColorTemplate.VORDIPLOM_COLORS)
            pieDataSet.setDrawValues(true)
            pieDataSet.sliceSpace = 3f
            pieDataSet.iconsOffset = MPPointF(10f, 10f)
            pieDataSet.selectionShift = 10f

            val data = PieData(pieDataSet)
            piechart.data = data
            data.setValueTextSize(12f)
            data.setValueTextColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.red
                )
            )
        }
        // Update PieChart appearance
        piechart.invalidate() // Refreshes the chart
        piechart.description.isEnabled = false
        piechart.holeRadius = 0f
        piechart.setEntryLabelColor(ContextCompat.getColor(view.context, R.color.black))
        piechart.isDrawHoleEnabled = false


        // Add value selection listener
        piechart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val label = (e as? PieEntry)?.label ?: "Unknown"
                    val value = e.y
                    Toast.makeText(
                        view.context,
                        "Category: $label\nValue: $value",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected() {
                // Handle the event when no value is selected
            }
        })
    }
    // perfect
    private fun showpiereco(view: View, fir: MutableMap<String, Float>) {
        // Initialize list to hold graph data
        val listofdata: ArrayList<graph_reco_model> = ArrayList()

        // Convert the MutableMap<String, Float> to a list of graph_reco_model
        for ((category, value) in fir) {
            listofdata.add(graph_reco_model(category, value.toInt()))
        }

        // Sort the list by value in descending order
        listofdata.sortByDescending { it.value }

        // Initialize RecyclerView and Adapter // Make sure to replace `recyclerView` with the actual ID
        reco.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adaptt = graph_reco_adapter(listofdata)
        reco.adapter = adaptt
    }

    private fun showlinechart(view: View, incomeList: ArrayList<Entry>, expenseList: ArrayList<Entry>) {
        val lineChart = view.findViewById<LineChart>(R.id.linechart1)

        // Define colors
        val incomeColor = Color.parseColor("#FF0000") // Red for income
        val expenseColor = Color.parseColor("#0000FF") // Blue for expense

        // Create datasets
        val incomeDataSet = LineDataSet(incomeList, "Income").apply {
            color = incomeColor
            valueTextColor = incomeColor // Optional: set color for text values
        }
        val expenseDataSet = LineDataSet(expenseList, "Expense").apply {
            color = expenseColor
            valueTextColor = expenseColor // Optional: set color for text values
        }

        // Create line data
        val lineDataSets = ArrayList<ILineDataSet>().apply {
            add(incomeDataSet)
            add(expenseDataSet)
        }
        val lineData = LineData(lineDataSets)

        // Set data to chart
        lineChart.data = lineData
        lineChart.invalidate() // Refresh the chart
    }

    fun getColor(colorString: String): Int {
        return try {
            Color.parseColor(colorString)
        } catch (e: IllegalArgumentException) {
            // Handle the case where the color string is invalid
            Color.BLACK // Default to black if invalid
        }
    }
}
