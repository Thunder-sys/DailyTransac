package com.example.dailytransac.analysis

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.datafetch_yearlydata
import com.example.dailytransac.kuna.datafetch_yearlydata_adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class yearly : Fragment() {

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var dateTextView: TextView

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseRefrence : DatabaseReference

    lateinit var textView1: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView
    lateinit var textView4: TextView

    lateinit var reco1: RecyclerView

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var uid = firebaseAuth.currentUser?.uid!!

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_yearly, container, false)

        reco1 = view.findViewById(R.id.year_reco)

        dateTextView = view.findViewById(R.id.year_value)
        val incrementButton: ImageView = view.findViewById(R.id.yearly_greater_than)
        val decrementButton: ImageView = view.findViewById(R.id.yearly_less_than)

        textView1 = view.findViewById(R.id.month_income)
        textView2 = view.findViewById(R.id.month_expenses)
        textView3 = view.findViewById(R.id.month_saving)


        updateDateDisplay()

        dateTextView.setOnClickListener(){
            yearPicker(calendar)
        }

        incrementButton.setOnClickListener {
            calendar.add(Calendar.YEAR, 1)
            updateDateDisplay()
        }

        decrementButton.setOnClickListener {
            calendar.add(Calendar.YEAR, -1)
            updateDateDisplay()
        }


        return view
    }

    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time)

        textView1.text = ""
        textView2.text = ""
        textView3.text = ""

        dateTextView.text = "$dateString"

        fetchdata(dateString)

    }
    private fun yearPicker(calendar: Calendar) {

        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.year_picker)
        val numberPicker = dialog.findViewById<NumberPicker>(R.id.yearpicker)
        numberPicker.minValue = 2000
        numberPicker.maxValue = 2100
        numberPicker.wrapSelectorWheel = true

        numberPicker.value = calendar.get(Calendar.YEAR)

        var cancel: TextView = dialog.findViewById(R.id.year_cancel)
        var ok: TextView = dialog.findViewById(R.id.year_ok)

        ok.setOnClickListener {
            dateTextView.text = numberPicker.value.toString()
            calendar.set(Calendar.YEAR, numberPicker.value)
            dialog.dismiss()
            updateDateDisplay()
        }
        cancel.setOnClickListener(){
            dialog.dismiss()
        }

        dialog.show()

    }


    private fun fetchdata(dateString: String) {

        var listofmonth1 = ArrayList<datafetch_yearlydata>()
        reco1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var adap = datafetch_yearlydata_adapter(listofmonth1)
        reco1.adapter = adap

        var year = 2100 - dateString.toInt()
        Log.d("mio","$year")

        firebaseRefrence = firebaseDatabase.getReference().child("User").child(uid).child("year").child("$year").child("month1")

        firebaseRefrence.addListenerForSingleValueEvent(object : ValueEventListener {
            var value1 = 0
            var value2 = 0
            var value3 = 0
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ip in snapshot.children){
                    var expenses = ip.child("totalexpenses").getValue().toString()
                    var revenue = ip.child("totalrevenue").getValue().toString()
                    var saving = ip.child("totalsaving").getValue().toString()
                    var mydate = ip.child("currentmonth").getValue().toString()

                    value1+=revenue.toInt()
                    value2+=expenses.toInt()
                    value3+=saving.toInt()

                    Log.d("mio","$expenses")
                    Log.d("mio","$revenue")
                    Log.d("mio","$saving")
                    Log.d("mio","$mydate")

                    var curent = mydate.substring(0,3).toString()
                    Log.d("mio","$curent")

                    listofmonth1.add(datafetch_yearlydata("$curent","$revenue","$expenses","$saving"))

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