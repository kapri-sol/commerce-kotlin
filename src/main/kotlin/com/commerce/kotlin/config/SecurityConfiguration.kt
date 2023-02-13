package com.commerce.kotlin.config

import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.security.authentication.RestAuthenticationEntryPoint
import com.commerce.kotlin.security.authentication.RestAuthenticationProvider
import com.commerce.kotlin.security.authentication.RestUserDetailService
import com.commerce.kotlin.security.filter.RestAuthenticationProcessingFilter
import com.commerce.kotlin.security.handler.RestAccessDeniedHandler
import com.commerce.kotlin.security.handler.RestAuthenticationFailureHandler
import com.commerce.kotlin.security.handler.RestAuthenticationSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Autowired private lateinit var authenticationConfiguration: AuthenticationConfiguration
    @Autowired private lateinit var accountRepository: AccountRepository

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun userDetailService(): UserDetailsService {
        return RestUserDetailService(accountRepository)
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val authenticationManger = authenticationConfiguration.authenticationManager as ProviderManager
        authenticationManger.providers.add(RestAuthenticationProvider(
            passwordEncoder =  passwordEncoder(),
            userDetailsService =  userDetailService()))
        return authenticationManger
    }

    @Bean
    fun authenticationProcessingFilter(): AbstractAuthenticationProcessingFilter {
        val authenticationProcessingFilter = RestAuthenticationProcessingFilter(AntPathRequestMatcher("/auth/login"))
        authenticationProcessingFilter.setSecurityContextRepository(HttpSessionSecurityContextRepository())
        authenticationProcessingFilter.setAuthenticationManager(authenticationManager())
        authenticationProcessingFilter.setAuthenticationSuccessHandler(RestAuthenticationSuccessHandler())
        authenticationProcessingFilter.setAuthenticationFailureHandler(RestAuthenticationFailureHandler())
        return authenticationProcessingFilter
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/accounts/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(authenticationProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
            .authenticationEntryPoint(RestAuthenticationEntryPoint())
            .accessDeniedHandler(RestAccessDeniedHandler())
            .and()
            .csrf().disable()
            .build()
    }
}