package com.commerce.kotlin.domain.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?

    fun findByPhoneNumber(phoneNumber: String): Account?

    @Query("select ac from Account ac join fetch Customer cu where ac.id = :id")
    fun findByIdWithCustomer(id : Long): Account?
}