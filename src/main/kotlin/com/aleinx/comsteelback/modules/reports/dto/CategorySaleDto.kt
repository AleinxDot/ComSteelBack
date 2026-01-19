package com.aleinx.comsteelback.modules.reports.dto

import java.math.BigDecimal

data class CategorySaleDto(
    val categoryName: String,
    val totalAmount: BigDecimal,
    val percentage: Double // Para mostrar barras de progreso
)