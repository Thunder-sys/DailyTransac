package com.example.dailytransac.Database

import MyViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.Saksh.Adapter_daily
import com.example.dailytransac.Saksh.Model_daily
import com.example.dailytransac.Saksh.loginpage2
import com.example.dailytransac.databinding.ActivityMainBinding
import com.example.dailytransac.databinding.ActivityShareDataBinding
import com.example.dailytransac.kuna.MainActivity
import com.example.dailytransac.kuna.home_spinner_adapter
import com.example.dailytransac.kuna.home_spinner_model
import com.example.dailytransac.kuna.setting_adapter
import com.example.dailytransac.kuna.setting_modle

class share_data : AppCompatActivity() {
    lateinit var binding: ActivityShareDataBinding
    private lateinit var reco1: RecyclerView
    private lateinit var myadap:setting_adapter
    private lateinit var mythisn:ArrayList<setting_modle>
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShareDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reco1 = findViewById(R.id.setting_reco)

        val myman = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reco1.layoutManager = myman
        mythisn = ArrayList()
        myadap = setting_adapter(mythisn) { jj ->
            control(jj)
        }
        reco1.adapter = myadap


        binding.leftKeyForMain.setOnClickListener(){
            var i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        op()
    }

    private fun control(jj: String) {
        when(jj){
            "Logout"->{
                myViewModel.textValue=""
                myViewModel.passwork=""
                var i = Intent(this,loginpage2::class.java)
                startActivity(i)
            }
            "Change Password"->{

            }
        }
    }

    private fun op() {
        mythisn.add(setting_modle(R.drawable.change_lock,"Change Password"))
        mythisn.add(setting_modle(R.drawable.logout,"Logout"))
    }
}