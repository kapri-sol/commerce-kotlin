package com.commerce.kotlin.controller

import com.commerce.kotlin.dto.CreateAccountDto
import com.commerce.kotlin.response.GetAccountResponse
import com.commerce.kotlin.response.PostAccountResponse
import com.commerce.kotlin.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("accounts")
@RestController
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping("{id}")
    fun getAccount(@PathVariable("id") accountId: Long): GetAccountResponse {
        val account = accountService.findAccount(accountId);
        return GetAccountResponse(
            email = account.email,
            phoneNumber = account.phoneNumber
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postAccount(@RequestBody createAccountDto: CreateAccountDto): PostAccountResponse {
        val accountId = this.accountService.createAccount(createAccountDto);
        return PostAccountResponse(id = accountId);
    }
}