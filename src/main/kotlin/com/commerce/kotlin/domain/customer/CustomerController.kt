package com.commerce.kotlin.domain.customer

import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.GetCustomerResponse
import com.commerce.kotlin.domain.customer.dto.PostCustomerResponse
import com.commerce.kotlin.domain.customer.dto.UpdateCustomerDto
import com.commerce.kotlin.security.authentication.CustomUserDetails
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping("customers")
@RestController
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping("me")
    fun findCustomerByMe(authentication: Authentication): GetCustomerResponse {
        val customUserDetails = authentication.principal as CustomUserDetails
        val customerId = customUserDetails.customerId ?: throw NotFoundException()
        val customer = this.customerService.findCustomerById(customerId)
        return GetCustomerResponse(name = customer.name, address = customer.address)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(
        authentication: Authentication,
        @RequestBody createCustomerDto: CreateCustomerDto,
    ): PostCustomerResponse {
        val customUserDetails = authentication.principal as CustomUserDetails
        val accountId = customUserDetails.accountId

        val customerId = this.customerService.createCustomer(accountId, createCustomerDto)
        customUserDetails.setCustomerId(customerId)

        return PostCustomerResponse(customerId = customerId)
    }

    @PatchMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchCustomer(
        authentication: Authentication,
        @RequestBody updateCustomerDto: UpdateCustomerDto
    ) {
        val customerId = (authentication.principal as CustomUserDetails).customerId ?: throw NotFoundException()
        this.customerService.updateCustomer(customerId, updateCustomerDto)
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(authentication: Authentication) {
        val customerId = (authentication.principal as CustomUserDetails).customerId ?: throw NotFoundException()
        this.customerService.removeCustomer(customerId)
    }
}