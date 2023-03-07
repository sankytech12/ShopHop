package com.example.shophop.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shophop.R
import com.example.shophop.activity.AddressActivity
import com.example.shophop.activity.CategoryActivity
import com.example.shophop.adapter.CartAdapter
import com.example.shophop.databinding.FragmentCartBinding
import com.example.shophop.roomdb.AppDatabase
import com.example.shophop.roomdb.ProductModel


class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var adapter: CartAdapter
    private lateinit var list:ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(layoutInflater)

        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.cartRecycler.layoutManager = linearLayoutManager

        val dao=AppDatabase.getInstance(requireContext()).productDao()
        list=ArrayList()

        dao.getAllProducts().observe(requireActivity()){
            if (it != null) {
                Log.d("Data", it.toString())
                adapter = CartAdapter(requireContext(), it)
                if (binding.cartRecycler != null) {
                    Log.d("RecyclerView", "RecyclerView is not null")
                    binding.cartRecycler.adapter = adapter
                    Log.d("Adapter", "Adapter set")
                } else {
                    Log.d("RecyclerView", "RecyclerView is null")
                }
            }
            list.clear()
            for (data in it){
                list.add(data.productId)
            }
            totalCost(it)
        }
        //binding.cartRecycler.adapter=CartAdapter(requireContext(),ll!!)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.cartRecycler.adapter = null
    }

    private fun totalCost(data: List<ProductModel>?) {
        var totCost=0
        for(item in data!!){
            totCost+=item.productSp!!.toInt()
        }

        binding.textView12.text="Total items in Cart is :${data.size}"
        binding.textView13.text="Total cost is : $totCost"

        binding.checkOut.setOnClickListener {
            val intent= Intent(context, AddressActivity::class.java)
            val b=Bundle()
            b.putStringArrayList("productIds",list)
            b.putString("totalCost",totCost.toString())
           intent.putExtras(b)
            startActivity(intent)
        }

    }



}