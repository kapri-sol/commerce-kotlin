package com.commerce.kotlin.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class RestAuthenticationSuccessHandler: AuthenticationSuccessHandler {

    private val objectMapper = ObjectMapper()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val principal = authentication?.principal ?: throw IllegalStateException("Principal is NULL!")

        response?.status = HttpStatus.OK.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE

        objectMapper.writeValue(response?.writer, principal)
    }
}