package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateCustomerDto
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
            account
        )
        return customerRepository.save(customer).id
    }
}