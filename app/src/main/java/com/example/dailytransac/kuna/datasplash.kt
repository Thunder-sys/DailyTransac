package com.example.dailytransac.kuna

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.Database.monthly_data
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.Adapter_monthly
import com.example.dailytransac.Saksh.Mainpage
import com.example.dailytransac.Saksh.Model_monthly
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

class datasplash : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseRefrence: DatabaseReference
    private lateinit var firebaseRefrence1: DatabaseReference
    private lateinit var firebase_for_month: DatabaseReference
    private var splashscreen=1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datasplash)

        var bundle: Bundle? = intent.extras
        var uid = bundle?.getString("uid") ?: ""
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseRefrence = firebaseDatabase.getReference().child("User").child(uid).child("year")
        firebase_for_month = firebaseDatabase.getReference("User").child(uid).child("data")

        var listofmonth = ArrayList<Model_monthly>()
        var adap = Adapter_monthly(listofmonth)

        firebase_for_month.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (mypep in snapshot.children) {
                    var date = mypep.child("mydateg").getValue().toString()
                    var short_year = date.substring(6, 10)
                    firebaseRefrence1 = firebaseDatabase.getReference().child("User").child(uid).child("year").child("$short_year").child("month")

                    firebaseRefrence1.addListenerForSingleValueEvent(object : ValueEventListener{
                        var nullop = ""
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(opmine in snapshot.children){
                                var month = opmine.child("currentmonth").getValue().toString()
                                var monthop = 13 - month.toInt()
                                when(month){
                                    "01" -> nullop = "January"
                                    "02" -> nullop = "Febrary"
                                    "03" -> nullop = "March"
                                    "04" -> nullop = "April"
                                    "05" -> nullop = "May"
                                    "06" -> nullop = "June"
                                    "07" -> nullop = "July"
                                    "08" -> nullop = "August"
                                    "09" -> nullop = "September"
                                    "10" -> nullop = "October"
                                    "11" -> nullop = "November"
                                    "12" -> nullop = "December"
                                }
                                firebaseRefrence.child("$short_year").child("spinnermonth").child("month$monthop").child("monthly").setValue(nullop)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        firebase_for_month.addListenerForSingleValueEvent(object : ValueEventListener {

            var totalEntry = 0
            var totalExpenses = 0

            override fun onDataChange(snapshot: DataSnapshot) {
                for (mype in snapshot.children) {
                    var date = mype.child("mydateg").getValue().toString()
                    var short_date = date.substring(3, 5)
                    var short_month = 13 - short_date.toInt()
                    var short_year = date.substring(6, 10)
                    var entry = mype.child("entry").getValue().toString()
                    var expenses = mype.child("Expenses").getValue().toString()

                    totalEntry+=entry.toInt()
                    totalExpenses+=expenses.toInt()

                    var child_month_data = monthly_data("$totalEntry","$totalExpenses",short_date)
                    firebaseRefrence.child("$short_year").child("month").child("$short_month").setValue(child_month_data)
                    val allstruu: MutableMap<String, Any> = HashMap()
                    allstruu["yearop"] = short_year
                    firebaseRefrence.child("$short_year").updateChildren(allstruu)
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
                Toast.makeText(this@datasplash, "There Are some error", Toast.LENGTH_SHORT).show()
            }
        })
        Handler().postDelayed({
            var dhh = Intent(this, Mainpage::class.java)
            dhh.putExtra("uid1","$uid")
            startActivity(dhh)
            finish()
        },splashscreen.toLong())
    }
}