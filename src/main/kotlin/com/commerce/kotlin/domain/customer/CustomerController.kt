package com.commerce.kotlin.domain.customer

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.customer.dto.CreateCustomerDto
import com.commerce.kotlin.domain.customer.dto.GetCustomerResponse
import com.commerce.kotlin.domain.customer.dto.PostCustomerResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@RequestMapping("customers")
@RestController
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping("/me")
    fun getCustomer(@SessionAttribute(name = SESSION_NAME) sessionBody: SessionBody): GetCustomerResponse {
        val customer = this.customerService.findCustomerById(sessionBody.customerId)
        return GetCustomerResponse(name = customer.name!!, address = customer.address!!)
    }

    @PostMapping
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
}