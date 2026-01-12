package com.aleinx.comsteelback.modules.catalog.model
import jakarta.persistence.*

@Entity
@Table(name = "brands")
class Brand(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, unique = true, length = 100 )
    var name: String,

    @Column(name = "is_active")
    var isActive: Boolean = true
)