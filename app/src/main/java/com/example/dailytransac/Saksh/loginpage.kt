package com.example.dailytransac.Saksh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.example.dailytransac.databinding.ActivityLoginpage2Binding
import com.example.dailytransac.databinding.ActivityLoginpageBinding
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth

class loginpage : AppCompatActivity() {
    lateinit var binding: ActivityLoginpageBinding
    lateinit var firebaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.sign.setOnClickListener(){
            val email = binding.emailEt.text.toString()
            val pass = binding.createPasswordEt.text.toString()
            val confirmpass = binding.confirmPasswordEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty())  {
                if (pass == confirmpass){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Password is not mataching",Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this,"Empty Fields Are not Allowed !!",Toast.LENGTH_LONG).show()
            }
        }
    }
}