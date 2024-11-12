package com.example.dailytransac.Database

import MyViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.dailytransac.databinding.ActivityOptVerificationBinding
import com.example.dailytransac.kuna.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.Transport
import kotlin.random.Random

class opt_verification : AppCompatActivity() {
    lateinit var binding: ActivityOptVerificationBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var Reference: DatabaseReference
    lateinit var Reference1: DatabaseReference
    var name:String = ""
    var email:String = ""
    var pass:String = ""
    var random:Int = 0
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

        random()

        binding.otpSentAgain.setOnClickListener() {
            random()
            Toast.makeText(this, "resend otp", Toast.LENGTH_SHORT).show()
        }


        binding.input1.doOnTextChanged { text, start, before, count ->
            if (!binding.input1.text.toString().isEmpty()) {
                binding.input2.requestFocus()
            }
        }
        binding.input2.doOnTextChanged { text, start, before, count ->
            if (binding.input2.text.toString().isEmpty()) {
                binding.input1.requestFocus()
            } else {
                binding.input3.requestFocus()
            }
        }
        binding.input3.doOnTextChanged { text, start, before, count ->
            if (binding.input3.text.toString().isEmpty()) {
                binding.input2.requestFocus()
            } else {
                binding.input4.requestFocus()
            }
        }
        binding.input4.doOnTextChanged { text, start, before, count ->
            if (binding.input4.text.toString().isEmpty()) {
                binding.input3.requestFocus()
            } else {
                binding.input5.requestFocus()
            }
        }
        binding.input5.doOnTextChanged { text, start, before, count ->
            if (binding.input5.text.toString().isEmpty()) {
                binding.input4.requestFocus()
            } else {
                binding.input6.requestFocus()
            }
        }
        binding.input6.doOnTextChanged { text, start, before, count ->
            if (binding.input6.text.toString().isEmpty()) {
                binding.input5.requestFocus()
            }
            binding.otpSubmit.setOnClickListener() {
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
                    binding.input6.text.toString().isEmpty()
                ) {
                    Toast.makeText(this@opt_verification, "Enter Otp", Toast.LENGTH_SHORT).show()
                } else if (!otp.equals(random.toString())) {
                    Toast.makeText(this@opt_verification, "Wrong Otp", Toast.LENGTH_SHORT).show()
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            addUsertoDatabase(name, email, firebaseAuth.currentUser?.uid!!)
                            myViewModel.textValue = email
                            myViewModel.passwork = pass
                            var intent = Intent(this@opt_verification, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@opt_verification,
                                it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    fun random() {
        random = Random.nextInt(100000, 999999) // Generate a random number
        try {
            val senderEmail = "radharanajiai@gmail.com"
            val senderPass = "mqpqjrzwxpouiywv"
            val receiverEmail = binding.otpEmail.text.toString()

            val stringHost = "smtp.gmail.com"

            val properties = System.getProperties().apply {
                put("mail.smtp.host", stringHost)
                put("mail.smtp.port", "465")
                put("mail.smtp.ssl.enable", "true")
                put("mail.smtp.auth", "true")
            }


            val session:Session =Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPass)
                }
            })

            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(senderEmail))
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
            mimeMessage.subject = "This is your Daily Transac Verification "
            mimeMessage.setText("Your Daily Transac Verification is:" +
                    " $random")

            val thread = Thread {
                try {
                    Transport.send(mimeMessage)
                    println("Email sent successfully!")
                } catch (e: MessagingException) {
                    println("Failed to send email: ${e.message}")
                    e.printStackTrace()
                }
            }
            thread.start()



        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
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