package com.commerce.kotlin.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
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
) {
    @Id
    @GeneratedValue
    val id: Long? = null;

    @OneToOne
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null
        private set;

    var email = email
        private set;

    var phoneNumber = phoneNumber
        private set;

    var password = password
        private set;

    private var deleted: Boolean? = false;

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now();

    @LastModifiedDate
    val updateAt: LocalDateTime = LocalDateTime.now();

    fun remove() {
        this.deleted = true;
    }

    fun update(phoneNumber: String?, password: String?) {
        if( phoneNumber !== null) {
            this.phoneNumber = phoneNumber;
        }

        if(password !== null) {
            this.password = password;
        }
    }

    fun authenticate(password: String): Boolean {
        return this.password == password;
    }

    fun setCustomer(customer: Customer) {
        this.customer = customer;
    }
}