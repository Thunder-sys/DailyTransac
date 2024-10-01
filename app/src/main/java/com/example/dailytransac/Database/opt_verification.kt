package com.example.dailytransac.Database

import MyViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.dailytransac.R
import com.example.dailytransac.databinding.ActivityLoginpageBinding
import com.example.dailytransac.databinding.ActivityOptVerificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random
import kotlin.random.nextInt

class opt_verification : AppCompatActivity() {
    lateinit var binding: ActivityOptVerificationBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var Reference: DatabaseReference
    lateinit var Reference1: DatabaseReference
    var name:String = ""
    var email:String = ""
    var pass:String = ""
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    init {
        Reference = FirebaseDatabase.getInstance().getReference().child("User")
        Reference1 = FirebaseDatabase.getInstance().getReference().child("User")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOptVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()
        pass = intent.getStringExtra("pass").toString()

        binding.otpEmail.setText("$email")

        binding.input1.doOnTextChanged { text, start, before, count ->
            if(!binding.input1.text.toString().isEmpty()){
                binding.input2.requestFocus()
            }
            if(!binding.input2.text.toString().isEmpty()){
                binding.input2.requestFocus()
            }
        }
        binding.input2.doOnTextChanged { text, start, before, count ->
            if(binding.input2.text.toString().isEmpty()){
                binding.input3.requestFocus()
            }
            else{
                binding.input1.requestFocus()
            }
        }
        binding.input3.doOnTextChanged { text, start, before, count ->
            if(binding.input3.text.toString().isEmpty()){
                binding.input4.requestFocus()
            }
            else{
                binding.input2.requestFocus()
            }
        }
        binding.input4.doOnTextChanged { text, start, before, count ->
            if(binding.input4.text.toString().isEmpty()){
                binding.input5.requestFocus()
            }
            else{
                binding.input3.requestFocus()
            }
        }
        binding.input5.doOnTextChanged { text, start, before, count ->
            if(binding.input5.text.toString().isEmpty()){
                binding.input6.requestFocus()
            }
            else{
                binding.input4.requestFocus()
            }
        }
        binding.input6.doOnTextChanged { text, start, before, count ->
            if(binding.input6.text.toString().isEmpty()){
                binding.input5.requestFocus()
            }
            binding.otpSubmit.setOnClickListener(){
                var otp1 = binding.input1.text.toString()
                var otp2 = binding.input2.text.toString()
                var otp3 = binding.input3.text.toString()
                var otp4 = binding.input4.text.toString()
                var otp5 = binding.input5.text.toString()
                var otp6 = binding.input6.text.toString()

                var otp = "$otp1$otp2$otp3$otp4$otp5$otp6"

                if (binding.input1.text.toString().isEmpty() ||
                    binding.input2.text.toString().isEmpty() ||
                    binding.input3.text.toString().isEmpty() ||
                    binding.input4.text.toString().isEmpty() ||
                    binding.input5.text.toString().isEmpty() ||
                    binding.input6.text.toString().isEmpty()){
                    Toast.makeText(this@opt_verification,"Enter Otp",Toast.LENGTH_SHORT).show()
                }
                else if (!otp.equals()){

                }
            }
        }
    }

    fun random(){
        var random = Random.nextInt(100000..999999)
    }

    private fun addUsertoDatabase(name:String,email: String, uid: String) {
        val mysendt: MutableMap<String, Any> = HashMap()
        mysendt["email"] = email
        mysendt["uid"] = uid
        val mysendt1: MutableMap<String, Any> = HashMap()
        mysendt1["name"] = name
        mysendt1["email"] = email
        mysendt1["uid"] = uid
        mysendt1["no"] = Random.nextInt(0, 10).toString()
        Reference.child(uid).setValue(mysendt)
        Reference1.child(uid).child("moredata").child("op").setValue(mysendt1)
    }
}