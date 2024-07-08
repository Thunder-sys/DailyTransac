package com.example.dailytransac.khush

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.Database.User
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage2
import com.google.firebase.auth.FirebaseAuth

class splashpro : AppCompatActivity() {
    private var splashscreen=3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashpro)

        val backgroundImg: ImageView = findViewById(R.id.logo)
        val topAnimation =AnimationUtils.loadAnimation(this,R.anim.slide)
        backgroundImg.startAnimation(topAnimation)
        Handler().postDelayed({
           startActivity(Intent(this,loginpage2::class.java))
            finish()
        },splashscreen.toLong()

        )
    }
}