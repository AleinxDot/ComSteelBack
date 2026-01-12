package com.aleinx.comsteelback.modules.reports.controller

import com.aleinx.comsteelback.modules.reports.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = ["*"])
class ReportsController(
    private val dashboardService: DashboardService
) {

    @GetMapping("/dashboard")
    fun getDashboard(): ResponseEntity<Any> {
        val stats = dashboardService.getDashboardStats()
        return ResponseEntity.ok(stats)
    }
}