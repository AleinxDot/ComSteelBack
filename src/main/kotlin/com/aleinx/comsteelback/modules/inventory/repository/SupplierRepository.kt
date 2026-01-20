package com.aleinx.comsteelback.modules.inventory.repository

import com.aleinx.comsteelback.modules.inventory.model.Supplier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupplierRepository : JpaRepository<Supplier, Long> {
    fun findByNameContainingIgnoreCase(name: String): List<Supplier>
}