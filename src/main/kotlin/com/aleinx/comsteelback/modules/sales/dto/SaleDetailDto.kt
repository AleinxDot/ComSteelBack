package com.aleinx.comsteelback.modules.sales.dto

import com.aleinx.comsteelback.common.enums.DocumentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class SaleDetailDto(
    val id: Long,
    val documentNumber: String?,
    val customerName: String,
    val issueDate: LocalDateTime,
    val totalAmount: BigDecimal,
    val status: DocumentStatus,
    // La lista de productos
    val items: List<SaleItemDto>
)