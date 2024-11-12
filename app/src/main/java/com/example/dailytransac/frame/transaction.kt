package com.example.dailytransac.frame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class transaction : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebase_for_daily: DatabaseReference
    private lateinit var firebase_for_month: DatabaseReference
    private lateinit var firebase_for_dailyfull_data: DatabaseReference
    private lateinit var firebaseRefrence: DatabaseReference

    private lateinit var reco1: RecyclerView
    private lateinit var reco2: RecyclerView
    private lateinit var reco3: RecyclerView
    private lateinit var shimmereffect1: ShimmerFrameLayout
    private lateinit var shimmereffect2: ShimmerFrameLayout
    private lateinit var shimmereffect3: ShimmerFrameLayout

    private lateinit var handler: Handler
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    lateinit var currentDate:String

    var firebaseAuth = FirebaseAuth.getInstance()
    var uid = firebaseAuth.currentUser?.uid!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_transaction, container, false)


        firebaseDatabase = FirebaseDatabase.getInstance()
        firebase_for_daily = firebaseDatabase.getReference("User").child(uid).child("daily")
        firebase_for_month = firebaseDatabase.getReference("User").child(uid).child("monthly")
        firebaseRefrence = firebaseDatabase.getReference().child("User").child(uid).child("monthly")

        shimmereffect1 = view.findViewById(R.id.shimmer1)
        shimmereffect2 = view.findViewById(R.id.shimmer2)
        shimmereffect3 = view.findViewById(R.id.shimmer3)

        simmereffect(view,shimmereffect1,shimmereffect2,shimmereffect3)

        reco1 = view.findViewById(R.id.recy1)
        reco2 = view.findViewById(R.id.recy2)
        reco3 = view.findViewById(R.id.recy)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())

                // Schedule the next update in 1 second
                handler.postDelayed(this, 1000)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)


        val myman = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        reco1.layoutManager = myman
        val mythisn = ArrayList<Model_daily>()
        val myadap = Adapter_daily(mythisn)
        reco1.adapter = myadap

        var listofdata: ArrayList<Model_mainpage> = ArrayList()
        reco3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var adaptt = Adapter_mainpage(listofdata)
        reco3.adapter = adaptt

        firebase_for_daily.limitToFirst(30).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (myds in dataSnapshot.children) {
                    var entry = myds.child("entry").getValue().toString()
                    var Expenses = myds.child("Expenses").getValue().toString()
                    var income = myds.child("income").getValue().toString()
                    var mydateg = myds.child("mydateg").getValue().toString()
                    var datevalue = myds.child("datevalue").getValue().toString()
                    var dateVlaue = datevalue.toInt()

                    reco1.visibility = View.VISIBLE
                    mythisn.add(Model_daily(mydateg, entry, Expenses, income))
                    shimmereffect1.visibility = View.GONE

                    var childitem = ArrayList<Model_reco>()
                    firebase_for_dailyfull_data =
                        firebaseDatabase.getReference("User").child(uid).child("daily")
                            .child("$dateVlaue").child("dateri")

                    firebase_for_dailyfull_data.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (mydata in datasnapshot.children) {
                                var entry2 = mydata.child("entry2").getValue().toString()
                                var work = mydata.child("work").getValue().toString()
                                var spinner = mydata.child("Spinner").getValue().toString()
                                shimmereffect1.visibility = View.GONE
                                shimmereffect3.visibility = View.GONE
                                reco3.visibility = View.VISIBLE
                                childitem.add(Model_reco(entry2, work, spinner))
                            }
                            adaptt.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                requireContext(),
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
                Toast.makeText(requireContext(), "There Are some error", Toast.LENGTH_SHORT).show()
            }
        })

        var listofmonth = ArrayList<Model_monthly>()
        reco2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var adap = Adapter_monthly(listofmonth)
        reco2.adapter = adap


        firebaseRefrence.limitToFirst(30).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(apidata in snapshot.children) {
                    var Year = apidata.child("currentmonth").getValue().toString()
                    var Totalentry = apidata.child("totalrevenue").getValue().toString()
                    var Totalexpenses = apidata.child("totalexpenses").getValue().toString()
                    var Totalsaving = apidata.child("totalsaving").getValue().toString()


                    reco2.visibility = View.VISIBLE

                    listofmonth.add(Model_monthly("$Year", Totalentry, Totalexpenses, Totalsaving))

                    shimmereffect1.visibility = View.GONE
                    shimmereffect2.visibility = View.GONE
                    shimmereffect3.visibility = View.GONE
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

        return view

    }

    private fun simmereffect(
        view: View,
        shimmereffect1: ShimmerFrameLayout,
        shimmereffect2: ShimmerFrameLayout,
        shimmereffect3: ShimmerFrameLayout
    ) {

        shimmereffect1.visibility = View.VISIBLE
        shimmereffect2.visibility = View.VISIBLE
        shimmereffect3.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateTimeRunnable)
    }
}