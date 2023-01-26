package com.commerce.kotlin.customer

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.domain.customer.CustomerRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerTest(
    @Autowired private val customerRepository: CustomerRepository,
    @Autowired private val mockMvc: MockMvc
) {
    @Test
    @DisplayName("")
    fun createCustomer() {
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/customers").sessionAttr(SESSION_NAME, ))
    }
}