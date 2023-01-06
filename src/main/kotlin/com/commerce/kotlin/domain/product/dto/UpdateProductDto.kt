package com.commerce.kotlin.domain.product.dto

data class UpdateProductDto(
    val name: String,
    val description: String,
    val increaseQuantityCount: Int?,
)