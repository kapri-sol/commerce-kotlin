package com.commerce.kotlin.entity

import jakarta.persistence.*
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Where(clause = "deleted IS FALSE")
@Entity
class Account(
    email: String,
    phoneNumber: String,
    password: String,
): BaseEntity()
{
    @Id
    @GeneratedValue
    val id: Long? = null;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null
        private set;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    var seller: Seller? = null
        private set;

    var email = email
        private set;

    var phoneNumber = phoneNumber
        private set;

    var password = password
        private set;

    private var deleted: Boolean? = false;

    fun remove() {
        this.deleted = true;
    }

    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun updatePassword(password: String) {
        this.password = password
    }


    fun authenticate(password: String): Boolean {
        return this.password == password;
    }

    fun setCustomer(customer: Customer) {
        if (this.customer != null) {
            throw Error();
        }

        this.customer = customer;
    }

    fun setSeller(seller: Seller) {
        this.seller = seller;
    }
}