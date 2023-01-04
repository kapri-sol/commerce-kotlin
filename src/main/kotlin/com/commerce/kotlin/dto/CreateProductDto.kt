package com.commerce.kotlin.dto

data class CreateProductDto(
    val name: String,
    val description: String,
    val price: Double,
    val stockQuantity: Int
) {
}