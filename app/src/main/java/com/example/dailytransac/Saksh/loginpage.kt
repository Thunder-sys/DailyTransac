package com.example.dailytransac.Saksh

import MyViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.Database.MyViewModelFactory
import com.example.dailytransac.Database.User
import com.example.dailytransac.R
import com.example.dailytransac.databinding.ActivityLoginpage2Binding
import com.example.dailytransac.databinding.ActivityLoginpageBinding
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class loginpage : AppCompatActivity() {
    lateinit var binding: ActivityLoginpageBinding
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var Reference:DatabaseReference
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    init {
        Reference = FirebaseDatabase.getInstance().getReference().child("User")
    }
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
                if (pass==confirmpass){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        myViewModel.textValue = email
                        myViewModel.passwork = pass
                        if (it.isSuccessful){
                            addUsertoDatabase(email,firebaseAuth.currentUser?.uid!!)
                            val intent = Intent(this, MainActivity::class.java)
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
    private fun addUsertoDatabase(email: String, uid: String) {
        val mysendt: MutableMap<String, Any> = HashMap()
        mysendt["email"] = email
        mysendt["uid"] = uid
        Reference.child(uid).setValue(mysendt)
    }
}