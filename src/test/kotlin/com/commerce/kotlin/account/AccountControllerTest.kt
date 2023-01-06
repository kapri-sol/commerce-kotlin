package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.dto.GetAccountResponse
import com.commerce.kotlin.domain.account.dto.PostAccountResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


//@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest(
    @Autowired
    private var mockMvc: MockMvc,
    @Autowired
    private val accountRepository: AccountRepository,
){

    private val faker = Faker()

    @AfterEach
    fun deleteAll() {
        this.accountRepository.deleteAll()
    }

    @Test
    fun postAccount() {

        val createAccountDto = CreateAccountDto(
            email = this.faker.internet().emailAddress(),
            phoneNumber = this.faker.phoneNumber().phoneNumber(),
            password = this.faker.internet().password()
        )

        val createAccountResponse = PostAccountResponse(id = 1L)

        this.mockMvc.perform(
            post("/accounts")
                .content(jacksonObjectMapper().writeValueAsString(createAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(createAccountResponse)))

    }

    @Test
    fun getAccount() {
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)

        val getAccountResponse = GetAccountResponse(
            email = createAccount.email,
            phoneNumber = createAccount.phoneNumber
        )

        this.mockMvc.perform(get("/accounts/${account.id}"))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getAccountResponse)))
    }
}