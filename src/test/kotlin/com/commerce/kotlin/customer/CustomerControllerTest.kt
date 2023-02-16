package com.commerce.kotlin.customer

import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.customer.Customer
import com.commerce.kotlin.domain.customer.CustomerRepository
import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.CreateCustomerResponse
import com.commerce.kotlin.domain.customer.dto.FindCustomerResponse
import com.commerce.kotlin.domain.customer.dto.UpdateCustomerDto
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.databind.ObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@Disabled
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class CustomerControllerTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("POST Customer")
    @WithMockCustomUser(accountId = 2L)
    fun postCustomer() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        this.accountRepository.save(createAccount)

        val createCustomerDto = CreateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

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
                document(
                    "customer", requestFields(
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

        // when
        this.mockMvc.perform(
            get("/customers/me")
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(getCustomerResponse)))
            .andDo(
                document(
                    "customer", responseFields(
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
        val customerId = initialCustomer.id
        val updateCustomerDto = UpdateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        // when
        this.mockMvc.perform(
            patch("/customers/me")
                .content(objectMapper.writeValueAsString(updateCustomerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isNoContent)
            .andDo(
                document("customer", requestFields(
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("address").description("주소")
                    )
                )
            )


        val updateCustomer = this.customerRepository.findByIdOrNull(customerId)

        assertThat(updateCustomer).isNotNull
        assertThat(updateCustomer?.name).isEqualTo(updateCustomerDto.name)
        assertThat(updateCustomer?.address).isEqualTo(updateCustomerDto.address)
    }

    @Test
    @DisplayName("DELETE Customer")
    @WithMockCustomUser(accountId = 1L, customerId = 1L)
    fun deleteCustomer() {
        // given
        val customerId = initialCustomer.id

        /* when */
        this.mockMvc.perform(
            delete("/customers/me")
        )
            // then
            .andExpect(status().isNoContent)
            .andDo(
                document("customer")
            )

        val deletedCustomer = this.customerRepository.findByIdOrNull(customerId)

        assertThat(deletedCustomer?.deleted).isTrue
    }

    companion object {
        val faker = Faker()
        private lateinit var initialAccount: Account
        lateinit var initialCustomer: Customer

        @JvmStatic
        @BeforeAll
        fun init(
            @Autowired accountRepository: AccountRepository,
            @Autowired customerRepository: CustomerRepository
        ): Unit {
            initialAccount = accountRepository.save(
                Account(
                    email = faker.internet().emailAddress(),
                    phoneNumber = faker.phoneNumber().phoneNumber(),
                    password = faker.internet().password()
                )
            )

            initialCustomer = customerRepository.save(
                Customer(
                    name = faker.name().fullName(),
                    address = faker.address().fullAddress()
                )
            )

            initialAccount.changeCustomer(initialCustomer)
        }
    }
}