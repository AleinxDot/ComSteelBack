package com.aleinx.comsteelback.modules.sales.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class SaleItemRequest(
    @field:NotNull
    val productId: Long,

    @field:NotNull
    val quantity: Int
)
