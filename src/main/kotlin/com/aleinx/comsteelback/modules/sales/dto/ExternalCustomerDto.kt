package com.aleinx.comsteelback.modules.sales.dto

data class ExternalCustomerDto(
    val docNumber: String,
    val name: String,
    val address: String? = null,
    val status: String? = null,    // Solo vendrá si es RUC
    val condition: String? = null  // Solo vendrá si es RUC
)