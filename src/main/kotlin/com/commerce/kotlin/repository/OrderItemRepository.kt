package com.commerce.kotlin.repository;

import com.commerce.kotlin.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
}