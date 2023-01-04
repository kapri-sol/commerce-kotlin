package com.commerce.kotlin.repository;

import com.commerce.kotlin.entity.Seller
import org.springframework.data.jpa.repository.JpaRepository

interface SellerRepository : JpaRepository<Seller, Long> {
}