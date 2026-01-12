package com.aleinx.comsteelback.modules.inventory.repository

import com.aleinx.comsteelback.modules.inventory.model.StockMovement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StockMovementRepository : JpaRepository<StockMovement, Long>