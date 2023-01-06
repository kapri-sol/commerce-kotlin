package com.commerce.kotlin.domain.order

import com.commerce.kotlin.domain.order.dto.CreateOrderDto
import com.commerce.kotlin.domain.product.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class OrderService(
    val orderRepository: OrderRepository,
    val productRepository: ProductRepository
) {
    fun order(createOrderDto: CreateOrderDto): Long {
        val orderItemsDto = createOrderDto.orderItems
        val products = productRepository.findAllById(orderItemsDto.map { it.productId }).associateBy { it.id }

        val orderItems = orderItemsDto.map { OrderItem(
            product = products.getValue(it.productId),
            count = it.count
        )  }

        val order = Order(orderItems)

        return orderRepository.save(order).id!!
    }

    fun findOrder() {

    }
}