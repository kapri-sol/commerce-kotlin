package com.commerce.kotlin.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class AuthInterceptor : HandlerInterceptor  {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val session = request.getSession(false)

        if (session === null) {
            return false
        }

        return true
    }
}