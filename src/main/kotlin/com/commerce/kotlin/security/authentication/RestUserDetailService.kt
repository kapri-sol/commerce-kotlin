package com.commerce.kotlin.security.authentication

import com.commerce.kotlin.domain.account.AccountRepository
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class RestUserDetailService(
    private val accountRepository: AccountRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw IllegalStateException("Username is Null")
        }

        val account = accountRepository.findByEmail(username) ?: throw ChangeSetPersister.NotFoundException()

        return AccountContext(
            username = account.email,
            password = null,
            authorities =  arrayListOf(SimpleGrantedAuthority("USER")),
            id = account.id
        )
    }
}