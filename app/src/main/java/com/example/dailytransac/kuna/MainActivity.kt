package com.example.dailytransac.kuna

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dailytransac.R
import com.example.dailytransac.databinding.ActivityMainBinding
import com.example.dailytransac.frame.analysis
import com.example.dailytransac.frame.graph
import com.example.dailytransac.frame.home
import com.example.dailytransac.frame.reached_updatelist
import com.example.dailytransac.frame.transaction
import com.example.dailytransac.frame.updatelist

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}