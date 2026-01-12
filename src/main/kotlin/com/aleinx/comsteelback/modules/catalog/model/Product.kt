package com.aleinx.comsteelback.modules.catalog.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "products")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    var barcode: String, // Lectura de barras

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    // Relación con Marca (Muchos productos, una marca)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    var brand: Brand,

    // Relación con Categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    var unitPrice: BigDecimal,

    @Column(name = "stock_quantity", nullable = false)
    var stockQuantity: Int = 0,

    @Column(name = "min_stock_alert")
    var minStockAlert: Int = 5,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Actualizar fecha automáticamente antes de guardar cambios
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}