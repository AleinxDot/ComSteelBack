package com.aleinx.comsteelback.modules.sales.model

import jakarta.persistence.*

@Entity
@Table(name = "customers")
class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(name = "tax_id", unique = true, nullable = false)
    var docNumber: String, // DNI o RUC

    var email: String? = null,
    var phone: String? = null,

    var address: String? = null
)