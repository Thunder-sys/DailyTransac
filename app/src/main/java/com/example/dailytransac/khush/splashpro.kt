package com.example.dailytransac.khush

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage2

class splashpro : AppCompatActivity() {
    private var splashscreen=3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashpro)
        Handler().postDelayed({
            var intent=Intent(this,loginpage2::class.java)
            startActivity(intent)
            finish()
        },splashscreen.toLong()

        )
    }
}