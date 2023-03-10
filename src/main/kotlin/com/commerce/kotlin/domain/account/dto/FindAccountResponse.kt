package com.commerce.kotlin.domain.account.dto

data class FindAccountResponse(
    val email: String,
    val name: String,
    val phoneNumber: String
)