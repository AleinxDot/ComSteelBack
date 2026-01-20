package com.aleinx.comsteelback.modules.inventory.dto

data class ExternalSupplierDto(
    val docNumber: String,
    val name: String,
    val address: String? = null,
    val status: String? = null,
    val condition: String? = null
)