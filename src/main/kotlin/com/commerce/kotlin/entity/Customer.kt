package com.commerce.kotlin.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
class Customer(
    name: String,
    address: String,
    @OneToOne val account: Account
) {

    @Id
    @GeneratedValue
    val id: Long? = null;


    var name = name
        private set;

    var address = address
        private set;

    private var deleted: Boolean = false;

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now();

    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now();

    fun remove() {
        this.deleted = true;
    }
}