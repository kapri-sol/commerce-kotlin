package com.commerce.kotlin.domain.order.dto

data class CreateOrderItemDto(
    val productId: Long,
    val count: Int
) {
}