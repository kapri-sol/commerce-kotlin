package com.commerce.kotlin.domain.customer

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.GetCustomerResponse
import com.commerce.kotlin.domain.customer.dto.PostCustomerResponse
import com.commerce.kotlin.domain.customer.dto.UpdateCustomerDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("customers")
@RestController
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping("me")
    fun getCustomer(@SessionAttribute(name = SESSION_NAME) sessionBody: SessionBody): GetCustomerResponse {
        val customerId = sessionBody.customerId ?: throw NotFoundException()
        val customer = this.customerService.findCustomerById(customerId)
        return GetCustomerResponse(name = customer.name, address = customer.address)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCustomer(
        httpServletRequest: HttpServletRequest,
        @RequestBody createCustomerDto: CreateCustomerDto
    ): PostCustomerResponse {
        val accountId = httpServletRequest.session.getAttribute(SESSION_NAME) as Long
        val customerId = this.customerService.createCustomer(accountId, createCustomerDto)
        val sessionBody = SessionBody(accountId = accountId, customerId = customerId)
        httpServletRequest.session.setAttribute(SESSION_NAME, sessionBody)
        return PostCustomerResponse(customerId = customerId)
    }

    @PatchMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchCustomer(
        @SessionAttribute(name = SESSION_NAME) sessionBody: SessionBody,
        @RequestBody updateCustomerDto: UpdateCustomerDto
    ) {
        val customerId = sessionBody.customerId ?: throw NotFoundException()
        this.customerService.updateCustomer(customerId, updateCustomerDto)
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@SessionAttribute(name = SESSION_NAME) sessionBody: SessionBody) {
        val customerId = sessionBody.customerId ?: throw NotFoundException()
        this.customerService.removeCustomer(customerId)
    }
}