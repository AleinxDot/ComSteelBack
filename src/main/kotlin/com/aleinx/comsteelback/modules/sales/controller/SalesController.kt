package com.aleinx.comsteelback.modules.sales.controller

import com.aleinx.comsteelback.common.enums.DocumentStatus
import com.aleinx.comsteelback.common.enums.DocumentType
import com.aleinx.comsteelback.modules.sales.dto.CreateSaleRequest
import com.aleinx.comsteelback.modules.sales.dto.QuoteDto
import com.aleinx.comsteelback.modules.sales.dto.SaleDetailDto
import com.aleinx.comsteelback.modules.sales.dto.SaleItemDto
import com.aleinx.comsteelback.modules.sales.repository.DocumentRepository
import com.aleinx.comsteelback.modules.sales.service.PdfService
import com.aleinx.comsteelback.modules.sales.service.SaleService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin(origins = ["*"])
class SalesController(
    private val saleService: SaleService,
    private val pdfService: PdfService,
    private val documentRepository: DocumentRepository
) {

    @PostMapping
    fun createSale(
        @RequestBody @Valid request: CreateSaleRequest,
        principal: Principal // Usuario del Token
    ): ResponseEntity<Any> {
        return try {
            val response = saleService.registerSale(request, principal.name)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            // Retornamos error 400 (Bad Request) con el mensaje (ej: "Stock insuficiente")
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
    @PostMapping("/{id}/convert")
    fun convertQuote(
        @PathVariable id: Long,
        @RequestParam targetType: String // "BOLETA" o "FACTURA"
    ): ResponseEntity<Any> {
        return try {
            val response = saleService.convertQuoteToSale(id, targetType)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<SaleDetailDto> {
        val doc = documentRepository.findById(id).orElseThrow { RuntimeException("No encontrado") }

        val items = doc.details.map {
            SaleItemDto(it.product.name, it.quantity, it.unitPrice, it.subtotal)
        }

        val dto = SaleDetailDto(
            id = doc.id!!,
            documentNumber = doc.documentNumber,
            customerName = doc.customer?.name ?: "Público General",
            issueDate = doc.issueDate,
            totalAmount = doc.totalAmount,
            status = doc.status,
            items = items
        )
        return ResponseEntity.ok(dto)
    }
    // 2. Archivar Cotización
    @PutMapping("/{id}/archive")
    fun archiveQuote(@PathVariable id: Long): ResponseEntity<Any> {
        val doc = documentRepository.findById(id).orElseThrow { RuntimeException("No encontrado") }
        doc.status = DocumentStatus.ARCHIVED
        documentRepository.save(doc)
        return ResponseEntity.ok(mapOf("message" to "Cotización archivada"))
    }

    // 3. ACTUALIZAR EL LISTADO (Para que solo traiga las ACTIVAS por defecto)
    @GetMapping("/quotes")
    fun getQuotes(@RequestParam(required = false) showArchived: Boolean = false): ResponseEntity<List<QuoteDto>> {
        val allQuotes = documentRepository.findAll().filter { it.documentType == DocumentType.COTIZACION }

        // Filtramos: Si showArchived es false, solo mostramos las ACTIVE
        val filtered = if (showArchived) allQuotes else allQuotes.filter { it.status != DocumentStatus.ARCHIVED}

        val dtos = filtered.map { doc ->
            QuoteDto(
                id = doc.id!!,
                documentNumber = doc.documentNumber,
                totalAmount = doc.totalAmount,
                issueDate = doc.issueDate,
                customerName = doc.customer?.name ?: "Público General"
            )
        }
        return ResponseEntity.ok(dtos)
    }

    @GetMapping("/{id}/pdf")
    fun downloadPdf(@PathVariable id: Long): ResponseEntity<ByteArray> {
        // El servicio ahora usa Thymeleaf internamente, pero el resultado sigue siendo bytes de PDF
        val pdfBytes = pdfService.generateSalePdf(id)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.setContentDispositionFormData("inline", "venta_$id.pdf")

        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes)
    }
    @GetMapping("/history")
    fun getSalesHistory(): ResponseEntity<List<QuoteDto>> {
        // Buscamos BOLETAS y FACTURAS
        val sales = documentRepository.findAll()
            .filter { it.documentType == DocumentType.BOLETA || it.documentType == DocumentType.FACTURA }
            .sortedByDescending { it.id } // Las más recientes primero

        // Reusamos el QuoteDto porque tiene los mismos campos que necesitamos para listar
        val dtos = sales.map { doc ->
            QuoteDto(
                id = doc.id!!,
                documentNumber = doc.documentNumber,
                totalAmount = doc.totalAmount,
                issueDate = doc.issueDate,
                customerName = doc.customer?.name ?: "Público General"
            )
        }
        return ResponseEntity.ok(dtos)
    }
}