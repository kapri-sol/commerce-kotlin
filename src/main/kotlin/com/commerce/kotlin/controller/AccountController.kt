package com.commerce.kotlin.controller

import com.commerce.kotlin.dto.CreateAccountDto
import com.commerce.kotlin.response.CreateAccountResponse
import com.commerce.kotlin.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/accounts")
@RestController
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping
    fun findAccount(): String {
        return "hi";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestBody createAccountDto: CreateAccountDto): CreateAccountResponse {
        val accountId = this.accountService.createAccount(createAccountDto);
        return CreateAccountResponse(id = accountId);
    }
}