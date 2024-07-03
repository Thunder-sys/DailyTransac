package com.example.dailytransac.Saksh

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
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

class loginpage2 : AppCompatActivity() {
    lateinit var binding: ActivityLoginpage2Binding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginpage2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        //showpassword
        var pass = findViewById<EditText>(R.id.editpas)
        var checkBox = findViewById<CheckBox>(R.id.check)
         checkBox.setOnClickListener {
             if (checkBox.isChecked){
                 pass.inputType = 1
             }
             else
                 pass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
         }


        //text gradient
        var txt = findViewById<TextView>(R.id.halo)
        var pent = txt.paint
        var widh = pent.measureText(txt.text.toString())
        txt.paint.shader = LinearGradient(
            0f, 0f, widh, txt.textSize, intArrayOf(
                Color.parseColor("#a6767c"),
                Color.parseColor("#cf768b"),
                Color.parseColor("#9c2542"),
                Color.parseColor("#f892a9"),

                ), null, Shader.TileMode.REPEAT
        )







        firebaseAuth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener() {
            val email = binding.reEmail.text.toString()
            val pass = binding.rePassword.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
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