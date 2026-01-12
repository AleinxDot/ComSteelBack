package com.aleinx.comsteelback.modules.sales.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateSaleRequest(
    val clientId: Long?, // Puede ser nulo (Venta a "Público General")

    @field:NotNull
    val type: String, // "BOLETA" o "FACTURA"

    @field:NotEmpty(message = "La venta debe tener al menos un producto")
    val items: List<SaleItemRequest>
)
