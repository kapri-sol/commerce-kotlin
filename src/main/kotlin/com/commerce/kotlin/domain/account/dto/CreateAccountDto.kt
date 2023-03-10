package com.commerce.kotlin.domain.account.dto

import jakarta.validation.constraints.NotBlank

data class CreateAccountDto(
    @NotBlank
    val email: String,

    @NotBlank
    val phoneNumber: String,

    @NotBlank
    val name: String,

    @NotBlank
    val password: String

)