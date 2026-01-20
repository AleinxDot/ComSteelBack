package com.aleinx.comsteelback.modules.inventory.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "suppliers")
class Supplier(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var ruc: String, // RUC o DNI

    @Column(nullable = false)
    var name: String, // Razón Social

    var address: String? = null,
    var phone: String? = null,
    var email: String? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)