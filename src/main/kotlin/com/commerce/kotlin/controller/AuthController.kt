package com.commerce.kotlin.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("auth")
class AuthController {
    @PostMapping("login")
    fun login() {

    }

    @PostMapping("logout")
    fun logout() {

    }
}