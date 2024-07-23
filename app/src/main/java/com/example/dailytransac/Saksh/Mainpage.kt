import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.Adapter_daily
import com.example.dailytransac.Saksh.Adapter_mainpage
import com.example.dailytransac.Saksh.Adapter_monthly
import com.example.dailytransac.Saksh.Model_daily
import com.example.dailytransac.Saksh.Model_mainpage
import com.example.dailytransac.Saksh.Model_monthly
import com.example.dailytransac.Saksh.Model_reco
import com.example.dailytransac.khush.Budgetscreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Mainpage : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebase_for_daily: DatabaseReference
    private lateinit var firebase_for_month: DatabaseReference
    private lateinit var firebase_for_dailyfull_data: DatabaseReference

    private lateinit var reco1: RecyclerView
    private lateinit var reco2: RecyclerView
    private lateinit var reco3: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)
        var bundle: Bundle? = intent.extras
        var uid = bundle?.getString("uid") ?: ""

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebase_for_daily = firebaseDatabase.getReference("User").child(uid).child("data")
        firebase_for_month = firebaseDatabase.getReference("User").child(uid).child("monthly_data")

        reco1 = findViewById(R.id.recy1)
        reco2 = findViewById(R.id.recy2)
        reco3 = findViewById(R.id.recy)

        var grap = findViewById<ImageButton>(R.id.imageButton)
        grap.setOnClickListener() {
            var intent = Intent(this, Budgetscreen::class.java)
            startActivity(intent)
        }

        val myman = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reco1.layoutManager = myman
        val mythisn = ArrayList<Model_daily>()
        val myadap = Adapter_daily(mythisn)
        reco1.adapter = myadap

        var listofdata: ArrayList<Model_mainpage> = ArrayList()
        reco3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var adaptt = Adapter_mainpage(listofdata)
        reco3.adapter = adaptt

        firebase_for_daily.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (myds in dataSnapshot.children) {
                    var entry = myds.child("entry").getValue().toString()
                    var Expenses = myds.child("Expenses").getValue().toString()
                    var income = myds.child("income").getValue().toString()
                    var mydateg = myds.child("mydateg").getValue().toString()
                    var datevalue = myds.child("datevalue").getValue().toString()
                    var dateVlaue = datevalue.toInt()
                    mythisn.add(Model_daily(mydateg, entry, Expenses, income))

                    var childitem = ArrayList<Model_reco>()
                    firebase_for_dailyfull_data =
                        firebaseDatabase.getReference("User").child(uid).child("data")
                            .child("$dateVlaue").child("dateri")

                    firebase_for_dailyfull_data.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (mydata in datasnapshot.children) {
                                var entry2 = mydata.child("entry2").getValue().toString()
                                var work = mydata.child("work").getValue().toString()
                                var spinner = mydata.child("Spinner").getValue().toString()
                                childitem.add(Model_reco(entry2, work, spinner))
                            }
                            adaptt.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@Mainpage,
                                "There Are some error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                    listofdata.add(Model_mainpage(mydateg, entry, Expenses, income, childitem))
                }
                myadap.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Mainpage, "There Are some error", Toast.LENGTH_SHORT).show()
            }
        })

        var listofmonth = ArrayList<Model_monthly>()
        reco2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var adap = Adapter_monthly(listofmonth)
        reco2.adapter = adap

        firebase_for_month.addListenerForSingleValueEvent(object : ValueEventListener {
            var monthlyDataMap = HashMap<String, Model_monthly>()
            var totalEntry = 0

            override fun onDataChange(snapshot: DataSnapshot) {
                for (mype in snapshot.children) {
                    var date = mype.child("mydateg").getValue().toString()
                    var short_date = date.substring(3, 5)
                    var entry = mype.child("entry").getValue().toString().toInt()
                    var expenses = mype.child("Expenses").getValue().toString().toInt()

                    if (monthlyDataMap.containsKey(short_date)) {
                        val existingMonthData = monthlyDataMap[short_date]!!
                        existingMonthData.totalEntry += entry
                        existingMonthData.totalExpenses += expenses
                    } else {
                        monthlyDataMap[short_date] = Model_monthly(short_date, "$entry", "$expenses")
                    }
                }

                listofmonth.addAll(monthlyDataMap.values)
                adap.notifyDataSetChanged()

                // Save monthly data to Firebase
                for ((key, value) in monthlyDataMap) {
                    val monthNode = firebase_for_month.child("User").child(uid).child("manthdata")
                    monthNode.child("mydateg").setValue(value.monthName)
                    monthNode.child("entry").setValue(value.totalEntry)
                    monthNode.child("Expenses").setValue(value.totalExpenses)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Mainpage, "There Are some error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
