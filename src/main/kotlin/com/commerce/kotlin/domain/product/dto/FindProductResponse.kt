package com.commerce.kotlin.domain.product.dto

data class FindProductResponse(
    val id: Long,
    val title: String,
    val description: String,
    val image: String,
    val price: Int,
    val stockQuantity: Int
)
