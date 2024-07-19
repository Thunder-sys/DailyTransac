package com.example.dailytransac.Saksh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.Database.User
import com.example.dailytransac.R
import com.example.dailytransac.databinding.ActivityLoginpage2Binding
import com.example.dailytransac.databinding.ActivityLoginpageBinding
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class loginpage2 : AppCompatActivity() {
    lateinit var binding: ActivityLoginpage2Binding
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var Reference:DatabaseReference
    init {
        Reference = FirebaseDatabase.getInstance().getReference().child("User")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginpage2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener(){
            val email = binding.reEmail.text.toString()
            val pass = binding.editpas.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty())  {
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {

                    if (it.isSuccessful) {
                        addUsertoDatabase(email,firebaseAuth.currentUser?.uid!!)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Password is not mataching", Toast.LENGTH_LONG).show()
            }
        }
        binding.create.setOnClickListener(){
            var jkl= Intent(this,loginpage::class.java)
            startActivity(jkl)
        }
    }

    private fun addUsertoDatabase(email: String, uid: String) {
        val mysendt: MutableMap<String, Any> = HashMap()
        mysendt["email"] = email
        mysendt["uid"] = uid
        Reference.child(uid).setValue(mysendt)
    }
}