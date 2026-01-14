package com.aleinx.comsteelback.modules.catalog.dto

import jakarta.validation.constraints.NotBlank

data class CreateBrandRequest(
    @field:NotBlank val name: String
)