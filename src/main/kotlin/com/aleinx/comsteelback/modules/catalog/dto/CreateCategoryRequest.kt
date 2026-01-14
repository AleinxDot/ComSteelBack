package com.aleinx.comsteelback.modules.catalog.dto

import jakarta.validation.constraints.NotBlank

data class CreateCategoryRequest(
    @field:NotBlank val name: String
)