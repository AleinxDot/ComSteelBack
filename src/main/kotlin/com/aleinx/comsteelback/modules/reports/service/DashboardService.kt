package com.aleinx.comsteelback.modules.reports.service

import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import com.aleinx.comsteelback.modules.reports.dto.CategorySaleDto
import com.aleinx.comsteelback.modules.reports.dto.DashboardResponse
import com.aleinx.comsteelback.modules.reports.dto.LowStockProductDto
import com.aleinx.comsteelback.modules.sales.repository.DocumentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Service
class DashboardService(
    private val documentRepository: DocumentRepository,
    private val productRepository: ProductRepository
) {

    @Transactional(readOnly = true)
    fun getGeneralStats(): DashboardResponse {
        val now = LocalDateTime.now()
        val startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN)

        val totalMoney = documentRepository.sumTotalSalesBetween(startOfMonth, now) ?: BigDecimal.ZERO
        val totalCount = documentRepository.countSalesBetween(startOfMonth, now)


        val categoryData = documentRepository.getSalesByCategory(startOfMonth, now)

        val categoriesDto = categoryData.take(5).map { row ->
            val name = row[0] as String
            val amount = row[1] as BigDecimal
            val percentage = if (totalMoney.compareTo(BigDecimal.ZERO) > 0)
                amount.toDouble() / totalMoney.toDouble() * 100
            else 0.0

            CategorySaleDto(name, amount, percentage)
        }

        val lowStockCount = productRepository.countLowStock()

        return DashboardResponse(
            monthlySalesAmount = totalMoney,
            monthlySalesCount = totalCount,
            lowStockTotalCount = lowStockCount,
            salesByCategory = categoriesDto
        )
    }

    // Nuevo método solo para la tabla paginada
    @Transactional(readOnly = true)
    fun getLowStockPaginated(pageable: Pageable): Page<LowStockProductDto> {
        return productRepository.findLowStockProducts(pageable).map { p ->
            LowStockProductDto(
                id = p.id!!,
                name = p.name,
                stock = p.stockQuantity,
                minStock = p.minStockAlert
            )
        }
    }
}