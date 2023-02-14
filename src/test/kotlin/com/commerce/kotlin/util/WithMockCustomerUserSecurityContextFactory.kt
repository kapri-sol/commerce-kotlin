package com.commerce.kotlin.util


import com.commerce.kotlin.security.authentication.CustomUserDetails
import com.commerce.kotlin.security.authentication.RestAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomerUserSecurityContextFactory :
    WithSecurityContextFactory<WithMockCustomUser> {
    override fun createSecurityContext(customUser: WithMockCustomUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val customUserDetails = CustomUserDetails(
            email = customUser.email,
            password = customUser.password,
            authorities = customUser.authorities.map { SimpleGrantedAuthority(it) },
            accountId = customUser.accountId,
            customerId = if (customUser.customerId < 0) null else customUser.customerId,
            sellerId = if (customUser.sellerId < 0) null else customUser.sellerId
        )

        val ajaxAuthenticationToken = RestAuthenticationToken(principal = customUserDetails, credentials = customUserDetails.password, authorities = arrayListOf(SimpleGrantedAuthority("USER")))
        context.authentication = ajaxAuthenticationToken
        return context
    }
}