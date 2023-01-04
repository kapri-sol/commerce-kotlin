package com.commerce.kotlin.dto

data class UpdateProductDto(
    val name: String,
    val description: String,
    val increaseQuantityCount: Int?,
) {
}