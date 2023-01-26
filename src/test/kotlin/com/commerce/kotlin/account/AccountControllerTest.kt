package com.commerce.kotlin.account

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.dto.GetAccountResponse
import com.commerce.kotlin.domain.account.dto.PostAccountResponse
import com.commerce.kotlin.domain.auth.dto.LoginDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.reactive.function.client.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val testRestTemplate: TestRestTemplate,
    @Autowired
    private val accountRepository: AccountRepository,
) {

    private val faker = Faker()

    @AfterEach
    fun deleteAll() {
        this.accountRepository.deleteAll()
    }

    @Test
    @DisplayName("계정을 생성한다.")
    fun postAccount() {

        val createAccountDto = CreateAccountDto(
            email = this.faker.internet().emailAddress(),
            phoneNumber = this.faker.phoneNumber().phoneNumber(),
            password = this.faker.internet().password()
        )

        val createAccountResponse = PostAccountResponse(id = 1L)

        val response = this.mockMvc.perform(
            post("/accounts")
                .content(jacksonObjectMapper().writeValueAsString(createAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(createAccountResponse)))
    }

    @Test
    @DisplayName("계정 정보를 조회한다.")
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

        val sessionAttr = HashMap<String, Any>()
        account.id?.let { sessionAttr.put(SESSION_NAME, it) }

        this.mockMvc.perform(get("/accounts/me").sessionAttrs(sessionAttr))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getAccountResponse)))
    }

    @Test
    @DisplayName("로그인하고 계정 정보를 조회한다.")
    fun getAccountAfterLogin() {
        // given
        val account = Account(
            email = this.faker.internet().emailAddress(),
            phoneNumber = this.faker.phoneNumber().phoneNumber(),
            password = this.faker.internet().password()
        )
        this.accountRepository.save(account)

        val loginDto = LoginDto(
            email = account.email,
            password = account.password
        )

        // when
        val loginResponse = this.testRestTemplate.postForEntity(
            "/auth/login",
            HttpEntity(loginDto),
            String::class.java
        )
        val authCookie = loginResponse.headers["Set-Cookie"]?.get(0)

        val httpHeaders = HttpHeaders()
        httpHeaders.add(HttpHeaders.COOKIE, authCookie)

        val response = this.testRestTemplate.exchange(
            "/accounts/me",
            HttpMethod.GET,
            HttpEntity<String>(httpHeaders),
            String::class.java
        )

        val getAccountResponse: GetAccountResponse = jacksonObjectMapper().readValue(response.body, GetAccountResponse::class.java)

        assertThat(getAccountResponse.email).isEqualTo(account.email)
        assertThat(getAccountResponse.phoneNumber).isEqualTo(account.phoneNumber)
    }
}