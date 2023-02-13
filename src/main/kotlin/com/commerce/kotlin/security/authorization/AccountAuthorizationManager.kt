package com.commerce.kotlin.security.authorization

import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import java.util.function.Supplier

class AccountAuthorizationManager: AuthorizationManager<RequestAuthorizationContext> {
    override fun check(
        authentication: Supplier<Authentication>?,
        requestAuthorizationContext: RequestAuthorizationContext?
    ): AuthorizationDecision? {
        return AuthorizationDecision(false)
    }
}