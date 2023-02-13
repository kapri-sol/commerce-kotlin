package com.commerce.kotlin.security.authentication

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class RestAuthenticationProvider(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsService,
): AuthenticationProvider {


    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication == null) {
           throw IllegalStateException("Authentication is null")
        }
        val username = authentication.name
        val password = authentication.credentials as String

        val accountContext = userDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, accountContext.password)) {
           throw BadCredentialsException("Bad Credential")
        }

        return RestAuthenticationToken(
            accountContext,
            null,
            accountContext.authorities
        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.equals(RestAuthenticationToken::class.java) ?: throw IllegalStateException("Authentication is Null")
    }
}