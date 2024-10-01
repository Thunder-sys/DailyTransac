package com.example.dailytransac.Saksh

import MyViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dailytransac.Database.MyViewModelFactory
import com.example.dailytransac.Database.opt_verification
import com.example.dailytransac.databinding.ActivityLoginpageBinding
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class loginpage : AppCompatActivity() {
    lateinit var binding: ActivityLoginpageBinding
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var Reference:DatabaseReference
    lateinit var Reference1:DatabaseReference
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    init {
        Reference = FirebaseDatabase.getInstance().getReference().child("User")
        Reference1 = FirebaseDatabase.getInstance().getReference().child("User")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.sign.setOnClickListener(){
            val name = binding.entername.text.toString()
            val email = binding.emailEt.text.toString()
            val pass = binding.createPasswordEt.text.toString()
            val confirmpass = binding.confirmPasswordEt.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty())  {
                if (pass.length < 8){
                    binding.createPasswordEt.error = "Password must be at least 8 Characters"
                    binding.createPasswordEt.requestFocus()
                    return@setOnClickListener
                }
                if (confirmpass.length < 8){
                    binding.confirmPasswordEt.error = "Password must be at least 8 Characters"
                    binding.confirmPasswordEt.requestFocus()
                    return@setOnClickListener
                }
                if (pass==confirmpass) {
                    val intent = Intent(this, opt_verification::class.java)
                    intent.putExtra("name","$name")
                    intent.putExtra("email","$email")
                    intent.putExtra("pass","$pass")
                    startActivity(intent)
                }
                else{
                    binding.confirmPasswordEt.error = "Password is not mataching"
                }
            }
            else{
                Toast.makeText(this,"Empty Fields Are not Allowed !!",Toast.LENGTH_LONG).show()
            }
        }
    }
}