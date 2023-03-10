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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CustomerServiceTest{
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var customerService: CustomerService

    companion object {
        private lateinit var initialAccount: Account
        lateinit var initialCustomer: Customer

        val faker = Faker()

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
                    name = faker.name().fullName(),
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

    @Test
    @DisplayName("고객을 생성한다.")
    fun createCustomer() {
        // given
        val account = accountRepository.save(
            Account(
                email = faker.internet().emailAddress(),
                phoneNumber = faker.phoneNumber().phoneNumber(),
                name = faker.name().fullName(),
                password = faker.internet().password()
            )
        )

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
    @DisplayName("고객을 검색한다.")
    fun findCustomer() {
        // given
        val customer = initialCustomer
        // when
        val findCustomer = this.customerService.findCustomerById(customer.id!!)
        // then
        assertThat(customer.id).isEqualTo(findCustomer.id)
        assertThat(customer.name).isEqualTo(findCustomer.name)
        assertThat(customer.address).isEqualTo(findCustomer.address)
    }

    @Test
    @DisplayName("고객 정보를 수정한다.")
    fun updateCustomer() {
        // given
        val customer = initialCustomer
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
    @DisplayName("고객을 제거한다.")
    fun removeCustomer() {
        // given
        val customer = initialCustomer
        // when
        this.customerService.removeCustomer(customer.id!!)
        val findCustomer = this.customerRepository.findByIdOrNull(customer.id!!)
        // then
        assertThat(findCustomer?.deleted).isTrue
    }
}