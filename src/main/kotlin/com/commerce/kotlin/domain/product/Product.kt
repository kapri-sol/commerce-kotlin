package com.commerce.kotlin.domain.product

import com.commerce.kotlin.domain.BaseEntity
import com.commerce.kotlin.domain.seller.Seller
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import org.hibernate.annotations.Where

@Where(clause = "deleted IS FALSE")
@Entity
class Product(
    name: String,
    description: String,
    price: Double,
    stockQuantity: Int
) : BaseEntity()
{
    @GeneratedValue
    @Id
    val id: Long? = null

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    var seller: Seller? = null
        private set

    var name: String = name
        private set

    var description: String = description
        private set

    var price: Double = price
        private set

    var stockQuantity: Int = stockQuantity
        private set

    private var deleted: Boolean = false

    init {
        if (stockQuantity <= 0) {
            throw Error()
        }
    }

    fun setSeller(seller: Seller) {
        if (this.seller != null) {
            throw Error()
        }
        this.seller = seller
    }

    fun remove() {
        this.deleted = true
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeDescription(description: String) {
        this.description = description
    }

    fun increaseQuantity(increaseCount: Int) {
        if (increaseCount <= 0) {
           throw Error()
        }
        this.stockQuantity += increaseCount
    }

    fun order(orderCount: Int) {
        if (this.stockQuantity < orderCount) {
           throw Error()
        }
        this.stockQuantity -= orderCount
    }
}