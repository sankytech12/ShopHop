package com.example.shophop.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.shophop.MainActivity
import com.example.shophop.R
import com.example.shophop.roomdb.AppDatabase
import com.example.shophop.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val checkout=Checkout()
        checkout.setKeyID("rzp_test_deTONJhwSZzH53");

        val price=intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name","ShopHop")
            options.put("description","Best E-Commerce App")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount",(price!!.toInt()*100))//pass amount in currency subunits
            checkout.open(this,options)

        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()

        uploadData()
    }

    private fun uploadData() {
        val id=intent.getStringArrayListExtra("productIds")
        for(currId in id!!){
            fetchData(currId)
        }
    }


    private fun fetchData(currId: String?) {
        val dao=AppDatabase.getInstance(this).productDao()
            Firebase.firestore.collection("products")
                .document(currId!!).get().addOnSuccessListener {

                    lifecycleScope.launch(Dispatchers.IO){
                        dao.deleteProduct(ProductModel(currId))
                    }

                    saveData(it.getString("productName"),
                    it.getString("productSp"),
                    currId)
                    
                }
    }

    private fun saveData(name: String?, sp: String?, currId: String) {
        val preferences=this.getSharedPreferences("user", MODE_PRIVATE)

        val data= hashMapOf<String,Any>()
        data["name"]=name!!
        data["price"]=sp!!
        data["productId"]=currId!!
        data["status"]="Ordered"
        data["userId"]=preferences.getString("number","")!!

        val firestore=Firebase.firestore.collection("allOrders")
        val key=firestore.document().id
        data["orderId"]=key

        firestore.add(data).addOnSuccessListener {
            Toast.makeText(this,"Order Placed",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Error",Toast.LENGTH_SHORT).show()
    }
}