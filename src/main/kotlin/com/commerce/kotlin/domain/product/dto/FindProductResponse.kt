package com.commerce.kotlin.domain.product.dto

data class FindProductResponse(
    val name: String,
    val description: String,
    val price: Int,
    val stockQuantity: Int
)
