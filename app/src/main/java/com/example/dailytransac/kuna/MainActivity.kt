package com.example.dailytransac.kuna

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.Mainpage
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var layout_list:LinearLayout
    lateinit var add_button:Button
    lateinit var entry:EditText
    lateinit var expences:TextView
    lateinit var income:TextView
    lateinit var sand:ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sand=findViewById(R.id.secondActivityButton)
        sand.setOnClickListener(){
            var dhh=Intent(this,Mainpage::class.java)
            startActivity(dhh)
        }
        layout_list = findViewById(R.id.Layout_list)
        add_button = findViewById(R.id.add)
        addcard()
        add_button.setOnClickListener(){
            addcard()
        }
        entry = findViewById(R.id.entry1)
        expences = findViewById(R.id.expences)
        income = findViewById(R.id.income)

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
        val delete:ImageButton = view.findViewById(R.id.delete)
        val entry2:EditText = view.findViewById(R.id.entry2)
      /*  val work:EditText = view.findViewById(R.id.work)
        var mytkvl=entry2.text.toString()
        Log.d("Myco","u"+mytkvl);*/
        entry2.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val mytkvl:Int = entry2.text.toString().toInt()
                var c = 0
                if (mytkvl!=null) {
                    var i = 0
                    do {
                        i = i + mytkvl
                        expences.setText(""+i)
                        c++
                    } while (c < 1)
                }

            }
        }
        val spinner:Spinner = view.findViewById(R.id.spinner)
        setupspinner(spinner)
        delete.setOnClickListener(){
            layout_list.removeView(view)
        }
        layout_list.addView(view)

    }
}