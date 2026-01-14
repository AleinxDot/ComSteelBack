package com.aleinx.comsteelback.modules.catalog.repository

import com.aleinx.comsteelback.modules.catalog.model.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun findById(id: Int)

}