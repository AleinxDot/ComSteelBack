package com.aleinx.comsteelback.modules.inventory.controller

import com.aleinx.comsteelback.modules.inventory.dto.ExternalSupplierDto
import com.aleinx.comsteelback.modules.inventory.model.Supplier
import com.aleinx.comsteelback.modules.inventory.service.SupplierService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/suppliers")
@CrossOrigin(origins = ["*"])
class SupplierController(
    private val supplierService: SupplierService
) {

    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<Supplier>> {
        val pageable = PageRequest.of(page, size, Sort.by("id").descending())
        return ResponseEntity.ok(supplierService.getAll(pageable))
    }

    @GetMapping("/all")
    fun getDropdownList(): ResponseEntity<List<Supplier>> {
        return ResponseEntity.ok(supplierService.getAllList())
    }

    @PostMapping
    fun create(@RequestBody supplier: Supplier): ResponseEntity<Supplier> {
        return ResponseEntity.ok(supplierService.save(supplier))
    }

    // --- NUEVO ENDPOINT ---
    @GetMapping("/consult-external/{docNumber}")
    fun consultExternal(@PathVariable docNumber: String): ResponseEntity<ExternalSupplierDto> {
        return ResponseEntity.ok(supplierService.consultExternalData(docNumber))
    }
}