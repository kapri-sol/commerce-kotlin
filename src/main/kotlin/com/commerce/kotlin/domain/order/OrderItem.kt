package com.commerce.kotlin.domain.order

import com.commerce.kotlin.domain.BaseEntity
import com.commerce.kotlin.domain.order.OrderItemStatus.*
import com.commerce.kotlin.domain.product.Product
import jakarta.persistence.*
import jakarta.persistence.FetchType.*

@Entity
class OrderItem(
    @OneToOne(optional= false, fetch = LAZY)
    val product: Product,
    val count: Int
): BaseEntity()
{
    @GeneratedValue
    @Id
    val id: Long? = null;

    private var status: OrderItemStatus = PENDING;

    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order? = null
        private set;

    fun order() {
        this.product.order(this.count);
    }

    fun setOrder(order: Order) {
            this.order = order;
    }

    fun cancel() {
        this.status = CANCELLED
    }

    fun confirm() {
        this.status = CONFIRMED
    }

    fun ship() {
        this.status = SHIPPED
    }

    fun delivery() {
        this.status = DELIVERED
    }
}

enum class OrderItemStatus {
    PENDING,
    CONFIRMED,
    SHIPPED ,
    DELIVERED,
    CANCELLED,
}
