package com.commerce.kotlin.domain.auth

import com.commerce.kotlin.domain.account.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val accountRepository: AccountRepository
) {
    fun authenticate(email: String, password: String): Long? {
        val account = this.accountRepository.findByEmail(email) ?: return null
        return account.id
    }
}