package com.example.dailytransac.transaction

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

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseRefrence : DatabaseReference

    lateinit var textView1: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView

    lateinit var reco1:RecyclerView

    var firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    var uid = firebaseAuth.currentUser?.uid!!

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_monthly, container, false)

        reco1 = view.findViewById(R.id.month_reco)


        dateTextView = view.findViewById(R.id.monthly_value)

        val incrementButton: ImageView = view.findViewById(R.id.monthly_greater_than)
        val decrementButton: ImageView = view.findViewById(R.id.monthly_less_than)

        textView1 = view.findViewById(R.id.daily_income)
        textView2 = view.findViewById(R.id.daily_expenses)
        textView3 = view.findViewById(R.id.daily_saving)

        dateTextView.setOnClickListener(){
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


    private fun updateDateDisplay(view: View) {
        val dateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dateFormat1 = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time).toString()
        val dateString1 = dateFormat1.format(calendar.time).toString()

        var monthvalue = ""
        when("$dateString"){
            "01" ->monthvalue = "Jan"
            "02" ->monthvalue = "Feb"
            "03" ->monthvalue = "Mar"
            "04" ->monthvalue = "Apr"
            "05" ->monthvalue = "May"
            "06" ->monthvalue = "Jun"
            "07" ->monthvalue = "Jul"
            "08" ->monthvalue = "Aug"
            "09" ->monthvalue = "Sep"
            "10" ->monthvalue = "Oct"
            "11" ->monthvalue = "Nov"
            "12" ->monthvalue = "Dec"
        }
        textView1.text = ""
        textView2.text = ""
        textView3.text = ""

        dateTextView.text = "$monthvalue "+"$dateString1"

        fetchdata(view,dateString,dateString1)


    }

    private fun fetchdata(view:View,dateString: String, dateString1: String) {

        var listofmonth1 = ArrayList<datafetch_montlydata>()
        reco1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var adap = datafetch_montlydata_adapter(listofmonth1)
        reco1.adapter = adap

        var year = 2100 - dateString1.toInt()
        var month = 13 - dateString.toInt()
        Log.d("mio","$year")
        Log.d("mio","$month")

        firebaseRefrence = firebaseDatabase.getReference().child("User").child(uid).child("year").child("$year").child("month").child("$month").child("date")

        firebaseRefrence.addListenerForSingleValueEvent(object :ValueEventListener{
            var value1 = 0
            var value2 = 0
            var value3 = 0
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ip in snapshot.children){
                    var expenses = ip.child("Expenses").getValue().toString()
                    var revenue = ip.child("entry").getValue().toString()
                    var saving = ip.child("income").getValue().toString()
                    var mydate = ip.child("mydateg").getValue().toString()

                    value1+=revenue.toInt()
                    value2+=expenses.toInt()
                    value3+=saving.toInt()

                    Log.d("mio","$expenses")
                    Log.d("mio","$revenue")
                    Log.d("mio","$saving")
                    Log.d("mio","$mydate")

                    var curent = mydate.substring(0,2).toString()
                    Log.d("mio","$curent")

                    listofmonth1.add(datafetch_montlydata("$curent","$revenue","$expenses","$saving"))

                    textView1.text = "$value1"
                    textView2.text = "$value2"
                    textView3.text = "$value3"

                }
                adap.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "There Are some error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}