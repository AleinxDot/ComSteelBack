package com.aleinx.comsteelback.modules.catalog.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CreateProductRequest(
    @field:NotBlank val barcode: String,
    @field:NotBlank val name: String,
    @field:NotNull val brandId: Int,
    @field:NotNull val categoryId: Int,
    @field:NotNull val price: BigDecimal,
    val minStock: Int = 5
)