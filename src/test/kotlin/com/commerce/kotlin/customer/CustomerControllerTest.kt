package com.commerce.kotlin.customer

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.customer.Customer
import com.commerce.kotlin.domain.customer.CustomerRepository
import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.GetCustomerResponse
import com.commerce.kotlin.domain.customer.dto.PostCustomerResponse
import com.commerce.kotlin.domain.customer.dto.UpdateCustomerDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val customerRepository: CustomerRepository,
    @Autowired private val mockMvc: MockMvc
) {
    val faker = Faker()

    @Test
    @DisplayName("POST Customer")
    fun postCustomer() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)

        val createCustomerDto = CreateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = account.id!!

        val postCustomerResponse = PostCustomerResponse(
            customerId = 1L
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/customers")
                .sessionAttrs(sessionAttr)
                .content(jacksonObjectMapper().writeValueAsString(createCustomerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isCreated)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(postCustomerResponse)))
    }

    @Test
    @DisplayName("GET Customer ME")
    fun getCustomerMyself() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val createCustomer = Customer(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        createAccount.setCustomer(createCustomer)

        val customer = this.customerRepository.save(createCustomer)
        val account = this.accountRepository.save(createAccount)

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(
            accountId = account.id!!,
            customerId = customer.id!!
        )

        val getCustomerResponse = GetCustomerResponse(
            name = customer.name,
            address = customer.address
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/customers/me")
                .sessionAttrs(sessionAttr)
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getCustomerResponse)))
    }

    @Test
    @DisplayName("PATCH Customer")
    fun patchCustomer() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val createCustomer = Customer(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        createAccount.setCustomer(createCustomer)

        val customer = this.customerRepository.save(createCustomer)
        val account = this.accountRepository.save(createAccount)

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(
            accountId = account.id!!,
            customerId = customer.id!!
        )

        val updateCustomerDto = UpdateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.patch("/customers/me")
                .sessionAttrs(sessionAttr)
                .content(jacksonObjectMapper().writeValueAsString(updateCustomerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isNoContent)

        val updateCustomer = this.customerRepository.findByIdOrNull(customer.id)

        Assertions.assertThat(updateCustomer).isNotNull
        Assertions.assertThat(updateCustomer?.name).isEqualTo(updateCustomerDto.name)
        Assertions.assertThat(updateCustomer?.address).isEqualTo(updateCustomerDto.address)
    }

    @Test
    @DisplayName("DELETE Customer")
    fun deleteCustomer() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val createCustomer = Customer(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        createAccount.setCustomer(createCustomer)

        val customer = this.customerRepository.save(createCustomer)
        val account = this.accountRepository.save(createAccount)

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(
            accountId = account.id!!,
            customerId = customer.id!!
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.delete("/customers/me")
                .sessionAttrs(sessionAttr)
        )
            // then
            .andExpect(status().isNoContent)

        val deletedCustomer = this.customerRepository.findByIdOrNull(customer.id)

        Assertions.assertThat(deletedCustomer).isNull()
    }
}