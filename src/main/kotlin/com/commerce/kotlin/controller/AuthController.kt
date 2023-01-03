package com.commerce.kotlin.controller

import com.commerce.kotlin.constant.SESSION_NAME
import com.commerce.kotlin.dto.LoginDto
import com.commerce.kotlin.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest
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

        val session = httpServletRequest
            .getSession()

        session.setAttribute(SESSION_NAME, accountId)
    }

    @PostMapping("logout")
    fun logout(httpServletRequest: HttpServletRequest) {
        httpServletRequest.getSession(false)?.invalidate()
    }
}