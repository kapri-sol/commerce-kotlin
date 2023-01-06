package com.commerce.kotlin.domain.order.dto

data class CreateOrderDto(
    val orderItems: List<CreateOrderItemDto>
) {
}