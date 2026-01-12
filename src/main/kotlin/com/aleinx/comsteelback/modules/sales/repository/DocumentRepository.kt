package com.aleinx.comsteelback.modules.sales.repository

import com.aleinx.comsteelback.modules.sales.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : JpaRepository<Document, Long>