package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateCustomerDto
import com.commerce.kotlin.dto.UpdateCustomerDto
import com.commerce.kotlin.entity.Customer
import com.commerce.kotlin.repository.AccountRepository
import com.commerce.kotlin.repository.CustomerRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Transactional
@Service
class CustomerService(
    private val accountRepository: AccountRepository,
    private val customerRepository: CustomerRepository
) {
    fun createCustomer(accountId: Long, createCustomerDto: CreateCustomerDto): Long? {
        val account = accountRepository.findByIdOrNull(accountId) ?: throw NotFoundException();
        val customer = Customer(
            name = createCustomerDto.name,
            address = createCustomerDto.address,
        )
        account.setCustomer(customer);
        return customerRepository.save(customer).id
    }

    fun findCustomer(customerId: Long): Customer {
        return this.customerRepository.findByIdOrNull(customerId) ?: throw NotFoundException()
    }

    fun updateCustomer(customerId: Long, updateCustomerDto: UpdateCustomerDto) {
        val customer = this.customerRepository.findByIdOrNull(customerId) ?: throw NotFoundException()
        customer.updateName(updateCustomerDto.name)
        customer.updateAddress(updateCustomerDto.address)
    }

    fun removeCustomer(customerId: Long) {
        val customer = this.customerRepository.findByIdOrNull(customerId) ?: throw NotFoundException()
        customer.remove()
    }
}