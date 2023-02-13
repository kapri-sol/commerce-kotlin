package com.commerce.kotlin.security.filter

import com.commerce.kotlin.security.authentication.LoginDto
import com.commerce.kotlin.security.authentication.RestAuthenticationToken
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class RestAuthenticationProcessingFilter(requiresAuthenticationRequestMatcher: RequestMatcher?) :
    AbstractAuthenticationProcessingFilter(requiresAuthenticationRequestMatcher) {

    private val objectMapper = ObjectMapper()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val loginDto = objectMapper.readValue(request?.reader, LoginDto::class.java)
        val restAuthenticationToken = RestAuthenticationToken(
            principal = loginDto.email,
            credentials = loginDto.password
        )
        return authenticationManager.authenticate(restAuthenticationToken)
    }
}