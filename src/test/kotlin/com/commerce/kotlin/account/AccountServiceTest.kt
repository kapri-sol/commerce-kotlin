package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.dto.UpdateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.AccountService
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import kotlin.IllegalStateException

@SpringBootTest
class AccountServiceTest(
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired
    private val accountService: AccountService
) {
    val faker = Faker()

    @DisplayName("계정을 생성한다.")
    @Test
    fun createAccount() {
        // given
        val createAccountDto = CreateAccountDto(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        // when
        val accountId = this.accountService.createAccount(createAccountDto)
        val account = this.accountRepository.findByIdOrNull(accountId)

        // then
        assertThat(accountId).isNotNull
        assertThat(account?.email).isEqualTo(createAccountDto.email)
        assertThat(account?.phoneNumber).isEqualTo(createAccountDto.phoneNumber)
    }

    @DisplayName("중복된 이메일로 계정을 생성하면 IllegalStateException 예외가 발생한다.")
    @Test
    fun createAccountByDuplicatedEmail() {
        // given
        val account = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        this.accountRepository.save(account)

        val createAccountDto = CreateAccountDto(
            email = account.email,
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )

        // when
        val createAccount = {
            this.accountService.createAccount(createAccountDto)
        }

        // then
        assertThrows<IllegalStateException> { createAccount() }
    }

    @DisplayName("중복된 전화번호로 계정을 생성하면 IllegalStateException 예외가 발생한다.")
    @Test
    fun createAccountByDuplicatedPhoneNumber() {
        // given
        val account = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        this.accountRepository.save(account)

        val createAccountDto = CreateAccountDto(
            email = faker.internet().emailAddress(),
            phoneNumber = account.phoneNumber,
            password = faker.internet().password()
        )

        // when
        val createAccount = {
            this.accountService.createAccount(createAccountDto)
        }

        // then
        assertThrows<IllegalStateException> { createAccount() }
    }

    @DisplayName("계정을 검색한다.")
    @Test
    fun findAccount() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)
        // when
        val findAccount = this.accountService.findAccount(account.id!!)
        // then
        assertThat(findAccount.id).isEqualTo(account.id)
        assertThat(findAccount.email).isEqualTo(account.email)
        assertThat(findAccount.phoneNumber).isEqualTo(account.phoneNumber)
    }

    @DisplayName("없는 계정을 검색하면 NotFoundException 예외가 발생한다.")
    @Test
    fun findNullAccount() {
        // given
        val accountId = 1000L;

        // when
        val findAccount = { this.accountService.findAccount(accountId) }

        // then
        assertThrows<NotFoundException> { findAccount() }
    }

    @DisplayName("계정을 수정한다.")
    @Test
    fun updateAccount() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)
        val updateAccountDto = UpdateAccountDto(
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )

        // when
        accountService.updateAccount(account.id!!, updateAccountDto)

        val updateAccount = this.accountRepository.findByIdOrNull(account.id)

        // then
        assertThat(updateAccount?.id).isEqualTo(account.id)
        assertThat(updateAccount?.password).isEqualTo(updateAccountDto.password)
        assertThat(updateAccount?.phoneNumber).isEqualTo(updateAccountDto.phoneNumber)
    }

    @DisplayName("없는 계정을 수정하면 NotFoundException 예외가 발생한다.")
    @Test
    fun updateNullAccount() {
        // given
        val accountId = 1000L;
        val updateAccountDto = UpdateAccountDto(
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )

        // when
        val updateAccount = { accountService.updateAccount(accountId, updateAccountDto) }

        // then
        assertThrows<NotFoundException> { updateAccount() }
    }

    @DisplayName("계정을 삭제한다.")
    @Test
    fun removeAccount() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)
        // when
        accountService.removeAccount(account.id!!)

        val removeAccount = this.accountRepository.findByIdOrNull(account.id)
        // then
        assertThat(removeAccount).isNull()
    }

    @DisplayName("없는 계정을 삭제한면 NotFoundException 예외가 발생한다.")
    @Test
    fun removeNullAccount() {
        // given
        val accountId = 1000L

        // when
        val removeAccount = { accountService.removeAccount(accountId) }

        // then
        assertThrows<NotFoundException>(removeAccount)
    }
}