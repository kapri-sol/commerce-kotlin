package com.commerce.kotlin.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.security.auth.login.CredentialExpiredException

class RestAuthenticationFailureHandler: AuthenticationFailureHandler {

    private val objectMapper = ObjectMapper()

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        var errMsg = "Invalid Username or Password"

        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE

        when (exception) {
            is BadCredentialsException ->
                errMsg = "Invalid Username or Password"

            is DisabledException ->
                errMsg = "Locked"

            is CredentialExpiredException ->
                errMsg = "Expired password"

        }

        objectMapper.writeValue(response?.writer, errMsg)
    }
}