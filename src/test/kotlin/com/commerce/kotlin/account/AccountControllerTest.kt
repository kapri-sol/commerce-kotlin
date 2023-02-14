package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.dto.GetAccountResponse
import com.commerce.kotlin.domain.account.dto.PostAccountResponse
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val accountRepository: AccountRepository,
) {

    private val faker = Faker()

    @Test
    @DisplayName("계정을 생성한다.")
    fun createAccount() {

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
    @DisplayName("계정 정보를 조회한다.")
    @WithMockCustomUser(accountId = 1L)
    fun findMyAccount() {
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )

        this.accountRepository.save(createAccount)

        val getAccountResponse = GetAccountResponse(
            email = createAccount.email,
            phoneNumber = createAccount.phoneNumber
        )

        this.mockMvc.perform(get("/accounts/me"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getAccountResponse)))
    }
}