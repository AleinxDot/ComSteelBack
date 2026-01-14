package com.aleinx.comsteelback.modules.catalog.repository

import com.aleinx.comsteelback.modules.catalog.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<Category, Long> {
    fun findById(id: Int)
}