package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.account.dto.FindAccountResponse
import com.commerce.kotlin.domain.account.dto.CreateAccountResponse
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.databind.ObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("계정을 생성한다.")
    fun createAccount() {

        val createAccountDto = CreateAccountDto(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )

        val createAccountResponse = CreateAccountResponse(id = 2L)

        this.mockMvc.perform(
            post("/accounts")
                .content(objectMapper.writeValueAsString(createAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(createAccountResponse)))
            .andDo(
                document(
                    "account", requestFields(
                        fieldWithPath("email").description("이메일 주소"),
                        fieldWithPath("phoneNumber").description("휴대폰 번호"),
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("id").description("계정 고유번호")
                    )
                )
            )
    }

    @Test
    @DisplayName("계정 정보를 조회한다.")
    @WithMockCustomUser(accountId = 1L)
    fun findMyAccount() {
        val getAccountResponse = FindAccountResponse(
            email = initialAccount.email,
            phoneNumber = initialAccount.phoneNumber
        )

        this.mockMvc.perform(get("/accounts/me"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(getAccountResponse)))
            .andDo(
                document(
                    "account", responseFields(
                        fieldWithPath("email").description("이메일 주소"),
                        fieldWithPath("phoneNumber").description("휴대폰 번호"),
                    )
                )
            )
    }

    companion object {
        lateinit var initialAccount: Account
        val faker = Faker()

        @JvmStatic
        @BeforeAll
        fun setup(@Autowired accountRepository: AccountRepository): Unit {
            initialAccount = accountRepository.save(
                Account(
                    email = faker.internet().emailAddress(),
                    phoneNumber = faker.phoneNumber().phoneNumber(),
                    password = faker.internet().password()
                )
            )
        }

    }
}