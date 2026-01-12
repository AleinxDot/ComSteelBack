package com.aleinx.comsteelback.modules.inventory.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class StockEntryItem(
    @field:NotNull
    val productId: Long,

    @field:Min(value = 1, message = "La cantidad debe ser mayor a 0")
    val quantity: Int,

    @field:Min(value = 0, message = "El costo no puede ser negativo")
    val unitCost: BigDecimal // Importante: Costo de compra unitario
)