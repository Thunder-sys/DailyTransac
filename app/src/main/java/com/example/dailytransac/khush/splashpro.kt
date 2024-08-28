package com.example.dailytransac.khush

import MyViewModel
import com.example.dailytransac.Database.MyViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage2
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashPro : AppCompatActivity() {
    private val splashScreenDuration = 3000L // Use Long type directly
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashpro)

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up the animation for the splash screen logo
        val backgroundImg: ImageView = findViewById(R.id.logo)
        val topAnimation = AnimationUtils.loadAnimation(this, R.anim.slide)
        backgroundImg.startAnimation(topAnimation)

        // Use Handler with Looper for better context management
        Handler(Looper.getMainLooper()).postDelayed({
            // Retrieve email and password from ViewModel
            val email = myViewModel.textValue ?: ""
            val pass = myViewModel.passwork ?: ""

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Attempt Firebase sign-in
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-in successful, navigate to MainActivity
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // Sign-in failed, show error message
                        Toast.makeText(this, task.exception?.message ?: "Login failed", Toast.LENGTH_LONG).show()
                    }
                    finish() // Ensure the splash screen activity is closed after processing
                }
            } else {
                // No credentials, navigate to login page
                startActivity(Intent(this, loginpage2::class.java))
                finish() // Ensure the splash screen activity is closed
            }
        }, splashScreenDuration)
    }
}
