package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateCustomerDto
import com.commerce.kotlin.entity.Account
import com.commerce.kotlin.repository.AccountRepository
import com.commerce.kotlin.repository.CustomerRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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

    @Test
    fun createCustomer() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount);

        val createCustomerDto = CreateCustomerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        //when
        val customerId = this.customerService.createCustomer(account.id!!, createCustomerDto);
        val customer = this.customerRepository.findByIdOrNull(customerId);

        assertThat(customer?.id).isEqualTo(customerId);
        assertThat(customer?.name).isEqualTo(createCustomerDto.name);
        assertThat(customer?.address).isEqualTo(createCustomerDto.address);
    }
}