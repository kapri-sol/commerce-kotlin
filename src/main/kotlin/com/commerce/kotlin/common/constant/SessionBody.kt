package com.commerce.kotlin.common.constant

data class SessionBody(
    val accountId: Long,
    var customerId: Long? = null,
    var sellerId: Long? = null
)