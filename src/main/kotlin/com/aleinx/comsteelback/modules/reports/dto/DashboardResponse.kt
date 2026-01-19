package com.aleinx.comsteelback.modules.reports.dto

import java.math.BigDecimal

data class DashboardResponse(
    val monthlySalesAmount: BigDecimal, // Ventas del Mes
    val monthlySalesCount: Long,        // Transacciones del Mes
    val lowStockTotalCount: Long,       // Total de alertas (para el KPI)
    val salesByCategory: List<CategorySaleDto> // Nueva métrica
)