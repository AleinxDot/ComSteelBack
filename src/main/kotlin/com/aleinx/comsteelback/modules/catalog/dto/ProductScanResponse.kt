package com.aleinx.comsteelback.modules.catalog.dto
import java.math.BigDecimal

data class ProductScanResponse(
    val id: Long,
    val barcode: String,
    val name: String,
    val brandName: String,
    val categoryName: String,
    val price: BigDecimal,
    val stock: Int,
    val minStock: Int
)