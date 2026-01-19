package com.aleinx.comsteelback.modules.catalog.controller

import com.aleinx.comsteelback.modules.catalog.service.CatalogService
import com.aleinx.comsteelback.modules.catalog.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = ["*"]) // Permitir acceso desde cualquier dispositivo (Ajustar en producción)
class ProductController(
    private val productService: ProductService,
    private val catalogService: CatalogService
) {

    // Endpoint: GET /api/v1/products/scan/7861034105139
    @GetMapping("/scan/{barcode}")
    fun scanProduct(@PathVariable barcode: String): ResponseEntity<Any> {
        val productDto = productService.findByBarcode(barcode)

        return if (productDto != null) {
            ResponseEntity.ok(productDto)
        } else {
            // Retornamos 404 si no existe
            ResponseEntity.notFound().build()
        }
    }
    @GetMapping
    fun getProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(defaultValue = "true") isActive: Boolean
    ): ResponseEntity<Page<Any>> { // Usamos Page<Any> o el DTO
        val response = productService.getAllProducts(page, size, isActive,search)
        return ResponseEntity.ok(response as Page<Any>)
    }
    @PutMapping("/{id}/archive")
    fun toggleArchive(@PathVariable id: Long): ResponseEntity<Any> {
        catalogService.toggleProductStatus(id)
        return ResponseEntity.ok(mapOf("message" to "Estado actualizado"))
    }
}