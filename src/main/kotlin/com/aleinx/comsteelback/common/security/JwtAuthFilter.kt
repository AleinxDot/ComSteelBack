package com.aleinx.comsteelback.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // 1. Verificar si viene el token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7) // Quitar "Bearer "
            val username = try {
                jwtUtil.extractUsername(token)
            } catch (e: Exception) {
                null
            }

            // 2. Si hay usuario y no está autenticado aún en el contexto
            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)

                // 3. Validar token
                if (jwtUtil.isTokenValid(token, userDetails.username)) {
                    // 4. Crear objeto de autenticación
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // 5. Establecer seguridad (Login exitoso para este request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        // Continuar con la petición
        filterChain.doFilter(request, response)
    }
}