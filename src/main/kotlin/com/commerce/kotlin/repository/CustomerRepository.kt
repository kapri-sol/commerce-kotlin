package com.commerce.kotlin.repository;

import com.commerce.kotlin.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long> {
}