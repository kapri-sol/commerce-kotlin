package com.commerce.kotlin.domain.account.dto

import jakarta.validation.constraints.NotBlank

data class CreateAccountDto(
    @NotBlank
    var email: String,

    @NotBlank
    var phoneNumber: String,

    @NotBlank
    var password: String
) {

}