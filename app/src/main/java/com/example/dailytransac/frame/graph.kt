package com.example.dailytransac.frame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.dailytransac.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class graph : Fragment() {

    var firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    var uid = firebaseAuth.currentUser?.uid!!

    lateinit var spinnerList:ArrayList<String>
    lateinit var adapter:ArrayAdapter<String>
    lateinit var spinner: Spinner
    lateinit var spinnerrefernce:DatabaseReference

    lateinit var showspinner:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_graph, container, false)

        spinner = view.findViewById(R.id.graph_spinner)
        showspinner = view.findViewById(R.id.spinnershow)

        spinnerrefernce = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("yearspinner")

        spinnerList = ArrayList()
        adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,spinnerList)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                showspinner.text = spinnerList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        showdata()


        return view
    }

    private fun showdata() {
        spinnerrefernce.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ip in snapshot.children){
                    var yeatspinner = ip.child("yearspinner").getValue().toString()
                    spinnerList.add(yeatspinner)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}