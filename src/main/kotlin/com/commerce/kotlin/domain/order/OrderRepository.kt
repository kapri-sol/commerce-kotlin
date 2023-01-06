package com.commerce.kotlin.domain.order;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Long> {
    @Query("select o from Order o join o.orderItems i  join i.product where o.id = :id")
    fun findByIdWithOrderItem(id: Long): Order?

}