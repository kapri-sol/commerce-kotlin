package com.commerce.kotlin.controller

import com.commerce.kotlin.dto.CreateAccountDto
import com.commerce.kotlin.response.CreateAccountResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


//@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
){

    @Test
    fun createAccount() {
        val createAccountDto = CreateAccountDto(
            email = "",
            phoneNumber = "010-1111-2222",
            password = "1111"
        );

        val createAccountResponse = CreateAccountResponse(id = 1L);

        this.mockMvc.perform(
            post("/accounts")
                .content(jacksonObjectMapper().writeValueAsString(createAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(createAccountResponse)));
    }

    @Test
    fun findAccount() {
        this.mockMvc.perform(get("/accounts"))
            .andExpect(status().isOk)
            .andExpect(content().string("hi"))
    }
}