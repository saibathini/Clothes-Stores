package com.cs.clothesstore.Models

data class Product(
    val category: String,
    val image: String,
    val name: String,
    val oldPrice: Double,
    val price: Double,
    val productId: String,
    val stock: Int,
    var isWishlisted: Boolean = false,
    var isAddedToCart: Boolean = false
)