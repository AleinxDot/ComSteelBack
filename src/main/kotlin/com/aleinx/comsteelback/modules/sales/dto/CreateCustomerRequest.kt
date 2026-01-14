package com.aleinx.comsteelback.modules.sales.dto

import jakarta.validation.constraints.NotBlank

data class CreateCustomerRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val docNumber: String,
    val email: String?,
    val phone: String?,
    val address: String?
)