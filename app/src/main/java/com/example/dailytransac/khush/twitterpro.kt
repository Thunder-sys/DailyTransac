package com.example.dailytransac.khush

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage2

class twitterpro : AppCompatActivity() {
    private var twitterpro = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.twittersplash);

        Handler().postDelayed({
            startActivity(Intent(this, loginpage2::class.java))
            finish()
        },twitterpro.toLong()

        )

    }
}