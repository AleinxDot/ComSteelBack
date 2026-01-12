package com.aleinx.comsteelback.modules.catalog.repository

import com.aleinx.comsteelback.modules.catalog.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    // Consulta optimizada para la pistola de código de barras
    // Trae el Producto + Marca + Categoría en un solo viaje a la BD
    @Query("""
        SELECT p FROM Product p 
        JOIN FETCH p.brand 
        JOIN FETCH p.category 
        WHERE p.barcode = :barcode AND p.isActive = true
    """)
    fun findByBarcode(barcode: String): Product?

    //Busqueda con paginado
    @Query("""
        SELECT p FROM Product p 
        LEFT JOIN FETCH p.brand 
        LEFT JOIN FETCH p.category 
        WHERE p.isActive = true 
        AND (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
            p.barcode LIKE CONCAT('%', :search, '%')
        )
    """)
    fun searchProducts(@Param("search") search: String, pageable: Pageable): Page<Product>

    // Incluir todos los productos
    @Query("""
        SELECT p FROM Product p 
        LEFT JOIN FETCH p.brand 
        LEFT JOIN FETCH p.category 
        WHERE p.isActive = true
    """)
    override fun findAll(pageable: Pageable): Page<Product>



    // Cuenta cuántos productos están en peligro (stock <= minStock)
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = true AND p.stockQuantity <= p.minStockAlert")
    fun countLowStock(): Long

    // Trae la lista de productos en peligro (para mostrar en tabla)
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity <= p.minStockAlert")
    fun findLowStockProducts(): List<Product>

}