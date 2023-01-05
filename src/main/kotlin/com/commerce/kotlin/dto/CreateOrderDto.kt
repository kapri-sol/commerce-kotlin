package com.commerce.kotlin.dto

data class CreateOrderDto(
    val orderItems: MutableList<CreateOrderItemDto>
) {
}