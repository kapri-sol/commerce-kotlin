package com.commerce.kotlin.repository;

import com.commerce.kotlin.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
}