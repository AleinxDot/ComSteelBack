package com.aleinx.comsteelback.modules.sales.dto

data class SaleResponse(
    val saleId: Long,
    val documentNumber: String?,
    val totalAmount: java.math.BigDecimal,
    val message: String = "Venta registrada correctamente"
)