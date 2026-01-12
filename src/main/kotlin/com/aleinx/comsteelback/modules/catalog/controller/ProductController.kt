package com.aleinx.comsteelback.modules.catalog.controller

import com.aleinx.comsteelback.modules.catalog.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = ["*"]) // Permitir acceso desde cualquier dispositivo (Ajustar en producción)
class ProductController(
    private val productService: ProductService
) {

    // Endpoint: GET /api/v1/products/scan/7861034105139
    @GetMapping("/scan/{barcode}")
    fun scanProduct(@PathVariable barcode: String): ResponseEntity<Any> {
        val productDto = productService.findByBarcode(barcode)

        return if (productDto != null) {
            ResponseEntity.ok(productDto)
        } else {
            // Retornamos 404 si no existe, ideal para que el frontend haga un sonido de error
            ResponseEntity.notFound().build()
        }
    }
    @GetMapping
    fun getProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<Page<Any>> { // Usamos Page<Any> o el DTO
        val response = productService.getAllProducts(page, size, search)
        return ResponseEntity.ok(response as Page<Any>)
    }
}