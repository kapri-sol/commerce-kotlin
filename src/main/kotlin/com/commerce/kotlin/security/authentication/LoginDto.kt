package com.commerce.kotlin.security.authentication

data class LoginDto(
    val email: String,
    val password: String
) {
    constructor() : this("", "")
}