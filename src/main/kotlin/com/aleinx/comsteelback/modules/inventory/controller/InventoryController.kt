package com.aleinx.comsteelback.modules.inventory.controller

import com.aleinx.comsteelback.modules.inventory.dto.StockEntryRequest
import com.aleinx.comsteelback.modules.inventory.service.InventoryService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/inventory")
@CrossOrigin(origins = ["*"])
class InventoryController(
    private val inventoryService: InventoryService
) {

    @PostMapping("/entry")
    fun registerEntry(
        @Valid @RequestBody request: StockEntryRequest,
        principal: Principal
    ): ResponseEntity<Any> {
        return try {
            val message = inventoryService.registerEntry(request, principal.name)
            ResponseEntity.ok(mapOf("message" to message))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}