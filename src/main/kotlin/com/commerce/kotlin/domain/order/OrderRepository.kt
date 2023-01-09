package com.commerce.kotlin.domain.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Long> {

    @Query("select distinct o from Order o join fetch o.orderItems oi where o.id = :id")
    fun findByIdWithOrderItem(id: Long): Order?

    @Query("select o from Order o join fetch o.orderItems oi join fetch oi.product p where o.id = :id")
    fun findByIdWithOrderItemAndProduct(id: Long): Order?

}