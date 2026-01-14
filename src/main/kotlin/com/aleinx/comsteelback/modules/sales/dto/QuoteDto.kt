package com.aleinx.comsteelback.modules.sales.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class QuoteDto(
    val id: Long,
    val documentNumber: String?,
    val totalAmount: BigDecimal,
    val issueDate: LocalDateTime,
    val customerName: String
)