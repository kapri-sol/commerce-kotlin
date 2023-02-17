package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.dto.UpdateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.AccountService
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import kotlin.IllegalStateException

@Transactional
@SpringBootTest
class AccountServiceTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountService: AccountService

    companion object {
        lateinit var initialAccount: Account

        val faker = Faker()

        @JvmStatic
        @BeforeAll
        fun init(@Autowired accountRepository: AccountRepository): Unit {
            initialAccount = accountRepository.save(
                Account(
                    email = faker.internet().emailAddress(),
                    phoneNumber = faker.phoneNumber().phoneNumber(),
                    password = faker.internet().password()
                )
            )
        }
    }

    @Test
    @DisplayName("계정을 생성한다.")
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

    @Test
    @DisplayName("중복된 이메일로 계정을 생성하면 IllegalStateException 예외가 발생한다.")
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

    @Test
    @DisplayName("중복된 전화번호로 계정을 생성하면 IllegalStateException 예외가 발생한다.")
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

    @Test
    @DisplayName("계정을 검색한다.")
    fun findAccount() {
        // given
        val account = initialAccount
        // when
        val findAccount = this.accountService.findAccountById(account.id!!)
        // then
        assertThat(findAccount.id).isEqualTo(account.id)
        assertThat(findAccount.email).isEqualTo(account.email)
        assertThat(findAccount.phoneNumber).isEqualTo(account.phoneNumber)
    }

    @Test
    @DisplayName("없는 계정을 검색하면 NotFoundException 예외가 발생한다.")
    fun findNullAccount() {
        // given
        val accountId = 1000L;

        // when
        val findAccount = { this.accountService.findAccountById(accountId) }

        // then
        assertThrows<NotFoundException> { findAccount() }
    }

    @Test
    @DisplayName("계정을 수정한다.")
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

    @Test
    @DisplayName("없는 계정을 수정하면 NotFoundException 예외가 발생한다.")
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

    @Test
    @DisplayName("계정을 삭제한다.")
    fun removeAccount() {
        // given
        val account = initialAccount

        // when
        accountService.removeAccount(account.id!!)

        val removeAccount = this.accountRepository.findByIdOrNull(account.id)

        // then
        assertThat(removeAccount?.deleted).isTrue
    }

    @Test
    @DisplayName("없는 계정을 삭제한면 NotFoundException 예외가 발생한다.")
    fun removeNullAccount() {
        // given
        val accountId = 1000L

        // when
        val removeAccount = { accountService.removeAccount(accountId) }

        // then
        assertThrows<NotFoundException>(removeAccount)
    }
}