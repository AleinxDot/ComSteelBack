package com.aleinx.comsteelback.modules.sales.model

import com.aleinx.comsteelback.modules.catalog.model.Product
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "document_details")
class DocumentDetail(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    var document: Document? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,

    @Column(nullable = false)
    var quantity: Int,

    // Precio Histórico (Snapshot)
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    var unitPrice: BigDecimal,

    @Column(nullable = false, precision = 10, scale = 2)
    var subtotal: BigDecimal
)