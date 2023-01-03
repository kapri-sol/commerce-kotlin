package com.commerce.kotlin.service

import com.commerce.kotlin.repository.AccountRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val accountRepository: AccountRepository
) {
    fun authenticate(email: String, password: String): Long? {
        val account = this.accountRepository.findByEmail(email) ?: return null;
        return account.id
    }
}