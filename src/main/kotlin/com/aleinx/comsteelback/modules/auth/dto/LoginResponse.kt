package com.aleinx.comsteelback.modules.auth.dto

data class LoginResponse(
    val token: String,
    val username: String,
    val role: String
)