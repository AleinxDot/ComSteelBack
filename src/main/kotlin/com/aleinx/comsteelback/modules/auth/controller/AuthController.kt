package com.aleinx.comsteelback.modules.auth.controller

import com.aleinx.comsteelback.modules.auth.dto.LoginRequest
import com.aleinx.comsteelback.modules.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = ["*"])
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            val response = authService.login(request)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            // Retornar 401 si falla la autenticación
            ResponseEntity.status(401).body(mapOf("error" to e.message))
        }
    }
}