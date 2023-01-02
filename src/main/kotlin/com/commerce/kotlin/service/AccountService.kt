package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateAccountDto
import com.commerce.kotlin.dto.UpdateAccountDto
import com.commerce.kotlin.entity.Account
import com.commerce.kotlin.repository.AccountRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Transactional
@Service
class AccountService (
    private  val accountRepository: AccountRepository
) {
    fun createAccount(createAccountDto: CreateAccountDto): Long {
        val createAccount = Account(
            email = createAccountDto.email,
            phoneNumber = createAccountDto.phoneNumber,
            password = createAccountDto.password
        )
        return this.accountRepository.save(createAccount).id ?: throw InternalError();
    }

    fun findAccount(accountId: Long): Account {
        return this.accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException()
    }

    fun updateAccount(accountId: Long, updateAccountDto: UpdateAccountDto) {
        val account = this.accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException()
        account.update(
            phoneNumber = updateAccountDto.phoneNumber,
            password = updateAccountDto.password
        )
    }

    fun removeAccount(accountId: Long) {
        val account = this.accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException()
        account.remove();
    }
}