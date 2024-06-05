package com.example.dailytransac.kuna

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R

class MainActivity : AppCompatActivity() {

    lateinit var layout_list:LinearLayout
    lateinit var add_button:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        layout_list = findViewById(R.id.Layout_list)
        add_button = findViewById(R.id.add)


    }

}