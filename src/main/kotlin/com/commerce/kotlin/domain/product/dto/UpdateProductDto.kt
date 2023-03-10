package com.commerce.kotlin.domain.product.dto

data class UpdateProductDto(
    val name: String,
    val description: String,
    val image: String,
    val category: String,
    val price: Int,
    val increaseQuantityCount: Int?,
)