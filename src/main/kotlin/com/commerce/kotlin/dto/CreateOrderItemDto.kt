package com.commerce.kotlin.dto

data class CreateOrderItemDto(
    val productId: Long,
    val count: Int
) {
}