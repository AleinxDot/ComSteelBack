package com.aleinx.comsteelback.modules.sales.repository

import com.aleinx.comsteelback.modules.sales.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Customer, Long> {

    // Buscador: Coincidencia en Nombre O Documento
    // Duplicidad
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR c.docNumber LIKE CONCAT('%', :query, '%')")
    fun search(query: String): List<Customer>

    fun existsByDocNumber(docNumber: String): Boolean
}