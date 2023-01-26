package com.commerce.kotlin.auth

import com.commerce.kotlin.domain.auth.dto.LoginDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.dto.GetAccountResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest(
    @Autowired
    private val testRestTemplate: TestRestTemplate,
    @Autowired
    private val accountRepository: AccountRepository
) {
    private val faker = Faker()

    @Test
    @DisplayName("로그인하면 쿠키를 얻는다.")
    fun login() {
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
        val response = this.testRestTemplate.postForEntity(
            "/auth/login",
            HttpEntity(loginDto),
            String::class.java
        )
        val authCookie = response.headers["Set-Cookie"]?.find { it.contains("my-cookie") }

        // then
        Assertions.assertThat(authCookie).isNotEmpty
    }

    @Test
    @DisplayName("로그아웃")
    fun logout() {
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

        val getAccountResponse = this.testRestTemplate.exchange(
            "/accounts/me",
            HttpMethod.GET,
            HttpEntity<String>(httpHeaders),
            String::class.java
        )

        val logOutResponse = this.testRestTemplate.exchange(
            "/auth/logout",
            HttpMethod.POST,
            HttpEntity<String>(httpHeaders),
            String::class.java
        )

        val getAccountResponseAfterLogOut = this.testRestTemplate.exchange(
            "/accounts/me",
            HttpMethod.GET,
            HttpEntity<String>(httpHeaders),
            String::class.java
        )

        // then
        Assertions.assertThat(logOutResponse.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(getAccountResponse.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(getAccountResponseAfterLogOut.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}