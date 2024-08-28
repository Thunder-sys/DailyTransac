package com.example.dailytransac.Saksh

import MyViewModel
import com.example.dailytransac.Database.MyViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.databinding.ActivityLoginpage2Binding
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth

class loginpage2 : AppCompatActivity() {
    lateinit var binding: ActivityLoginpage2Binding
    lateinit var firebaseAuth: FirebaseAuth
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginpage2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener() {
            val email = binding.reEmail.text.toString()
            val pass = binding.editpas.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {

                    myViewModel.textValue = email
                    myViewModel.passwork = pass
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Password is not mataching", Toast.LENGTH_LONG).show()
            }
        }
        binding.create.setOnClickListener() {
            var jkl = Intent(this, loginpage::class.java)
            startActivity(jkl)
        }
    }
}