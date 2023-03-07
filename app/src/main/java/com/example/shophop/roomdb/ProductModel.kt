package com.example.shophop.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    @NotNull
    val productId:String,
    @ColumnInfo("productName")
    val productName:String?="",
    @ColumnInfo("productImage")
    val productImage:String?="",
    @ColumnInfo("productSp")
    val productSp:String?="",
)
