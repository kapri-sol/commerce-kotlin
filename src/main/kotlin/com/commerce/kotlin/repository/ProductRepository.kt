package com.commerce.kotlin.repository;

import com.commerce.kotlin.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
}