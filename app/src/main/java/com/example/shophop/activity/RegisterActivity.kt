package com.example.shophop.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shophop.R
import com.example.shophop.databinding.ActivityRegisterBinding
import com.example.shophop.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)

        binding.button4.setOnClickListener {
            openLogin()
        }
        binding.button3.setOnClickListener {
            validateUser()
        }
        setContentView(binding.root)
    }

    private fun validateUser() {
        if(binding.userName.text!!.isEmpty() || binding.userNumber.text!!.isEmpty()){
            Toast.makeText(this,"Please fill the fields",Toast.LENGTH_SHORT).show()
        }else{
            storeData()
        }
    }

    private fun storeData() {
        val builder=AlertDialog.Builder(this).setTitle("Loading....").setMessage("Please wait").setCancelable(false).create()
        builder.show()

        val preference=this.getSharedPreferences("user", MODE_PRIVATE)
        val editor=preference.edit()
        editor.putString("name",binding.userName.text.toString())
        editor.putString("number",binding.userNumber.text.toString())
        editor.apply()

        val data=UserModel(userName =binding.userName.text.toString(), userNumber = binding.userNumber.text.toString() )

        Firebase.firestore.collection("users").document(binding.userNumber.text.toString())
            .set(data).addOnSuccessListener {
                Toast.makeText(this,"User Registered",Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()
            }.addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                builder.dismiss()
            }

    }

    private fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}