package com.commerce.kotlin.domain.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.dto.UpdateAccountDto
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Transactional
@Service
class AccountService (
    private  val accountRepository: AccountRepository
) {
    private fun validateEmailDuplicated(email: String) {
        val account = this.accountRepository.findByEmail(email)

        if (account !== null) {
            throw IllegalStateException()
        }
    }

    private fun validatePhoneNumberDuplicated(phoneNumber: String) {
        val account = this.accountRepository.findByPhoneNumber(phoneNumber);

        if (account !== null) {
            throw IllegalStateException()
        }
    }

    fun createAccount(createAccountDto: CreateAccountDto): Long {
        validateEmailDuplicated(createAccountDto.email)
        validatePhoneNumberDuplicated(createAccountDto.phoneNumber)

        val createAccount = Account(
            email = createAccountDto.email,
            phoneNumber = createAccountDto.phoneNumber,
            password = createAccountDto.password
        )
        return this.accountRepository.save(createAccount).id ?: throw InternalError()
    }

    fun findAccount(accountId: Long): Account {
        return this.accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException()
    }

    fun updateAccount(accountId: Long, updateAccountDto: UpdateAccountDto) {
        val account = this.accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException()
        account.updatePhoneNumber(updateAccountDto.phoneNumber!!)
        account.updatePassword(updateAccountDto.password!!)
    }

    fun removeAccount(accountId: Long) {
        val account = this.accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException()
        account.remove()
    }
}