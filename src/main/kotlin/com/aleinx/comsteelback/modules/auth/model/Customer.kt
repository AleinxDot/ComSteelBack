package com.aleinx.comsteelback.modules.auth.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "customers")
class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "tax_id", unique = true, length = 20)
    var taxId: String? = null, // RUC o DNI

    @Column(nullable = false)
    var name: String,

    var email: String? = null,
    var phone: String? = null,
    var address: String? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)