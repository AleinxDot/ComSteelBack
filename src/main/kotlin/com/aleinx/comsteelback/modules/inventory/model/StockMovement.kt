package com.aleinx.comsteelback.modules.inventory.model

import com.aleinx.comsteelback.common.enums.MovementType
import com.aleinx.comsteelback.modules.auth.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "stock_movements")
class StockMovement(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    val type: MovementType,

    @Column(name = "reference_doc")
    val reference: String? = null,

    val comments: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    val supplier: Supplier? = null,

    @Column(name = "movement_date")
    val date: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "movement", cascade = [CascadeType.ALL], orphanRemoval = true)
    val details: MutableList<StockMovementDetail> = mutableListOf()
) {
    fun addDetail(detail: StockMovementDetail) {
        details.add(detail)
        detail.movement = this
    }
}