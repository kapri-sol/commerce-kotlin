package com.commerce.kotlin.auth

import com.commerce.kotlin.domain.auth.dto.LoginDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val accountRepository: AccountRepository
) {

    private val faker = Faker()

    @Test
    fun login() {
        // given
        val email = faker.internet().emailAddress()
        val password = faker.internet().password()
        val createAccount = Account(
            email = email,
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = password,
        )
        val account = accountRepository.save(createAccount)
        val loginDto = LoginDto(
            email = email,
            password = password
        )

        //when
        this.mockMvc.perform(
            post("/auth/login")
            .content(jacksonObjectMapper().writeValueAsString(loginDto))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
//            .andExpect(cookie().exists(SESSION_NAME))
    }

    @Test
    fun logout() {
    }
}