package com.commerce.kotlin.domain.account

import com.commerce.kotlin.domain.account.dto.CreateAccountDto
import com.commerce.kotlin.domain.account.dto.FindAccountResponse
import com.commerce.kotlin.domain.account.dto.CreateAccountResponse
import com.commerce.kotlin.security.authentication.CustomUserDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/me")
    fun getAccount(authentication: Authentication): FindAccountResponse {
        val customUserDetails = authentication.principal as CustomUserDetails
        val accountId = customUserDetails.accountId
        val account = accountService.findAccount(accountId)
        return FindAccountResponse(
            email = account.email,
            phoneNumber = account.phoneNumber
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postAccount(@RequestBody createAccountDto: CreateAccountDto): CreateAccountResponse {
        val accountId = this.accountService.createAccount(createAccountDto)
        return CreateAccountResponse(id = accountId)
    }
}