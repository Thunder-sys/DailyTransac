package com.example.dailytransac.frame

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.kuna.graph_spinner_adapter
import com.example.dailytransac.kuna.graph_spinner_model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class graph : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid: String = firebaseAuth.currentUser?.uid ?: ""

    private lateinit var spinnerReference: DatabaseReference
    private lateinit var showSpinner: TextView
    private lateinit var listOfMonth: ArrayList<graph_spinner_model>
    private lateinit var adapter: graph_spinner_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        showSpinner = view.findViewById(R.id.shorspinner)
        showSpinner.setOnClickListener {
            showGraphYearSpinner()
        }

        spinnerReference = FirebaseDatabase.getInstance().getReference()
            .child("User").child(uid).child("yearspinner")

        return view
    }

    private fun showGraphYearSpinner() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.graph_spinner_show)

        val recyclerView: RecyclerView = dialog.findViewById(R.id.graph_recycle)
        val searchView:androidx.appcompat.widget.SearchView = dialog.findViewById(R.id.graph_searchView)

        listOfMonth = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = graph_spinner_adapter(listOfMonth){ dattaspinner->
            showSpinner.setText(dattaspinner.text)
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
                    val yearSpinner = ip.child("yearspinner").getValue(String::class.java) ?: ""
                    listOfMonth.add(graph_spinner_model(yearSpinner))
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        dialog.show()
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
}
