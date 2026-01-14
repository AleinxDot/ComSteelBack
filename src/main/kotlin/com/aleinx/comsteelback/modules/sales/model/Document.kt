package com.aleinx.comsteelback.modules.sales.model

import com.aleinx.comsteelback.common.enums.DocumentStatus
import com.aleinx.comsteelback.common.enums.DocumentType
import com.aleinx.comsteelback.modules.sales.model.Customer
import com.aleinx.comsteelback.modules.auth.model.User
import com.aleinx.comsteelback.modules.catalog.model.Product
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "documents")
class Document(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 20)
    var documentType: DocumentType, // BOLETA, FACTURA, COTIZACION

    @Column(name = "document_number")
    var documentNumber: String? = null, // Serie B001-00001

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null,

    @Column(name = "issue_date")
    var issueDate: LocalDateTime = LocalDateTime.now(),

    // Totales
    @Column(nullable = false, precision = 10, scale = 2)
    var subtotal: BigDecimal = BigDecimal.ZERO,

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    var taxAmount: BigDecimal = BigDecimal.ZERO,

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    var totalAmount: BigDecimal = BigDecimal.ZERO,

    @Column(name="status", nullable = true, length = 20)
    @Enumerated(EnumType.STRING)
    var status: DocumentStatus = DocumentStatus.ACTIVE,

    // Relación Bidireccional: Un documento tiene muchos detalles
    // CascadeType.ALL: Si guardo el documento, guarda los detalles.
    // orphanRemoval: Si quito un detalle de la lista, se borra de la BD.
    @OneToMany(mappedBy = "document", cascade = [CascadeType.ALL], orphanRemoval = true)
    var details: MutableList<DocumentDetail> = mutableListOf()
) {
    // Helper para añadir detalles y mantener la coherencia bidireccional
    fun addDetail(detail: DocumentDetail) {
        details.add(detail)
        detail.document = this
    }
}