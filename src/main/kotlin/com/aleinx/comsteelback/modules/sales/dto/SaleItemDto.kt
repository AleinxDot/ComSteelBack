package com.aleinx.comsteelback.modules.sales.dto

import java.math.BigDecimal

data class SaleItemDto(
    val productName: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
)