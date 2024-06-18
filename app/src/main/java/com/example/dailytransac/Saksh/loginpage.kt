package com.example.dailytransac.Saksh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.example.dailytransac.kuna.MainActivity

class loginpage : AppCompatActivity() {
    lateinit var log:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginpage)
        log=findViewById(R.id.sign)
        log.setOnClickListener(){
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}