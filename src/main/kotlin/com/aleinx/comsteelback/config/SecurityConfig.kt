package com.aleinx.comsteelback.config;

import com.aleinx.comsteelback.common.security.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.Arrays

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 1. Configuración CORS explícita (Permitir conexión desde React)
            .cors { it.configurationSource(corsConfigurationSource()) }

            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/auth/**").permitAll()
                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Permitir explícitamente las peticiones OPTIONS (Preflight)
                auth.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    // --- NUEVO: Configuración Global de CORS ---
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // CAMBIO AQUÍ: Usamos Patrones en lugar de Origenes fijos
        // Esto permite localhost, 192.168.1.37, o cualquier IP de tu red
        configuration.allowedOriginPatterns = Arrays.asList("*")

        configuration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = Arrays.asList("Authorization", "Content-Type", "X-Requested-With")
        configuration.allowCredentials = true // Esto requiere usar allowedOriginPatterns si usas *

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}