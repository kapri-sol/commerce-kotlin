package com.commerce.kotlin.domain.auth

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.domain.auth.dto.LoginDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RequestMapping("auth")
@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("login")
    fun login(@RequestBody loginDto: LoginDto, httpServletRequest: HttpServletRequest) {
        val accountId = this.authService.authenticate(
            email = loginDto.email, 
            password = loginDto.password
        ) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)

        httpServletRequest.session.setAttribute(SESSION_NAME, accountId)
    }

    @PostMapping("logout")
    fun logout(httpServletRequest: HttpServletRequest) {
        httpServletRequest.getSession(false)?.invalidate()
    }
}