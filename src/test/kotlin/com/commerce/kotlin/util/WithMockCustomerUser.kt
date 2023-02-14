package com.commerce.kotlin.util

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomerUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val email: String = "user",
    val password: String = "1",
    val authorities: Array<String> = ["USER"],
    val accountId: Long = 1L,
    val customerId: Long = -1L,
    val sellerId: Long = -1L
)