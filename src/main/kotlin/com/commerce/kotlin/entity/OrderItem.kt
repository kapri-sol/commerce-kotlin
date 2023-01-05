package com.commerce.kotlin.entity

import jakarta.persistence.*

@Entity
class OrderItem(
    @OneToOne(fetch = FetchType.LAZY)
    val product: Product,
    val count: Int
): BaseEntity()
{
    @GeneratedValue
    @Id
    val id: Long? = null;

    var status: OrderItemStatus = OrderItemStatus.PENDING;

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
        this.status = OrderItemStatus.CANCELLED
    }

    fun confirm() {
        this.status = OrderItemStatus.CONFIRMED
    }

    fun ship() {
        this.status = OrderItemStatus.SHIPPED
    }

    fun delivery() {
        this.status = OrderItemStatus.DELIVERED
    }
}

enum class OrderItemStatus {
    PENDING,
    CONFIRMED,
    SHIPPED ,
    DELIVERED,
    CANCELLED,
}
