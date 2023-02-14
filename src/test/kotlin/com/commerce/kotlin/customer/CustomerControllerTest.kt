package com.commerce.kotlin.customer

import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.customer.CustomerRepository
import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.PostCustomerResponse
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.databind.ObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val faker = Faker()

    @Test
    @DisplayName("POST Customer")
    @WithMockCustomUser
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

        // when
        val perform = this.mockMvc.perform(
            post("/customers")
                .content(objectMapper.writeValueAsString(createCustomerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isCreated)

        val postCustomerResponse = objectMapper.readValue(
            perform.andReturn().response.contentAsString,
            PostCustomerResponse::class.java
        )

        assertThat(postCustomerResponse.customerId).isNotNull
    }

//    @Test
//    @DisplayName("GET Customer ME")
//    @WithMockCustomUser(accountId = 1L, customerId = 1L)
//    fun getCustomerMyself() {
//        // given
//        val createAccount = Account(
//            email = faker.internet().emailAddress(),
//            phoneNumber = faker.phoneNumber().phoneNumber(),
//            password = faker.internet().password()
//        )
//        val createCustomer = Customer(
//            name = faker.name().fullName(),
//            address = faker.address().fullAddress()
//        )
//
//        createAccount.setCustomer(createCustomer)
//
//        val customer = this.customerRepository.save(createCustomer)
//        this.accountRepository.save(createAccount)
//
//        val getCustomerResponse = GetCustomerResponse(
//            name = customer.name,
//            address = customer.address
//        )
//
//        // when
//        this.mockMvc.perform(
//            get("/customers/me")
//        )
//            // then
//            .andExpect(status().isOk)
//            .andExpect(content().json(objectMapper.writeValueAsString(getCustomerResponse)))
//    }
//
//    @Test
//    @DisplayName("PATCH Customer")
//    @WithMockCustomUser(accountId = 1L, customerId = 1L)
//    fun patchCustomer() {
//        // given
//        val createAccount = Account(
//            email = faker.internet().emailAddress(),
//            phoneNumber = faker.phoneNumber().phoneNumber(),
//            password = faker.internet().password()
//        )
//        val createCustomer = Customer(
//            name = faker.name().fullName(),
//            address = faker.address().fullAddress()
//        )
//
//        createAccount.setCustomer(createCustomer)
//
//        val customer = this.customerRepository.save(createCustomer)
//        this.accountRepository.save(createAccount)
//
//        val updateCustomerDto = UpdateCustomerDto(
//            name = faker.name().fullName(),
//            address = faker.address().fullAddress()
//        )
//
//        // when
//        this.mockMvc.perform(
//            patch("/customers/me")
//                .content(objectMapper.writeValueAsString(updateCustomerDto))
//                .contentType(MediaType.APPLICATION_JSON)
//        )
//            // then
//            .andExpect(status().isNoContent)
//
//        val updateCustomer = this.customerRepository.findByIdOrNull(customer.id)
//
//        assertThat(updateCustomer).isNotNull
//        assertThat(updateCustomer?.name).isEqualTo(updateCustomerDto.name)
//        assertThat(updateCustomer?.address).isEqualTo(updateCustomerDto.address)
//    }
//
//    @Test
//    @DisplayName("DELETE Customer")
//    @WithMockCustomUser(accountId = 1L, customerId = 1L)
//    fun deleteCustomer() {
//        // given
//        val createAccount = Account(
//            email = faker.internet().emailAddress(),
//            phoneNumber = faker.phoneNumber().phoneNumber(),
//            password = faker.internet().password()
//        )
//        val createCustomer = Customer(
//            name = faker.name().fullName(),
//            address = faker.address().fullAddress()
//        )
//
//        createAccount.setCustomer(createCustomer)
//
//        val customer = this.customerRepository.save(createCustomer)
//        this.accountRepository.save(createAccount)
//
//        // when
//        this.mockMvc.perform(
//            delete("/customers/me")
//        )
//            // then
//            .andExpect(status().isNoContent)
//
//        val deletedCustomer = this.customerRepository.findByIdOrNull(customer.id)
//
//        assertThat(deletedCustomer).isNull()
//    }
}