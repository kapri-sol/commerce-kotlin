package com.commerce.kotlin.domain.order

import com.commerce.kotlin.common.entity.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @OneToMany(mappedBy = "order",  cascade = [CascadeType.ALL])
    val orderItems: List<OrderItem>
) : BaseEntity()
{
    @GeneratedValue
    @Id
    val id: Long? = null

    init {
        orderItems.forEach{
            it.setOrder(this)
            it.order()
        }
    }
}