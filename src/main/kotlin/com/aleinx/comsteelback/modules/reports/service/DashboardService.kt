package com.aleinx.comsteelback.modules.reports.service

import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import com.aleinx.comsteelback.modules.reports.dto.DashboardResponse
import com.aleinx.comsteelback.modules.reports.dto.LowStockProductDto
import com.aleinx.comsteelback.modules.sales.repository.DocumentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class DashboardService(
    private val documentRepository: DocumentRepository,
    private val productRepository: ProductRepository
) {

    @Transactional(readOnly = true)
    fun getDashboardStats(): DashboardResponse {
        // 1. Definir rango de tiempo (Hoy)
        val startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN) // 00:00:00
        val endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)   // 23:59:59.999

        // 2. Obtener métricas de Ventas
        val totalMoney = documentRepository.sumTotalSalesBetween(startOfDay, endOfDay)
        val totalCount = documentRepository.countSalesBetween(startOfDay, endOfDay)

        // 3. Obtener métricas de Inventario
        val lowStockCount = productRepository.countLowStock()
        val lowStockEntities = productRepository.findLowStockProducts()

        // 4. Convertir entidades a DTOs ligeros
        val lowStockDtos = lowStockEntities.map { p ->
            LowStockProductDto(
                id = p.id!!,
                name = p.name,
                stock = p.stockQuantity,
                minStock = p.minStockAlert
            )
        }

        return DashboardResponse(
            todaySalesAmount = totalMoney,
            todaySalesCount = totalCount,
            lowStockCount = lowStockCount,
            lowStockProducts = lowStockDtos
        )
    }
}