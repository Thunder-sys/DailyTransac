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

class loginpage2 : AppCompatActivity() {
    lateinit var go:Button
    lateinit var signup:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginpage2)
         go=findViewById(R.id.login)
         signup=findViewById(R.id.create)
        go.setOnClickListener(){
            var jkl= Intent(this,MainActivity::class.java)
            startActivity(jkl)
        }
        signup.setOnClickListener(){
            var jkl= Intent(this,loginpage::class.java)
            startActivity(jkl)
        }

    }
}