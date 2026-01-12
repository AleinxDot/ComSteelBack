package com.aleinx.comsteelback.modules.catalog.model

import jakarta.persistence.Entity
import jakarta.persistence.*
import jakarta.persistence.Table

@Entity
@Table(name = "categories")
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, unique = true, length =100)
    var name: String,

    var description: String? = null,

    @Column(name = "is_active")
    var isActive: Boolean = true
)