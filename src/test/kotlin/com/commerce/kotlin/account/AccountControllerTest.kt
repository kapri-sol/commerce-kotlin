package com.commerce.kotlin.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountController
import com.commerce.kotlin.domain.account.AccountService
import com.commerce.kotlin.domain.account.dto.GetAccountResponse
import com.commerce.kotlin.domain.account.dto.PostAccountResponse
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var accountService: AccountService

    @InjectMocks
    private lateinit var accountController: AccountController

    private val faker = Faker()

    @BeforeEach
    fun init() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build()
    }

    @Test
    @DisplayName("계정을 생성한다.")
    fun createAccount() {

        val createAccountDto = CreateAccountDto(
            email = this.faker.internet().emailAddress(),
            phoneNumber = this.faker.phoneNumber().phoneNumber(),
            password = this.faker.internet().password()
        )

        val createAccountResponse = PostAccountResponse(id = 2L)

        doReturn(2L).`when`(accountService).createAccount(createAccountDto)

        this.mockMvc.perform(
            post("/accounts")
                .content(jacksonObjectMapper().writeValueAsString(createAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(createAccountResponse)))
            .andDo(
                document("account", requestFields(
                        fieldWithPath("email").description("이메일 주소"),
                        fieldWithPath("phoneNumber").description("휴대폰 번호"),
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("id").description("계정 고유번호")
                    )
                )
            )
            .andDo(print())
    }

    @Test
    @DisplayName("계정 정보를 조회한다.")
    @WithMockCustomUser(accountId = 2L)
    fun findMyAccount() {
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )

        val proxyAccount = ProxyAccount(
            id = 2L,
            email = createAccount.email,
            phoneNumber = createAccount.phoneNumber,
            password = createAccount.password
        )

        val getAccountResponse = GetAccountResponse(
            email = createAccount.email,
            phoneNumber = createAccount.phoneNumber
        )

        doReturn(proxyAccount).`when`(accountService).findAccount(2L)

        this.mockMvc.perform(get("/accounts/me"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getAccountResponse)))
    }

    companion object {
        class ProxyAccount(
            override val id: Long,
            email: String,
            phoneNumber: String,
            password: String
        ) : Account(email = email, phoneNumber = phoneNumber, password = password)
    }
}