package com.aleinx.comsteelback.modules.sales.controller

import com.aleinx.comsteelback.modules.sales.dto.CreateSaleRequest
import com.aleinx.comsteelback.modules.sales.service.SaleService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin(origins = ["*"])
class SalesController(
    private val saleService: SaleService
) {

    @PostMapping
    fun createSale(
        @RequestBody @Valid request: CreateSaleRequest,
        principal: Principal // Usuario del Token
    ): ResponseEntity<Any> {
        return try {
            val response = saleService.registerSale(request, principal.name)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            // Retornamos error 400 (Bad Request) con el mensaje (ej: "Stock insuficiente")
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}