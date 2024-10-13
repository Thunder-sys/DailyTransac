package com.example.dailytransac.Database

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpData(
    private val senderEmail: String,
    private val senderPass: String,
    private val receiverEmail: String,
    private val subject: String,
    private val sendingMessage: String
) {

    fun sendOtp() {
        val stringHost = "smtp.gmail.com"
        val properties = System.getProperties().apply {
            put("mail.smtp.host", stringHost)
            put("mail.smtp.port", "465")
            put("mail.smtp.ssl.enable", "true")
            put("mail.smtp.auth", "true")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPass)
            }
        })

        val mimeMessage = MimeMessage(session).apply {
            setFrom(InternetAddress(senderEmail))
            addRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
            this.subject = subject
            setText(sendingMessage)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Transport.send(mimeMessage)
                withContext(Dispatchers.Main) {
                    println("Email sent successfully!")
                }
            } catch (e: MessagingException) {
                withContext(Dispatchers.Main) {
                    println("Failed to send email: ${e.message}")
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("An error occurred: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }
}
