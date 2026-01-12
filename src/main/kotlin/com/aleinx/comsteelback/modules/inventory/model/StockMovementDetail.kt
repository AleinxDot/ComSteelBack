package com.aleinx.comsteelback.modules.inventory.model


import com.aleinx.comsteelback.modules.catalog.model.Product
import jakarta.persistence.*
import java.math.BigDecimal
@Entity
@Table(name = "stock_movement_details")
class StockMovementDetail(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_id", nullable = false)
    var movement: StockMovement? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    val quantity: Int,

    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    val unitCost: BigDecimal
)