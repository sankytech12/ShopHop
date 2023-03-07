package com.example.shophop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shophop.R
import com.example.shophop.activity.CategoryActivity
import com.example.shophop.databinding.LayoutCategoryItemBinding
import com.example.shophop.model.AddProductModel
import com.example.shophop.model.CategoryModel


class CategoryAdapter(var context: Context, var list: ArrayList<CategoryModel>): Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View): ViewHolder(view){
        var binding=LayoutCategoryItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textView.text=list[position].cate
        Glide.with(context).load(list[position].img).into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            val intent=Intent(context,CategoryActivity::class.java)
            intent.putExtra("cat",list[position].cate)
            context.startActivity(intent)
        }
    }
}