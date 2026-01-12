package com.aleinx.comsteelback.modules.catalog.service

import com.aleinx.comsteelback.modules.catalog.dto.ProductScanResponse
import com.aleinx.comsteelback.modules.catalog.model.Product
import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    @Transactional(readOnly = true) // Optimiza rendimiento para solo lectura
    fun findByBarcode(barcode: String): ProductScanResponse? {
        val product = productRepository.findByBarcode(barcode) ?: return null

        // Mapeo manual (Entity -> DTO)
        return ProductScanResponse(
            id = product.id!!,
            barcode = product.barcode,
            name = product.name,
            brandName = product.brand.name,
            categoryName = product.category.name,
            price = product.unitPrice,
            stock = product.stockQuantity,
            minStock = product.minStockAlert
        )
    }

    @Transactional(readOnly = true)
    fun getAllProducts(page: Int, size: Int, search: String?): Page<ProductScanResponse> {
        // 1. Configurar la página (Ordenado por ID descendente para ver lo nuevo primero)
        val pageable = PageRequest.of(page, size, Sort.by("id").descending())

        // 2. Ejecutar consulta (con o sin filtro)
        val productPage = if (search.isNullOrBlank()) {
            productRepository.findAll(pageable)
        } else {
            productRepository.searchProducts(search, pageable)
        }

        // 3. Convertir Entidad -> DTO manteniendo la estructura de página
        return productPage.map { product -> mapToDto(product) }
    }

    // Helper para no repetir código de mapeo
    private fun mapToDto(product: Product): ProductScanResponse {
        return ProductScanResponse(
            id = product.id!!,
            barcode = product.barcode,
            name = product.name,
            brandName = product.brand.name,
            categoryName = product.category.name,
            price = product.unitPrice,
            stock = product.stockQuantity,
            minStock = product.minStockAlert
        )
    }
}