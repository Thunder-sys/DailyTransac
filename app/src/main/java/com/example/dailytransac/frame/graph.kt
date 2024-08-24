package com.example.dailytransac.frame

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.dailytransac.Database.ModelClassformaiji
import com.example.dailytransac.Database.ModelClassformainn
import com.example.dailytransac.R
import com.example.dailytransac.databinding.FragmentGraphBinding
import com.example.dailytransac.kuna.graph_spinner_adapter
import com.example.dailytransac.kuna.graph_spinner_model
import com.github.mikephil.charting.animation.Easing
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
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.transition.MaterialSharedAxis.Axis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.log

class graph : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid: String = firebaseAuth.currentUser?.uid ?: ""
    private lateinit var handler: Handler
    private lateinit var updateTimeRunnable: Runnable

    private lateinit var spinnerReference: DatabaseReference
    private lateinit var pieReference: DatabaseReference
    private lateinit var fetchdataReference: DatabaseReference
    private lateinit var showSpinner: TextView
    private lateinit var listOfMonth: ArrayList<graph_spinner_model>
    private lateinit var adapter: graph_spinner_adapter
    private lateinit var barchart: BarChart
    private lateinit var linechart: LineChart
    private lateinit var linechart2: LineChart
    private lateinit var piechart: PieChart


    val entries = mutableListOf<PieEntry>()
    val categoryMap = mutableMapOf<String, Float>()
    var barlist_1 = ArrayList<BarEntry>()
    var barlist_2 = ArrayList<BarEntry>()
    var linelist_1=  ArrayList<Entry>()
    var linelist_2=  ArrayList<Entry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)
        showSpinner = view.findViewById(R.id.shorspinner)
        showSpinner.setOnClickListener {
            showdateslector(view)
        }

        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                fetchfulldataValueset(view)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)

        spinnerReference = FirebaseDatabase.getInstance().getReference()
            .child("User").child(uid).child("yearspinner")

        return view
    }

    private fun showdateslector(view: View) {
        var dailog = Dialog(requireContext())
        dailog.setContentView(R.layout.graph_date_selector)
        dailog.show()

        var yearstart: TextView = dailog.findViewById(R.id.graph_yearstart)
        var yearend: TextView = dailog.findViewById(R.id.graph_yearend)
        var monthstart: TextView = dailog.findViewById(R.id.graph_monthstart)
        var monthend: TextView = dailog.findViewById(R.id.graph_monthend)
        var datestart: TextView = dailog.findViewById(R.id.graph_datestart)
        var datteend: TextView = dailog.findViewById(R.id.graph_dateend)

        var cancel: TextView = dailog.findViewById(R.id.graph_cancel)
        var ok: TextView = dailog.findViewById(R.id.graph_ok)

        yearstart.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.graph_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
            val searchView: androidx.appcompat.widget.SearchView =
                dialog.findViewById(R.id.graph_searchView)

            listOfMonth = ArrayList()
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = graph_spinner_adapter(listOfMonth) { dattaspinner ->
                yearstart.setText(dattaspinner.text)
                dialog.dismiss()
            }
            recyclerView.adapter = adapter

            searchView.clearFocus()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
                        val yearSpinner = ip.child("yearspinner").getValue(String::class.java) ?: ""
                        listOfMonth.add(graph_spinner_model("All"))
                        listOfMonth.add(graph_spinner_model(yearSpinner))
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })

            dialog.show()

        }
        yearend.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.graph_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
            val searchView: androidx.appcompat.widget.SearchView =
                dialog.findViewById(R.id.graph_searchView)

            listOfMonth = ArrayList()
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = graph_spinner_adapter(listOfMonth) { dattaspinner ->
                yearend.setText(dattaspinner.text)
                dialog.dismiss()
            }
            recyclerView.adapter = adapter


            searchView.clearFocus()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
                        val yearSpinner = ip.child("yearspinner").getValue(String::class.java) ?: ""
                        listOfMonth.add(graph_spinner_model("All"))
                        listOfMonth.add(graph_spinner_model(yearSpinner))
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })

            dialog.show()

        }
        monthstart.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.graph_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
            val searchView: androidx.appcompat.widget.SearchView =
                dialog.findViewById(R.id.graph_searchView)

            listOfMonth = ArrayList()
            listOfMonth.add(graph_spinner_model("All"))
            listOfMonth.add(graph_spinner_model("January"))
            listOfMonth.add(graph_spinner_model("Febrary"))
            listOfMonth.add(graph_spinner_model("March"))
            listOfMonth.add(graph_spinner_model("April"))
            listOfMonth.add(graph_spinner_model("May"))
            listOfMonth.add(graph_spinner_model("June"))
            listOfMonth.add(graph_spinner_model("July"))
            listOfMonth.add(graph_spinner_model("August"))
            listOfMonth.add(graph_spinner_model("September"))
            listOfMonth.add(graph_spinner_model("October"))
            listOfMonth.add(graph_spinner_model("November"))
            listOfMonth.add(graph_spinner_model("December"))
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = graph_spinner_adapter(listOfMonth) { dattaspinner ->
                monthstart.setText(dattaspinner.text)
                dialog.dismiss()
            }
            recyclerView.adapter = adapter

            searchView.clearFocus()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }
            })

            dialog.show()

        }
        monthend.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.graph_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
            val searchView: androidx.appcompat.widget.SearchView =
                dialog.findViewById(R.id.graph_searchView)

            listOfMonth = ArrayList()
            listOfMonth.add(graph_spinner_model("All"))
            listOfMonth.add(graph_spinner_model("January"))
            listOfMonth.add(graph_spinner_model("Febrary"))
            listOfMonth.add(graph_spinner_model("March"))
            listOfMonth.add(graph_spinner_model("April"))
            listOfMonth.add(graph_spinner_model("May"))
            listOfMonth.add(graph_spinner_model("June"))
            listOfMonth.add(graph_spinner_model("July"))
            listOfMonth.add(graph_spinner_model("August"))
            listOfMonth.add(graph_spinner_model("September"))
            listOfMonth.add(graph_spinner_model("October"))
            listOfMonth.add(graph_spinner_model("November"))
            listOfMonth.add(graph_spinner_model("December"))
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = graph_spinner_adapter(listOfMonth) { dattaspinner ->
                monthend.setText(dattaspinner.text)
                dialog.dismiss()
            }
            recyclerView.adapter = adapter

            searchView.clearFocus()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }
            })

            dialog.show()

        }
        datestart.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.graph_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
            val searchView: androidx.appcompat.widget.SearchView =
                dialog.findViewById(R.id.graph_searchView)

            listOfMonth = ArrayList()
            listOfMonth.add(graph_spinner_model("All"))
            listOfMonth.add(graph_spinner_model("01"))
            listOfMonth.add(graph_spinner_model("02"))
            listOfMonth.add(graph_spinner_model("03"))
            listOfMonth.add(graph_spinner_model("04"))
            listOfMonth.add(graph_spinner_model("05"))
            listOfMonth.add(graph_spinner_model("06"))
            listOfMonth.add(graph_spinner_model("07"))
            listOfMonth.add(graph_spinner_model("08"))
            listOfMonth.add(graph_spinner_model("09"))
            listOfMonth.add(graph_spinner_model("10"))
            listOfMonth.add(graph_spinner_model("11"))
            listOfMonth.add(graph_spinner_model("12"))
            listOfMonth.add(graph_spinner_model("13"))
            listOfMonth.add(graph_spinner_model("14"))
            listOfMonth.add(graph_spinner_model("15"))
            listOfMonth.add(graph_spinner_model("16"))
            listOfMonth.add(graph_spinner_model("17"))
            listOfMonth.add(graph_spinner_model("18"))
            listOfMonth.add(graph_spinner_model("19"))
            listOfMonth.add(graph_spinner_model("20"))
            listOfMonth.add(graph_spinner_model("21"))
            listOfMonth.add(graph_spinner_model("22"))
            listOfMonth.add(graph_spinner_model("23"))
            listOfMonth.add(graph_spinner_model("24"))
            listOfMonth.add(graph_spinner_model("25"))
            listOfMonth.add(graph_spinner_model("26"))
            listOfMonth.add(graph_spinner_model("27"))
            listOfMonth.add(graph_spinner_model("28"))
            listOfMonth.add(graph_spinner_model("29"))
            listOfMonth.add(graph_spinner_model("30"))
            listOfMonth.add(graph_spinner_model("31"))
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = graph_spinner_adapter(listOfMonth) { dattaspinner ->
                datestart.setText(dattaspinner.text)
                dialog.dismiss()
            }
            recyclerView.adapter = adapter

            searchView.clearFocus()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }
            })
            dialog.show()

        }
        datteend.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.graph_spinner_show)

            val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
            val searchView: androidx.appcompat.widget.SearchView =
                dialog.findViewById(R.id.graph_searchView)

            listOfMonth = ArrayList()
            listOfMonth.add(graph_spinner_model("All"))
            listOfMonth.add(graph_spinner_model("01"))
            listOfMonth.add(graph_spinner_model("02"))
            listOfMonth.add(graph_spinner_model("03"))
            listOfMonth.add(graph_spinner_model("04"))
            listOfMonth.add(graph_spinner_model("05"))
            listOfMonth.add(graph_spinner_model("06"))
            listOfMonth.add(graph_spinner_model("07"))
            listOfMonth.add(graph_spinner_model("08"))
            listOfMonth.add(graph_spinner_model("09"))
            listOfMonth.add(graph_spinner_model("10"))
            listOfMonth.add(graph_spinner_model("11"))
            listOfMonth.add(graph_spinner_model("12"))
            listOfMonth.add(graph_spinner_model("13"))
            listOfMonth.add(graph_spinner_model("14"))
            listOfMonth.add(graph_spinner_model("15"))
            listOfMonth.add(graph_spinner_model("16"))
            listOfMonth.add(graph_spinner_model("17"))
            listOfMonth.add(graph_spinner_model("18"))
            listOfMonth.add(graph_spinner_model("19"))
            listOfMonth.add(graph_spinner_model("20"))
            listOfMonth.add(graph_spinner_model("21"))
            listOfMonth.add(graph_spinner_model("22"))
            listOfMonth.add(graph_spinner_model("23"))
            listOfMonth.add(graph_spinner_model("24"))
            listOfMonth.add(graph_spinner_model("25"))
            listOfMonth.add(graph_spinner_model("26"))
            listOfMonth.add(graph_spinner_model("27"))
            listOfMonth.add(graph_spinner_model("28"))
            listOfMonth.add(graph_spinner_model("29"))
            listOfMonth.add(graph_spinner_model("30"))
            listOfMonth.add(graph_spinner_model("31"))
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = graph_spinner_adapter(listOfMonth) { dattaspinner ->
                datteend.setText(dattaspinner.text)
                dialog.dismiss()
            }
            recyclerView.adapter = adapter

            searchView.clearFocus()
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }
            })

            dialog.show()

        }

        cancel.setOnClickListener() {
            dailog.dismiss()
        }
        ok.setOnClickListener() {
            var  yearst = yearstart.text.toString()
            var  yearen = yearend.text.toString()
            var  monthst = monthstart.text.toString()
            var  monthen = monthend.text.toString()
            var  datest = datestart.text.toString()
            var  dateen = datteend.text.toString()
            var op1 = ""
            var op2 = ""
            when(monthst){
                "All" -> op1="All"
                "January" -> op1="01"
                "Febrary" -> op1="02"
                "March" -> op1="03"
                "April" -> op1="04"
                "May" -> op1="05"
                "June" -> op1="06"
                "July" -> op1="07"
                "August" -> op1="08"
                "September" -> op1="09"
                "October" -> op1="10"
                "November" -> op1="11"
                "December" -> op1="12"
            }
            when(monthen){
                "All" -> op2="All"
                "January" -> op2="01"
                "Febrary" -> op2="02"
                "March" -> op2="03"
                "April" -> op2="04"
                "May" -> op2="05"
                "June" -> op2="06"
                "July" -> op2="07"
                "August" -> op2="08"
                "September" -> op2="09"
                "October" -> op2="10"
                "November" -> op2="11"
                "December" -> op2="12"
            }

            if (yearst == "All" && yearen == "All") {
                showSpinner.setText("OverAll")
                dailog.dismiss()
                fetchfulldataValueset(view)
            }
            else {
                if (monthst == "All" && monthen == "All") {
                    showSpinner.setText("$yearst to $yearen")
                    dailog.dismiss()
                    fetchfulldataValueset(view)
                } else {
                    if (datest == "All" && dateen == "All") {
                        showSpinner.setText("$op1/$yearst to $op2/$yearen")
                        dailog.dismiss()
                        fetchfulldataValueset(view)
                    }
                    else {
                        showSpinner.setText("$datest/$op1/$yearst to $dateen/$op2/$yearen")
                        dailog.dismiss()
                        fetchfulldataValueset(view)
                    }
                }
            }
        }
    }

    private fun fetchfulldataValueset(view:View){
        var show = showSpinner.text.toString()
        val formatte1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val finalDate = "30/12/2100"
        val initiadate = LocalDate.parse(finalDate, formatte1)
        val main = initiadate.toEpochDay().toInt()

        if (show.length == 12){
            var first = show.substring(0,4)
            var end = show.substring(8,12)
            var inaidate = "01/01/$first"
            var final = "31/12/$end"

            val initiadate1 = LocalDate.parse(inaidate, formatte1)
            val dfirst = initiadate1.toEpochDay().toInt()
            val initiadat2 = LocalDate.parse(final, formatte1)
            val dend = initiadat2.toEpochDay().toInt()

            var fin = dfirst
            var fir = dend

            Log.d("miop","$fir")
            Log.d("miop","$fin")

            fetchdataformfirebase(view,fir,fin)
        }
        else{
            if (show.length == 18){
                var first = show.substring(0,7)
                var end = show.substring(11,18)
                var inaidate = "01/$first"
                var final = "31/$end"

                val initiadate1 = LocalDate.parse(inaidate, formatte1)
                val dfirst = initiadate1.toEpochDay().toInt()
                val initiadat2 = LocalDate.parse(final, formatte1)
                val dend = initiadat2.toEpochDay().toInt()

                var fin = dfirst
                var fir = dend
                Log.d("miop","$fir")
                Log.d("miop","$fin")
                fetchdataformfirebase(view,fir,fin)
            }
            else{
                if (show.length == 24){
                    var first = show.substring(0,10)
                    var end = show.substring(14,24)
                    var inaidate = "$first"
                    var final = "$end"

                    val initiadate1 = LocalDate.parse(inaidate, formatte1)
                    val dfirst = initiadate1.toEpochDay().toInt()
                    val initiadat2 = LocalDate.parse(final, formatte1)
                    val dend = initiadat2.toEpochDay().toInt()

                    var fin = dfirst
                    var fir = dend
                    Log.d("miop","$fir")
                    Log.d("miop","$fin")
                    fetchdataformfirebase(view,fir,fin)
                }
                else{
                    var fin =0
                    var fir =0
                    fetchdataformfirebase(view,fir,fin)
                }
            }
        }
    }

    private fun fetchdataformfirebase(view:View,fir:Int,fin:Int){

        linelist_1.clear()
        linelist_2.clear()
        barlist_1.clear()
        barlist_2.clear()

        if (fir != 0 && fin !=0){
            val categoryMap = mutableMapOf<String, Float>()
            var small = fin.toInt()
            var big = fir.toInt()
            var sumd = 0
            var sumex = 0
            var i = 0
            val tasksCount = big - small + 1 // Number of tasks to complete
            var completedTasks = 0 // Counter for completed tasks



            while (small <= big) {
                fetchdataReference =
                    FirebaseDatabase.getInstance().getReference().child("User").child(uid)
                        .child("daily2").child("$small").child("date1")
                fetchdataReference.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (myds in snapshot.children) {
                            for (myds in snapshot.children) {
                                val myteo = myds.child("entry").value.toString()
                                val myexper = myds.child("Expenses").value.toString()
                                val mydatege = myds.child("mydateg").value.toString()
                                val mydatev = myds.child("datevalue").value.toString()
                                Log.d("mud", mydatege)
                                val subdate = mydatev.toInt()
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
                            showlinechart(view, linelist_1, linelist_2)
                            shorbarchart(view, barlist_1, barlist_2)
                        }
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
                            val spinvalueStr = ipo.child("entry2").getValue(String::class.java) ?: ""

                            if (spinvalueStr.isNotEmpty()) {
                                try {
                                    val value = spinvalueStr.toFloat()
                                    categoryMap[spinner] =
                                        categoryMap.getOrDefault(spinner, 0f) + value
                                } catch (e: NumberFormatException) {
                                    Log.e("NumberFormatError", "Failed to parse value: $spinvalueStr", e)
                                }
                            }
                        }

                        // Increment completed tasks counter
                        completedTasks++

                        // If all tasks are completed, show the pie chart
                        if (completedTasks == tasksCount) {
                            showpiechart(view, categoryMap)
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

            var small = fin.toInt()
            var big = fir.toInt()
            var sumd = 0
            var sumex = 0
            var i = 0

            fetchdataReference =
                FirebaseDatabase.getInstance().getReference().child("User").child(uid)
                    .child("daily2")
            fetchdataReference.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (myds in snapshot.children) {
                        val myteo = myds.child("entry").value.toString()
                        val myexper = myds.child("Expenses").value.toString()
                        val mydatege = myds.child("mydateg").value.toString()
                        val mydatev = myds.child("datevalue").value.toString()
                        Log.d("mud", mydatege)
                        val subdate = mydatev.toInt()
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
                    showlinechart(view, linelist_1, linelist_2)
                    shorbarchart(view, barlist_1, barlist_2)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun showpiechart(view: View, fir: MutableMap<String, Float>) {
        piechart = view.findViewById(R.id.piechart)
        for ((category, totalValue) in fir) {
            entries.add(PieEntry(totalValue, category))
        }

        // Log entries for debugging
        Log.d("PieChartData", entries.toString())

        val piedataset: PieDataSet
        if (piechart.data != null && piechart.data.dataSetCount > 0) {
            piedataset = piechart.data.getDataSetByIndex(0) as PieDataSet
            piedataset.values = entries
            piechart.data.notifyDataChanged()
            piechart.notifyDataSetChanged()
        } else {
            piedataset = PieDataSet(entries, "Pie Data Set")
            piedataset.setColors(*ColorTemplate.VORDIPLOM_COLORS)
            piedataset.setDrawValues(true)
            piedataset.sliceSpace = 3f
            piedataset.iconsOffset = MPPointF(10f, 10f)
            piedataset.selectionShift = 10f

            val data = PieData(piedataset)
            piechart.data = data
            piechart.invalidate()
            piechart.description.isEnabled = false
            data.setValueTextSize(12f)
            data.setValueTextColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.red
                )
            )

            piechart.holeRadius = 0f
            piechart.setEntryLabelColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.black
                )
            )
            piechart.isDrawHoleEnabled = false
        }
    }

    private fun showlinechart(view: View,linelist_1:ArrayList<Entry>,linelist_2: ArrayList<Entry>) {
        linechart = view.findViewById(R.id.linechart1)
        linechart2 = view.findViewById(R.id.linechart2)


        val validColor = getColor("FF0000") // Tomato color
        val invalidColor = getColor("#03CFAD") // Defaults to black
        val barDataset = LineDataSet(linelist_1, "Income")
        barDataset.color = validColor
        val barDataset1 = LineDataSet(linelist_2, "Expense")
        barDataset1.color = invalidColor

        var datset = ArrayList<ILineDataSet>()
        datset.add(barDataset)
        datset.add(barDataset1)

        var data = LineData(datset)
        linechart.data = data
        linechart.invalidate()

    }

    private fun shorbarchart(view: View,barlist_1:ArrayList<BarEntry>,barlist_2: ArrayList<BarEntry>) {
        barchart = view.findViewById(R.id.barchart)

        val validColor = getColor("FF0000") // Tomato color
        val invalidColor = getColor("#03CFAD") // Defaults to black

        val barDataset = BarDataSet(barlist_1, "Income")
        barDataset.color = validColor
        val barDataset1 = BarDataSet(barlist_2, "Expense")
        barDataset1.color = invalidColor

        var months = arrayOf("First month", "Second month")

        var bardata = BarData(barDataset, barDataset1)
        barchart.data = bardata

        val xAxis = barchart.xAxis

        xAxis.setValueFormatter(IndexAxisValueFormatter(months))
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity
        xAxis.isGranularityEnabled = true

        var barSpace = 0.1f
        var grop_space = 0.5f
        var grouCount = 6
        barchart.isDragEnabled = true
        bardata.barWidth = 0.15f
        barchart.xAxis.axisMinimum = 0f
        barchart.xAxis.axisMaximum =
            0 + barchart.barData.getGroupWidth(grop_space, barSpace) * grouCount
        barchart.groupBars(0f, grop_space, barSpace)
        barchart.invalidate()
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

    fun getColor(colorString: String): Int {
        return try {
            Color.parseColor(colorString)
        } catch (e: IllegalArgumentException) {
            // Handle the case where the color string is invalid
            Color.BLACK // Default to black if invalid
        }
    }
}
