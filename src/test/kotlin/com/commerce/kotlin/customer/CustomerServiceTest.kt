package com.commerce.kotlin.customer

import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.UpdateCustomerDto
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.customer.Customer
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.customer.CustomerService
import com.commerce.kotlin.domain.customer.CustomerRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull


@SpringBootTest
class CustomerServiceTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val customerRepository: CustomerRepository,
    @Autowired private val customerService: CustomerService
) {

    private val faker = Faker()

    fun generateAccount(): Account {
       val account = Account(
           email = faker.internet().emailAddress(),
           phoneNumber = faker.phoneNumber().phoneNumber(),
           password = faker.internet().password()
       )
        return this.accountRepository.save(account)
    }

    fun generateCustomer(): Customer {
        val customer = Customer(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
        return this.customerRepository.save(customer)
    }

    @DisplayName("고객을 생성한다.")
    @Test
    fun createCustomer() {
        // given
        val account = this.generateAccount()

        val createCustomerDto = CreateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        //when
        val customerId = this.customerService.createCustomer(account.id!!, createCustomerDto)
        val customer = this.customerRepository.findByIdOrNull(customerId)

        // then
        assertThat(customer?.id).isEqualTo(customerId)
        assertThat(customer?.name).isEqualTo(createCustomerDto.name)
        assertThat(customer?.address).isEqualTo(createCustomerDto.address)
    }

    @Test
    fun findCustomer() {
        // given
        val customer = generateCustomer()
        // when
        val findCustomer = this.customerService.findCustomer(customer.id!!)
        // then
        assertThat(customer.id).isEqualTo(findCustomer.id)
        assertThat(customer.name).isEqualTo(findCustomer.name)
        assertThat(customer.address).isEqualTo(findCustomer.address)
    }

    @Test
    fun updateCustomer() {
        // given
        val customer = generateCustomer()
        val updateCustomerDto = UpdateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        // when
        this.customerService.updateCustomer(customer.id!!, updateCustomerDto)
        val findCustomer = this.customerRepository.findByIdOrNull(customer.id!!)

        // then
        assertThat(findCustomer?.id).isEqualTo(customer.id)
        assertThat(findCustomer?.name).isEqualTo(updateCustomerDto.name)
        assertThat(findCustomer?.address).isEqualTo(updateCustomerDto.address)
    }

    @Test
    fun removeCustomer() {
        // given
        val customer = generateCustomer()
        // when
        this.customerService.removeCustomer(customer.id!!)
        val findCustomer = this.customerRepository.findByIdOrNull(customer.id!!)
        // then
        assertThat(findCustomer).isNull()
    }


}