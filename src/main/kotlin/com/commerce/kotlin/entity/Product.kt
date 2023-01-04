package com.commerce.kotlin.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Where(clause = "deleted IS FALSE")
@Entity
class Product(
    name: String,
    description: String,
    price: Double,
    stockQuantity: Int
) {
    @GeneratedValue
    @Id
    val id: Long? = null;

    @ManyToOne
    @JoinColumn
    var seller: Seller? = null
        private set;

    var name: String = name
        private set;

    var description: String = description
        private set;

    var price: Double = price
        private set;

    var stockQuantity: Int = stockQuantity
        private set;

    private var deleted: Boolean = false;

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now();

    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now();

    init {
        if (stockQuantity <= 0) {
            throw Error();
        }
    }

    fun setSeller(seller: Seller) {
        if (this.seller != null) {
            throw Error()
        }
        this.seller = seller;
    }

    fun remove() {
        this.deleted = true;
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeDescription(description: String) {
        this.description = description;
    }

    fun increaseQuantity(increaseCount: Int) {
        if (increaseCount <= 0) {
           throw Error();
        }
        this.stockQuantity += increaseCount
    }

    fun order(orderCount: Int) {
        if (this.stockQuantity < orderCount) {
           throw Error();
        }
        this.stockQuantity -= orderCount;
    }
}