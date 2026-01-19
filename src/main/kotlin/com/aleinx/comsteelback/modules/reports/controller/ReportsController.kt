package com.aleinx.comsteelback.modules.reports.controller

import com.aleinx.comsteelback.modules.reports.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = ["*"])
class ReportsController(
    private val dashboardService: DashboardService
) {

    @GetMapping("/dashboard/stats")
    fun getDashboardStats(): ResponseEntity<Any> {
        return ResponseEntity.ok(dashboardService.getGeneralStats())
    }

    // 2. Tabla Paginada (Se llama al cambiar de página en la tabla)
    @GetMapping("/dashboard/low-stock")
    fun getLowStockProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int // Tabla pequeña por defecto
    ): ResponseEntity<Any> {
        val pageable = org.springframework.data.domain.PageRequest.of(page, size)
        return ResponseEntity.ok(dashboardService.getLowStockPaginated(pageable))
    }
}