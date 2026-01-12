package com.aleinx.comsteelback.modules.sales.repository

import com.aleinx.comsteelback.modules.sales.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
interface DocumentRepository : JpaRepository<Document, Long>{

    // Suma el total_amount de documentos emitidos en un rango de fechas (Hoy)
    // COALESCE(SUM(...), 0) asegura que si no hay ventas devuelva 0 en vez de null
    @Query("SELECT COALESCE(SUM(d.totalAmount), 0) FROM Document d WHERE d.status = 'COMPLETED' AND d.issueDate BETWEEN :start AND :end")
    fun sumTotalSalesBetween(@Param("start") start: LocalDateTime, @Param("end") end: LocalDateTime): BigDecimal

    // Cuenta cuántas ventas hubo
    @Query("SELECT COUNT(d) FROM Document d WHERE d.status = 'COMPLETED' AND d.issueDate BETWEEN :start AND :end")
    fun countSalesBetween(@Param("start") start: LocalDateTime, @Param("end") end: LocalDateTime): Long
}