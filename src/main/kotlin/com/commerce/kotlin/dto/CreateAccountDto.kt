package com.commerce.kotlin.dto

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