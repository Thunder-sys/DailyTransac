package com.example.dailytransac.Saksh

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
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

        val sharef:SharedPreferences = getSharedPreferences("namedemo", MODE_PRIVATE)


        val s1:String? =sharef.getString("Email","")
        val s2:String? =sharef.getString("Pass","")

        var s11 = s1.toString()
        var s22 = s2.toString()

        if (s11.isEmpty() && s22.isEmpty()){
            Toast.makeText(this,"Enter your Email or Password",Toast.LENGTH_LONG).show()
        }
        else{
            binding.reEmail.setText("$s11")
            binding.editpas.setText("$s22")
            val email1 = binding.reEmail.text.toString()
            val pass1 = binding.editpas.text.toString()
            firebaseAuth.signInWithEmailAndPassword(email1,pass1).addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.login.setOnClickListener(){
            val email = binding.reEmail.text.toString()
            val pass = binding.editpas.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty())  {
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {

                    if (it.isSuccessful) {
                        val editor:SharedPreferences.Editor = sharef.edit()
                        editor.putString("Email",email)
                        editor.putString("Pass",pass)
                        editor.commit()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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
}