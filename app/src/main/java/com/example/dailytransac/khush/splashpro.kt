package com.example.dailytransac.khush

import MyViewModel
import com.example.dailytransac.Database.MyViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage2
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth

class splashpro : AppCompatActivity() {
    private var splashscreen=3000
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashpro)

        firebaseAuth = FirebaseAuth.getInstance()

        val backgroundImg: ImageView = findViewById(R.id.logo)
        val topAnimation =AnimationUtils.loadAnimation(this,R.anim.slide)
        backgroundImg.startAnimation(topAnimation)
        Handler().postDelayed({
            var email = myViewModel.textValue.toString()
            var pass = myViewModel.passwork.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                startActivity(Intent(this, loginpage2::class.java))
                finish()
            }
        },splashscreen.toLong()

        )
    }
}