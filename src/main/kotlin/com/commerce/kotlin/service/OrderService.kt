package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateOrderDto
import com.commerce.kotlin.entity.OrderItem
import com.commerce.kotlin.entity.Product
import com.commerce.kotlin.repository.OrderItemRepository
import com.commerce.kotlin.repository.OrderRepository
import com.commerce.kotlin.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val orderItemRepository: OrderItemRepository,
    val productRepository: ProductRepository
) {
    fun createOrder(createOrderDto: CreateOrderDto) {
        val orderItemsDto = createOrderDto.orderItems;
        val orderItems = orderItemsDto.map { OrderItem(
            product = productRepository.getReferenceById(it.productId),
            count = it.count
        )  }

    }

    fun findOrder() {

    }
}