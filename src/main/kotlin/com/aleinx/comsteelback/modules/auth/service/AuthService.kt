package com.aleinx.comsteelback.modules.auth.service

import com.aleinx.comsteelback.common.security.JwtUtil
import com.aleinx.comsteelback.modules.auth.dto.*
import com.aleinx.comsteelback.modules.auth.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder, // Inyectamos el encriptador
    private val jwtUtil: JwtUtil
) {

    fun login(request: LoginRequest): LoginResponse {
        // 1. Buscar usuario
        val user = userRepository.findByUsername(request.username)
            .orElseThrow { RuntimeException("Usuario o contraseña incorrectos") }

        // 2. Verificar contraseña (comparar texto plano con Hash de BD)
        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw RuntimeException("Usuario o contraseña incorrectos")
        }

        // 3. Generar Token
        val token = jwtUtil.generateToken(user.username, user.role)

        return LoginResponse(token, user.username, user.role)
    }
}