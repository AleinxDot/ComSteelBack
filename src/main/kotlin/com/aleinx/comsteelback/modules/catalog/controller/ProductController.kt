package com.aleinx.comsteelback.modules.catalog.controller

import com.aleinx.comsteelback.modules.catalog.service.CatalogService
import com.aleinx.comsteelback.modules.catalog.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = ["*"])
class ProductController(
    private val productService: ProductService,
    private val catalogService: CatalogService
) {

    @GetMapping("/scan/{barcode}")
    fun scanProduct(@PathVariable barcode: String): ResponseEntity<Any> {
        val productDto = productService.findByBarcode(barcode)
        return if (productDto != null) {
            ResponseEntity.ok(productDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(defaultValue = "true") isActive: Boolean,
        // Sort format: field,asc|desc
        @RequestParam(defaultValue = "id,desc") sort: String
    ): ResponseEntity<Page<Any>> {
        val response = productService.getAllProducts(page, size, isActive, search, sort)
        return ResponseEntity.ok(response as Page<Any>)
    }

    @PutMapping("/{id}/archive")
    fun toggleArchive(@PathVariable id: Long): ResponseEntity<Any> {
        catalogService.toggleProductStatus(id)
        return ResponseEntity.ok(mapOf("message" to "Estado actualizado"))
    }
}