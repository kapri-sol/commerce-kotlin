package com.commerce.kotlin.security.config

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
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class RestSecurityConfiguration {
    @Autowired
    private lateinit var authenticationConfiguration: AuthenticationConfiguration

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun userDetailService(): UserDetailsService {
        return RestUserDetailService(accountRepository)
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        return RestAuthenticationProvider(
            passwordEncoder = passwordEncoder(),
            userDetailsService = userDetailService()
        )
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val authenticationManger = authenticationConfiguration.authenticationManager as ProviderManager
        authenticationManger.providers.add(authenticationProvider())
        return authenticationManger
    }

    @Bean
    fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        return RestAuthenticationSuccessHandler()
    }

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler {
        return RestAuthenticationFailureHandler()
    }

    @Bean
    fun authenticationProcessingFilter(): AbstractAuthenticationProcessingFilter {
        val authenticationProcessingFilter = RestAuthenticationProcessingFilter(AntPathRequestMatcher("/auth/login"))
        authenticationProcessingFilter.setSecurityContextRepository(HttpSessionSecurityContextRepository())
        authenticationProcessingFilter.setAuthenticationManager(authenticationManager())
        authenticationProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler())
        authenticationProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler())
        return authenticationProcessingFilter
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return RestAuthenticationEntryPoint()
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return RestAccessDeniedHandler()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(CorsConfiguration.ALL)
        configuration.allowedMethods = listOf(CorsConfiguration.ALL)
        configuration.allowedHeaders = listOf(CorsConfiguration.ALL)
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/accounts/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
            .requestMatchers("/docs/**").permitAll()
            .requestMatchers("/static/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(authenticationProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            .cors().configurationSource (corsConfigurationSource())
            .and()
//            .cors().disable()
            .csrf().disable()
            .build()
    }
}