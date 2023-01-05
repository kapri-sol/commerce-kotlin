package com.commerce.kotlin.repository;

import com.commerce.kotlin.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
}