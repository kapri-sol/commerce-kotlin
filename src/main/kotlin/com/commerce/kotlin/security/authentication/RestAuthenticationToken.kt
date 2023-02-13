package com.commerce.kotlin.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.SpringSecurityCoreVersion
import org.springframework.util.Assert

class RestAuthenticationToken(
    private val principal: Any?,
    private var credentials: Any?,
    private val authorities: Collection<GrantedAuthority?>?
) :
    AbstractAuthenticationToken(authorities) {

    private val serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID

    constructor(principal: Any?, credentials: Any?) : this(principal, credentials, null)

    init {
        if (authorities != null) {
            super.setAuthenticated(true)
        } else {
            isAuthenticated = false
        }
    }

    fun unauthenticated(principal: Any?, credentials: Any?): RestAuthenticationToken {
        return RestAuthenticationToken(principal, credentials, null)
    }

    fun authenticated(
        principal: Any?, credentials: Any?,
        authorities: Collection<GrantedAuthority?>?
    ): RestAuthenticationToken {
        return RestAuthenticationToken(principal, credentials, authorities)
    }


    override fun getCredentials(): Any? {
        return credentials
    }

    override fun getPrincipal(): Any? {
        return principal
    }

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(isAuthenticated: Boolean) {
        Assert.isTrue(
            !isAuthenticated,
            "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"
        )
        super.setAuthenticated(false)
    }

    override fun eraseCredentials() {
        super.eraseCredentials()
        credentials = null
    }
}