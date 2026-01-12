package com.aleinx.comsteelback.modules.auth.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, unique = true, length = 50)
    var username: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(name = "full_name", nullable = false)
    var fullName: String,

    @Column(name = "role", nullable = false, length =20)
    var role: String = "SELLER",

    @Column(name = "is_active")
    var isActive: Boolean = true
)