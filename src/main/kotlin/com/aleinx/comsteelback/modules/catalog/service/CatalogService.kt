package com.aleinx.comsteelback.modules.catalog.service

import com.aleinx.comsteelback.modules.catalog.dto.CreateBrandRequest
import com.aleinx.comsteelback.modules.catalog.dto.CreateCategoryRequest
import com.aleinx.comsteelback.modules.catalog.dto.CreateProductRequest
import com.aleinx.comsteelback.modules.catalog.dto.DropdownItem
import com.aleinx.comsteelback.modules.catalog.model.Brand
import com.aleinx.comsteelback.modules.catalog.model.Category
import com.aleinx.comsteelback.modules.catalog.model.Product
import com.aleinx.comsteelback.modules.catalog.repository.BrandRepository
import com.aleinx.comsteelback.modules.catalog.repository.CategoryRepository
import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CatalogService(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository
) {

    // --- PRODUCTOS ---
    @Transactional
    fun createProduct(req: CreateProductRequest): Long {
        if (productRepository.findByBarcode(req.barcode) != null) {
            throw RuntimeException("El código de barras ${req.barcode} ya existe")
        }

        val brand = brandRepository.findByIdOrNull(req.brandId.toLong())
            ?: throw RuntimeException("Marca no encontrada")

        val category = categoryRepository.findByIdOrNull(req.categoryId.toLong())
            ?: throw RuntimeException("Categoría no encontrada")

        val product = Product(
            barcode = req.barcode,
            name = req.name,
            brand = brand,
            category = category,
            unitPrice = req.price,
            minStockAlert = req.minStock,
            stockQuantity = 0
        )

        return productRepository.save(product).id!!
    }

    // --- MARCAS ---
    @Transactional
    fun createBrand(req: CreateBrandRequest): DropdownItem {
        val brand = brandRepository.save(Brand(name = req.name))
        return DropdownItem(brand.id!!, brand.name)
    }

    // -- CATEGORIAS ---
    @Transactional
    fun createCategory(req: CreateCategoryRequest): DropdownItem {
        // Guardamos usando la entidad Category (asegúrate de tener el constructor adecuado)
        val category = categoryRepository.save(Category(name = req.name))
        return DropdownItem(category.id!!, category.name)
    }

    // ACTUALIZAR PRODUCTOS
    @Transactional
    fun updateProduct(id: Long, req: CreateProductRequest) {
        val product = productRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Producto no encontrado")

        val brand = brandRepository.findByIdOrNull(req.brandId.toLong())
            ?: throw RuntimeException("Marca no encontrada")

        val category = categoryRepository.findByIdOrNull(req.categoryId.toLong())
            ?: throw RuntimeException("Categoría no encontrada")

        // Actualizamos los campos
        product.barcode = req.barcode
        product.name = req.name
        product.brand = brand
        product.category = category
        product.unitPrice = req.price
        product.minStockAlert = req.minStock

        productRepository.save(product)
    }

    fun getAllBrands(): List<DropdownItem> = brandRepository.findAll()
        .map { DropdownItem(it.id!!, it.name) }

    fun getAllCategories(): List<DropdownItem> = categoryRepository.findAll()
        .map { DropdownItem(it.id!!, it.name) }
}