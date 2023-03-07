package com.example.shophop.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shophop.R
import com.example.shophop.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddressBinding
    private lateinit var preference:SharedPreferences
    private lateinit var totalCost:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference=this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost=intent.getStringExtra("totalCost")!!
        loadUserInfo()
        binding.proceed.setOnClickListener {
            validateData(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.userVillage.text.toString(),
                binding.userState.text.toString(),
                binding.userCity.text.toString(),
                binding.userPincode.text.toString()
            )
        }
    }

    private fun validateData(name: String, number: String, village: String, state: String, city: String, pincode: String) {

        if(name.isEmpty() || number.isEmpty() || village.isEmpty() || state.isEmpty() || city.isEmpty() || pincode.isEmpty()){
            Toast.makeText(this,"Please fill the fields",Toast.LENGTH_SHORT).show()
        }else{
            storeData(village,state,city,pincode)
        }

    }

    private fun storeData(village: String, state: String, city: String, pincode: String) {

        val map= hashMapOf<String,Any>()
        map["village"]=village
        map["state"]=state
        map["city"]=city
        map["pincode"]=pincode

        Firebase.firestore.collection("users").document(preference.getString("number","")!!)
            .update(map).addOnSuccessListener {
                val b=Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost",totalCost)
                val intent=Intent(this,CheckoutActivity::class.java)
                intent.putExtras(b)


//                intent.putStringArrayListExtra("productIds",intent.getStringArrayExtra("productIds"))
//                intent.putExtra("totalCost",totalCost)
                startActivity(intent)


            }.addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadUserInfo() {


        Firebase.firestore.collection("users").document(preference.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userNumber"))
                binding.userVillage.setText(it.getString("village"))
                binding.userState.setText(it.getString("state"))
                binding.userCity.setText(it.getString("city"))
                binding.userPincode.setText(it.getString("pincode"))
            }.addOnFailureListener {

            }

    }
}