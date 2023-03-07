package com.example.shophop.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shophop.MainActivity
import com.example.shophop.R
import com.example.shophop.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpactivityBinding.inflate(layoutInflater)
        binding.button3.setOnClickListener {
            if(binding.userOTP.text!!.isEmpty()){
                Toast.makeText(this,"Please provide OTP",Toast.LENGTH_SHORT).show()
            }else{
                verifyUser(binding.userOTP.text.toString())
            }
        }

        setContentView(binding.root)
    }

    private fun verifyUser(otp: String) {

        val credential = PhoneAuthProvider.getCredential(intent.getStringExtra("verificationId")!!, otp)
        signInWithPhoneAuthCredential(credential)

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val preference=this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor=preference.edit()
                    editor.putString("number",intent.getStringExtra("number")!!)
                    editor.apply()

                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                  Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
    }
}