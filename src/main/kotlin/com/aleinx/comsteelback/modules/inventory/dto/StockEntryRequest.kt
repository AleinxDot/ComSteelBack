package com.aleinx.comsteelback.modules.inventory.dto

import jakarta.validation.constraints.NotEmpty


data class StockEntryRequest(
    val reference: String?, // Ej: "Factura F001-999"
    val comments: String?,

    @field:NotEmpty(message = "Debes ingresar al menos un producto")
    val items: List<StockEntryItem>
)