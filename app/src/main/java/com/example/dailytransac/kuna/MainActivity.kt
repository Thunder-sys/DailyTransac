package com.example.dailytransac.kuna

import MyViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.dailytransac.Database.MyViewModelFactory
import com.example.dailytransac.Database.share_data
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.loginpage
import com.example.dailytransac.Saksh.loginpage2
import com.example.dailytransac.databinding.ActivityMainBinding
import com.example.dailytransac.frame.analysis
import com.example.dailytransac.frame.graph
import com.example.dailytransac.frame.home
import com.example.dailytransac.frame.reached_updatelist
import com.example.dailytransac.frame.transaction
import com.example.dailytransac.frame.updatelist
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    var auth = FirebaseAuth.getInstance()
    var uid = auth.currentUser?.uid!!
    lateinit var firebasedatabase : FirebaseDatabase
    private lateinit var firebaseRefer: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebasedatabase = FirebaseDatabase.getInstance()
        firebaseRefer = firebasedatabase.getReference().child("User").child(uid).child("moredata")

        var drawerLayout :DrawerLayout = findViewById(R.id.contract)
        var navView :NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        var headerview: View = navView.getHeaderView(0)
        var imageview:TextView = headerview.findViewById(R.id.user_image)
        var user_name:TextView = headerview.findViewById(R.id.user_name)
        var user_email:TextView = headerview.findViewById(R.id.user_email)
        firebaseRefer.limitToFirst(1).addListenerForSingleValueEvent(object :ValueEventListener{
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    var name = i.child("name").getValue().toString()
                    var email = i.child("email").getValue().toString()
                    var no = i.child("no").getValue().toString().toInt() ?: 0
                    var io = name.substring(0,1).toString().toUpperCase()
                    imageview.setText(io)
                    user_name.setText("$name")
                    user_email.setText("$email")
                    when(no){
                        1 -> imageview.setBackgroundColor(R.color.red)
                        2 -> imageview.setBackgroundColor(R.color.blue)
                        3 -> imageview.setBackgroundColor(R.color.black)
                        4 -> imageview.setBackgroundColor(R.color.sea)
                        5 -> imageview.setBackgroundColor(R.color.sea_blue)
                        6 -> imageview.setBackgroundColor(R.color.green)
                        7 -> imageview.setBackgroundColor(R.color.dark_green)
                        8 -> imageview.setBackgroundColor(R.color.light_green)
                        9 -> imageview.setBackgroundColor(R.color.red)
                        10 -> imageview.setBackgroundColor(R.color.shimmer)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("main","error")
            }

        })



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replacefragement(home())
                R.id.analysis -> replacefragement(analysis())
                R.id.transaction -> replacefragement(transaction())
                R.id.graph -> replacefragement(graph())
                R.id.setting ->{
                    var i = Intent(this,share_data::class.java)
                    startActivity(i)
                }
                R.id.share -> {
                   var intent:Intent = Intent()
                    intent.setAction(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT , "Hello")
                    intent.setType("text/plain")
                    if (intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent)
                    }
                }
                R.id.rate ->{
                    var uri:Uri = Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext())
                    var i = Intent(Intent.ACTION_VIEW,uri)
                    startActivity(i)

                }
            }
            true
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        replacefragement(home())
 
        binding.bottomNavi.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replacefragement(home())
                R.id.analysis -> replacefragement(analysis())
                R.id.transaction -> replacefragement(transaction())
                R.id.graph -> replacefragement(graph())
                else -> {

                }
            }
            true
        }
    }
    private fun replacefragement(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragementTransaction = fragmentManager.beginTransaction()
        fragementTransaction.replace(R.id.frame,fragment)
        fragementTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}