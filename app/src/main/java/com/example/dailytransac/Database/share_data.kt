package com.example.dailytransac.Database

import MyViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.concurrent.thread
import kotlin.random.Random

class share_data : AppCompatActivity() {
    lateinit var binding: ActivityShareDataBinding
    private lateinit var reco1: RecyclerView
    private lateinit var myadap:setting_adapter
    private lateinit var mythisn:ArrayList<setting_modle>
    private lateinit var auth :FirebaseAuth
    private lateinit var refree:DatabaseReference
    private val myViewModel: MyViewModel by viewModels { MyViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShareDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reco1 = findViewById(R.id.setting_reco)
        auth = FirebaseAuth.getInstance()
        refree = FirebaseDatabase.getInstance().getReference().child("User")

        val myman = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
            "Forget Password"->{
                binding.emailEnter.visibility = View.VISIBLE
                binding.emailDelete.setOnClickListener(){
                    binding.emailEnter.visibility = View.GONE
                }
                binding.emailNext.setOnClickListener(){
                    val email = binding.enterEmail.text.toString()
                    if ( email.isNotEmpty()) {
                        auth.sendPasswordResetEmail(email)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Please Check Your Email", Toast.LENGTH_LONG)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                            }
                        binding.emailEnter.visibility = View.GONE
                    }
                    else{
                        binding.enterEmail.setError("Please Enter Your Email")
                    }
                }
            }
            "Edit Name"->{
                binding.editName.visibility = View.VISIBLE
                var uid = auth.currentUser?.uid!!
                refree.child(uid).child("moredata")
                    .limitToFirst(1).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ip in snapshot.children){
                            var name = ip.child("name").getValue().toString()
                            var uid = ip.child("uid").getValue().toString()
                            var email = ip.child("email").getValue().toString()
                            var no = ip.child("no").getValue().toString()
                            binding.enterEditYourName.setText(name)
                            binding.editUid.setText(uid)
                            binding.editNo.setText(no)
                            binding.editEmail.setText(email)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                binding.editNameDelete.setOnClickListener(){
                    binding.editName.visibility = View.GONE
                }
                binding.editNameNext.setOnClickListener(){
                    val name = binding.enterEditYourName.text.toString()
                    val email = binding.editEmail.text.toString()
                    val uid1 = binding.editUid.text.toString()
                    val no = binding.editNo.text.toString()
                    if (name.isNotEmpty()){
                        val mysendt: MutableMap<String, Any> = HashMap()
                        mysendt["name"] = name
                        mysendt["email"] = email
                        mysendt["uid"] = uid
                        mysendt["no"] = no
                        refree.child(uid).child("moredata").child("op").updateChildren(mysendt)
                        binding.editName.visibility = View.GONE
                    }
                    else{
                        Toast.makeText(this,"Enter Your UserName",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "Change Password"->{
                var user = auth.currentUser
                binding.oldPassword.visibility = View.VISIBLE
                binding.oldCancel.setOnClickListener(){
                    binding.oldPassword.visibility = View.GONE
                }
                binding.oldNext.setOnClickListener(){
                    var pass = binding.enteroldPasswordEt.text.toString()
                    if(pass.isEmpty()){
                        binding.enteroldPasswordEt.setError("Password is required")
                        binding.enteroldPasswordEt.requestFocus()
                        return@setOnClickListener
                    }
                    user.let {
                        var credential = EmailAuthProvider.getCredential(user!!.email!!,pass)
                        user.reauthenticate(credential)
                            .addOnCompleteListener{ task ->
                                when{
                                    task.isSuccessful->{
                                        binding.oldPassword.visibility = View.GONE
                                        binding.newPassword.visibility = View.VISIBLE
                                    }
                                    task.exception is FirebaseAuthInvalidCredentialsException->{
                                        binding.enteroldPasswordEt.setError("Invalid Password")
                                        binding.enteroldPasswordEt.requestFocus()
                                    }
                                    else->{
                                        Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        binding.newNext.setOnClickListener(){
                            var new1 = binding.enterNewPassword.text.toString()
                            var con1 = binding.enterConfrimPassword.text.toString()
                            if (new1.length < 8){
                                binding.enterNewPassword.error = "Password must be at least 8 Characters"
                                binding.enterNewPassword.requestFocus()
                                return@setOnClickListener
                            }
                            if (con1.length < 8){
                                binding.enterConfrimPassword.error = "Password must be at least 8 Characters"
                                binding.enterConfrimPassword.requestFocus()
                                return@setOnClickListener
                            }
                            if (new1!=con1){
                                binding.enterConfrimPassword.error = "Password doesn't match"
                                binding.enterConfrimPassword.requestFocus()
                                return@setOnClickListener
                            }
                            user.let {
                                it.updatePassword(new1)
                                    .addOnCompleteListener { task->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Password Updated",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            binding.newPassword.visibility = View.GONE
                                            binding.oldPassword.visibility = View.GONE
                                            logoout()
                                        }
                                        else{
                                            Toast.makeText(
                                                this,
                                                task.exception?.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun logoout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        myViewModel.textValue=""
        myViewModel.passwork=""
        var i = Intent(this,loginpage2::class.java)
        startActivity(i)
    }

    private fun op() {
        mythisn.add(setting_modle(R.drawable.edit_name,"Edit Name"))
        mythisn.add(setting_modle(R.drawable.change_lock,"Change Password"))
        mythisn.add(setting_modle(R.drawable.password,"Forget Password"))
        mythisn.add(setting_modle(R.drawable.logout,"Logout"))
    }
}