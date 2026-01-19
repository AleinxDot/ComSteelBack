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

    @Transactional(readOnly = true)
    fun findByBarcode(barcode: String): ProductScanResponse? {
        val product = productRepository.findByBarcode(barcode) ?: return null
        return mapToDto(product)
    }

    @Transactional(readOnly = true)
    fun getAllProducts(
        page: Int,
        size: Int,
        status: Boolean,
        search: String?,
        sortParam: String
    ): Page<ProductScanResponse> {

        // Parsear "columna,direccion"
        val sortSplit = sortParam.split(",")
        val sortProperty = sortSplit[0]
        val sortDirection = if (sortSplit.getOrElse(1) { "asc" }.equals("desc", ignoreCase = true))
            Sort.Direction.DESC else Sort.Direction.ASC

        // Mapeo seguro de Frontend -> Entidad BD
        val entityProperty = when (sortProperty) {
            "stock" -> "stockQuantity"
            "price" -> "unitPrice"
            else -> sortProperty
        }

        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, entityProperty))

        val productPage = if (search.isNullOrBlank()) {
            productRepository.findByIsActive(status, pageable)
        } else {
            productRepository.searchProducts(search, status, pageable)
        }
        return productPage.map { product -> mapToDto(product) }
    }

    private fun mapToDto(product: Product): ProductScanResponse {
        return ProductScanResponse(
            id = product.id!!,
            barcode = product.barcode,
            name = product.name,
            brandName = product.brand.name,
            categoryName = product.category.name,
            price = product.unitPrice,
            stock = product.stockQuantity,
            minStock = product.minStockAlert,
            brandId = product.brand.id!!,
            categoryId = product.category.id!!
        )
    }
}