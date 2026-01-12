package com.aleinx.comsteelback.common.security

import com.aleinx.comsteelback.modules.auth.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuario no encontrado") }

        // Convertimos nuestro User (Entidad) al User de Spring Security
        return User.builder()
            .username(user.username)
            .password(user.passwordHash)
            .roles(user.role) // Spring añade "ROLE_" automáticamente internamente
            .build()
    }
}