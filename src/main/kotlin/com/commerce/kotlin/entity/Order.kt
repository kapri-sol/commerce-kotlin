package com.commerce.kotlin.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Order(
    @OneToMany
    val orderItems: MutableList<OrderItem> = ArrayList()
) : BaseEntity()
{
    @GeneratedValue
    @Id
    val id: Long? = null;

    init {
        orderItems.forEach { it.setOrder(this) }
    }

    fun takeOrderItems() {
        this.orderItems.forEach { it.order() }
    }
}