package com.commerce.kotlin.customer

import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.customer.Customer
import com.commerce.kotlin.domain.customer.CustomerService
import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.CreateCustomerResponse
import com.commerce.kotlin.domain.customer.dto.FindCustomerResponse
import com.commerce.kotlin.domain.customer.dto.UpdateCustomerDto
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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerTest {
    @MockkBean
    private lateinit var accountRepository: AccountRepository

    @MockkBean
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val faker = Faker()

    private val initialCustomer: Customer = Customer(
        name = faker.name().fullName(),
        address = faker.address().fullAddress()
    )

    @Test
    @DisplayName("POST Customer")
    @WithMockCustomUser(accountId = 2L)
    fun postCustomer() {
        // given
        val account = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            name = faker.name().fullName(),
            password = faker.internet().password()
        )

        val createCustomerDto = CreateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        every {
            accountRepository.findByIdOrNull(2L)
        } returns account

        every {
            customerService.createCustomer(2L, any())
        } returns 2L

        val createCustomerResponse = CreateCustomerResponse(
            customerId = 2L
        )

        // when
        this.mockMvc.perform(
            post("/customers")
                .content(objectMapper.writeValueAsString(createCustomerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(createCustomerResponse)))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "customer/post-customers",
                    resourceDetails = ResourceSnippetParametersBuilder().tag("Customer")
                        .responseFields(
                            fieldWithPath("name").description("이름"),
                            fieldWithPath("address").description("주소")
                        )
                )
            )
    }

    @Test
    @DisplayName("GET Customer ME")
    @WithMockCustomUser(accountId = 1L, customerId = 1L)
    fun getCustomerMyself() {
        // given
        val getCustomerResponse = FindCustomerResponse(
            name = initialCustomer.name,
            address = initialCustomer.address
        )

        every { customerService.findCustomerById(1L) } returns initialCustomer

        // when
        this.mockMvc.perform(
            get("/customers/me")
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(getCustomerResponse)))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "customer/get-customers-me",
                    resourceDetails = ResourceSnippetParametersBuilder().tag("Customer").responseFields(
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("address").description("주소")
                    )
                )
            )
    }

    @Test
    @DisplayName("PATCH Customer")
    @WithMockCustomUser(accountId = 1L, customerId = 1L)
    fun patchCustomer() {
        // given
        val updateCustomerDto = UpdateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        every { customerService.updateCustomer(1L, any()) } returns Unit

        // when
        this.mockMvc.perform(
            patch("/customers/me")
                .content(objectMapper.writeValueAsString(updateCustomerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isNoContent)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "customer/get-customers-me",
                    resourceDetails = ResourceSnippetParametersBuilder().tag("Customer")
                        .responseFields(
                            fieldWithPath("name").description("이름"),
                            fieldWithPath("address").description("주소")
                        )
                    )
                )
    }

    @Test
    @DisplayName("DELETE Customer")
    @WithMockCustomUser(accountId = 1L, customerId = 1L)
    fun deleteCustomer() {
        // given
        every { customerService.removeCustomer(1L) } returns Unit

        /* when */
        this.mockMvc.perform(
            delete("/customers/me")
        )
            // then
            .andExpect(status().isNoContent)
            .andDo(
                MockMvcRestDocumentationWrapper.document("customer/delete-customers-me")
            )
    }
}