package com.aleinx.comsteelback.modules.catalog.controller

import com.aleinx.comsteelback.modules.catalog.dto.CreateBrandRequest
import com.aleinx.comsteelback.modules.catalog.dto.CreateCategoryRequest
import com.aleinx.comsteelback.modules.catalog.dto.CreateProductRequest
import com.aleinx.comsteelback.modules.catalog.service.CatalogService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/catalog")
@CrossOrigin(origins = ["*"])
class CatalogController(private val catalogService: CatalogService) {

    // Endpoints para Producto
    @PostMapping("/products")
    fun createProduct(@Valid @RequestBody req: CreateProductRequest): ResponseEntity<Any> {
        val id = catalogService.createProduct(req)
        return ResponseEntity.ok(mapOf("id" to id, "message" to "Producto creado"))
    }

    // Endpoints para Marcas
    @GetMapping("/brands")
    fun getBrands() = ResponseEntity.ok(catalogService.getAllBrands())

    @PostMapping("/brands")
    fun createBrand(@Valid @RequestBody req: CreateBrandRequest) =
        ResponseEntity.ok(catalogService.createBrand(req))

    // Endpoints para Categorías
    @GetMapping("/categories")
    fun getCategories() = ResponseEntity.ok(catalogService.getAllCategories())

    @PostMapping("/categories")
    fun createCategory(@Valid @RequestBody req: CreateCategoryRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(catalogService.createCategory(req))
    }
    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody req: CreateProductRequest
    ): ResponseEntity<Any> {
        catalogService.updateProduct(id, req)
        return ResponseEntity.ok(mapOf("message" to "Producto actualizado correctamente"))
    }
}