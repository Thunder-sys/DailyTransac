package com.example.dailytransac.kuna

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.R

class MainActivity : AppCompatActivity() {

    lateinit var layout_list:LinearLayout
    lateinit var add_button:Button
    lateinit var entry:EditText
    lateinit var expences: TextView
    lateinit var income: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        layout_list = findViewById(R.id.Layout_list)
        add_button = findViewById(R.id.add)
        addcard()
        add_button.setOnClickListener(){
            addcard()
        }
        entry = findViewById(R.id.entry1)
        expences = findViewById(R.id.expences)
        income = findViewById(R.id.income)
        expences.isActivated = true
        expences.movementMethod = ScrollingMovementMethod()
        expences.isPressed = true

        var str:String



    }

    private fun expensiontext(str:String) {
        expences.text = str
    }
    private fun incometext(){

    }

    private fun setupspinner(spinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Work,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun addcard() {
        val view:View = layoutInflater.inflate(R.layout.add_list,null)
        val delete: ImageButton = view.findViewById(R.id.delete)
        val entry2: EditText = view.findViewById(R.id.entry2)
        val work: EditText = view.findViewById(R.id.work)
        val spinner: Spinner = view.findViewById(R.id.spinner)


        setupspinner(spinner)
        delete.setOnClickListener(){
            layout_list.removeView(view)
        }
        layout_list.addView(view)
    }
}