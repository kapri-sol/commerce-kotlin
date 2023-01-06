package com.commerce.kotlin

import com.commerce.kotlin.interceptor.AuthInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
class WebMvcConfig: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/accounts")
    }
}