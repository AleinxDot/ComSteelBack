package com.aleinx.comsteelback.modules.sales.repository

import com.aleinx.comsteelback.modules.auth.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Customer, Long>