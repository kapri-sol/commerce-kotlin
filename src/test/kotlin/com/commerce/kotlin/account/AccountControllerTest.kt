package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountService
import com.commerce.kotlin.domain.account.dto.FindAccountResponse
import com.commerce.kotlin.domain.account.dto.CreateAccountResponse
import com.commerce.kotlin.util.WithMockCustomUser
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val faker = Faker()

    @Test
    @DisplayName("계정을 생성한다.")
    fun createAccount() {
        val createAccountDto = CreateAccountDto(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            name = faker.name().fullName(),
            password = faker.internet().password()
        )

        val createAccountResponse = CreateAccountResponse(id = 2L)

        every { accountService.createAccount(createAccountDto) }.returns(2L)

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.
            post("/accounts")
                .content(objectMapper.writeValueAsString(createAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(createAccountResponse)))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "account/post-accounts",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .tag("Post Account")
                        .requestFields(
                            fieldWithPath("email").description("이메일 주소"),
                            fieldWithPath("phoneNumber").description("휴대폰 번호"),
                            fieldWithPath("password").description("비밀번호")
                        ).responseFields(
                            fieldWithPath("id").description("계정 고유번호")
                        )
                )
            )
    }

    @Test
    @DisplayName("계정 정보를 조회한다.")
    @WithMockCustomUser(accountId = 1L)
    fun findMyAccount() {
        val account = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            name = faker.name().fullName(),
            password = faker.internet().password()
        )

        every { accountService.findAccountById(1L) }.returns(account)

        val getAccountResponse = FindAccountResponse(
            email = account.email,
            name = faker.name().fullName(),
            phoneNumber = account.phoneNumber
        )

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/accounts/me")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(getAccountResponse)))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "get-accounts",
                    resourceDetails = ResourceSnippetParametersBuilder().responseFields(
                        fieldWithPath("email").description("이메일 주소"),
                        fieldWithPath("phoneNumber").description("휴대폰 번호"),
                    )
                )
            )
    }
}