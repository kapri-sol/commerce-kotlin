package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateAccountDto
import com.commerce.kotlin.dto.UpdateAccountDto
import com.commerce.kotlin.entity.Account
import com.commerce.kotlin.repository.AccountRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class AccountServiceTest(
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired
    private val accountService: AccountService
)
{

    @Test
    fun clear() {
        this.accountRepository.deleteAll()
    }

    @Test
    fun createAccount() {
        // given
        val createAccountDto = CreateAccountDto(
            email = "a@a.com",
            phoneNumber = "010-1111-2222",
            password = "1111"
        );
        // when
        val accountId = this.accountService.createAccount(createAccountDto);
        val account = this.accountRepository.findByIdOrNull(accountId);

        // then
        assertThat(accountId).isNotNull;
        assertThat(account?.email).isEqualTo(createAccountDto.email);
        assertThat(account?.phoneNumber).isEqualTo(createAccountDto.phoneNumber);
    }

    @Test
    fun findAccount() {
        // given
        val createAccount = Account(
            email = "b@b.com",
            phoneNumber = "010-1111-3333",
            password = "2222");
        val account = this.accountRepository.save(createAccount);
        // when
        val findAccount = this.accountService.findAccount(account.id!!);
        // then
        assertThat(findAccount.id).isEqualTo(account.id);
        assertThat(findAccount.email).isEqualTo(account.email);
        assertThat(findAccount.phoneNumber).isEqualTo(account.phoneNumber);
    }

    @Test
    fun updateAccount() {
        // given
        val createAccount = Account(
            email = "c@c.com",
            phoneNumber = "010-1111-4444",
            password = "3333");
        val account = this.accountRepository.save(createAccount);
        val updateAccountDto = UpdateAccountDto(phoneNumber = "010-1111-5555", password = "4444");

        // when
        accountService.updateAccount(account.id!!, updateAccountDto);

        val updateAccount = this.accountRepository.findByIdOrNull(account.id);

        // then
        assertThat(updateAccount?.id).isEqualTo(account.id);
        assertThat(updateAccount?.password).isEqualTo(updateAccountDto.password);
        assertThat(updateAccount?.phoneNumber).isEqualTo(updateAccountDto.phoneNumber);
    }

    @Test
    fun removeAccount() {
        // given
        val createAccount = Account(
            email = "d@d.com",
            phoneNumber = "010-1111-6666",
            password = "5555");
        val account = this.accountRepository.save(createAccount);
        // when
        accountService.removeAccount(account.id!!);

        val removeAccount = this.accountRepository.findByIdOrNull(account.id);
        // then
        assertThat(removeAccount).isNull();
    }
}