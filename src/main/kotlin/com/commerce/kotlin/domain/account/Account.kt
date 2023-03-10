package com.commerce.kotlin.domain.account

import com.commerce.kotlin.common.entity.BaseEntity
import com.commerce.kotlin.domain.customer.Customer
import com.commerce.kotlin.domain.seller.Seller
import jakarta.persistence.*
import jakarta.persistence.FetchType.*
import org.hibernate.annotations.Where

@Where(clause = "deleted IS FALSE")
@Entity
class Account(
    email: String,
    phoneNumber: String,
    name: String,
    password: String,
) : BaseEntity() {
    @Id
    @GeneratedValue
    val id: Long? = null

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null
        private set

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    var seller: Seller? = null
        private set

    @Column(unique = true)
    var email = email
        private set

    var phoneNumber = phoneNumber
        private set

    var name = name
        private set

    var password = password
        private set

    var deleted: Boolean? = false

    fun remove() {
        this.deleted = true
    }

    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updatePassword(password: String) {
        this.password = password
    }


    fun authenticate(password: String): Boolean {
        return this.password == password
    }

    fun changeCustomer(customer: Customer) {
        if (this.customer != null) {
            throw Error()
        }

        this.customer = customer
    }

    fun changeSeller(seller: Seller) {
        this.seller = seller
    }
}