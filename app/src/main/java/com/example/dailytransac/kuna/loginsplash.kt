package com.example.dailytransac.kuna

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.log

class loginsplash : AppCompatActivity() {

    lateinit var Referenceup: DatabaseReference
    private lateinit var handler: Handler
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    lateinit var currentDate:String
    private lateinit var valuefor:String
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var splashscreen=1000
    var uid = firebaseAuth.currentUser?.uid!!.toString()
    init {
        Referenceup = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("data")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginsplash)
        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatte = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val finalDate = "30/12/2100"
        var bundle: Bundle? = intent.extras
        var email = bundle?.getString("Email1") ?: ""
        var pass = bundle?.getString("Pass1") ?: ""

        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date())
                var dataStringm = currentDate.toString()

                val initiadate = LocalDate.parse(finalDate, formatte)
                val initda = initiadate.toEpochDay().toInt()
                val initiadate2 = LocalDate.parse(dataStringm, formatte)
                val initda2 = initiadate2.toEpochDay().toInt()
                Log.d("Mus", "" + initda)
                valuefor = (initda - initda2).toString()

                val mysendti: MutableMap<String, Any> = HashMap()
                mysendti["mydateg"] = currentDate
                Referenceup.child(valuefor).setValue(mysendti)
            }
        }
        // Start updating the TextView
        handler.post(updateTimeRunnable)


        Handler().postDelayed({
            var intent = Intent(this, loginpage2::class.java)
            intent.putExtra("email2", email)
            intent.putExtra("pass2", pass)
            startActivity(intent)
            finish()
        }, splashscreen.toLong())
    }
}




