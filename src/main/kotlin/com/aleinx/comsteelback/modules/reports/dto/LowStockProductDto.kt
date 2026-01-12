package com.aleinx.comsteelback.modules.reports.dto

data class LowStockProductDto(
    val id: Long,
    val name: String,
    val stock: Int,
    val minStock: Int
)