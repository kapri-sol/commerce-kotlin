package com.commerce.kotlin.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {
    @Query("select p from Product p join fetch p.seller s where p.id = :id")
    fun findByIdWithSeller(id: Long): Product?
}