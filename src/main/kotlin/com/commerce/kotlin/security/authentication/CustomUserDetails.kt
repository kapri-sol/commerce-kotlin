package com.commerce.kotlin.security.authentication

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

class CustomUserDetails(
    val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority?>?,
    val accountId: Long,
    customerId: Long?,
    sellerId: Long?
) : UserDetails, Serializable {

    private var locked: Boolean = false
    var customerId: Long? = customerId
        private set

    var sellerId: Long? = sellerId
        private set

    fun setCustomerId(customerId: Long) {
        this.customerId = customerId
    }

    fun setSellerId(sellerId: Long) {
        this.sellerId = sellerId
    }

    constructor(email: String, password: String, authorities: Collection<GrantedAuthority?>?, accountId: Long) : this(
        email,
        password,
        authorities,
        accountId,
        null,
        null
    )

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return locked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(javaClass.name).append(" [")
        sb.append("Username=").append(username).append(", ")
        sb.append("Email=").append(email).append(", ")
        sb.append("AccountId=").append(accountId).append(", ")
        sb.append("CustomerId=").append(customerId).append(", ")
        sb.append("SellerId=").append(sellerId).append(", ")
        sb.append("Password=[PROTECTED], ")
        sb.append("Enabled=").append(isEnabled).append(", ")
        sb.append("AccountNonExpired=").append(isAccountNonExpired).append(", ")
        sb.append("credentialsNonExpired=").append(isCredentialsNonExpired).append(", ")
        sb.append("AccountNonLocked=").append(isAccountNonLocked).append(", ")
        sb.append("Granted Authorities=").append(authorities).append("]")
        return sb.toString()
    }
}