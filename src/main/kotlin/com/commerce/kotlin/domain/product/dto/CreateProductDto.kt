package com.commerce.kotlin.domain.product.dto

data class CreateProductDto(
    val name: String,
    val description: String,
    val price: Int,
    val stockQuantity: Int,
    val image: String
)