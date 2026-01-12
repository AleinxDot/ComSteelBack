package com.aleinx.comsteelback.modules.reports.dto

import java.math.BigDecimal

data class DashboardResponse(
    val todaySalesAmount: BigDecimal, // Total dinero hoy
    val todaySalesCount: Long,        // Cantidad ventas hoy
    val lowStockCount: Long,          // Cuántos productos están en alerta
    val lowStockProducts: List<LowStockProductDto> // Lista de los productos críticos
)