package com.aleinx.comsteelback.modules.sales.service

import com.aleinx.comsteelback.modules.sales.repository.DocumentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

@Service
class PdfService(
    private val documentRepository: DocumentRepository,
    private val templateEngine: TemplateEngine // Inyectamos el motor de Thymeleaf
) {

    fun generateSalePdf(documentId: Long): ByteArray {
        // 1. Obtener datos de la BD
        val document = documentRepository.findByIdOrNull(documentId)
            ?: throw RuntimeException("Documento no encontrado")

        // 2. Preparar el Contexto de Thymeleaf (Variables para el HTML)
        val context = Context()

        // Datos simples
        context.setVariable("companyName", "Comercial Steel S.A.C.")
        context.setVariable("documentType", document.documentType.name)
        context.setVariable("documentNumber", document.documentNumber)
        context.setVariable("customerName", document.customer?.name ?: "Público General")
        context.setVariable("customerDoc", document.customer?.docNumber ?: "-")
        context.setVariable("totalAmount", document.totalAmount)

        // Fecha formateada
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        context.setVariable("issueDate", document.issueDate.format(formatter))

        // Lista de Productos (Mapeamos a objetos simples)
        val items = document.details.map {
            mapOf(
                "productName" to it.product.name,
                "quantity" to it.quantity,
                "subtotal" to it.subtotal
            )
        }
        context.setVariable("items", items)

        // 3. Renderizar HTML (String)
        val htmlContent = templateEngine.process("receipt", context)

        // 4. Convertir HTML a PDF usando Flying Saucer
        val outputStream = ByteArrayOutputStream()
        val renderer = ITextRenderer()

        // Importante: setDocumentFromString espera XHTML válido
        renderer.setDocumentFromString(htmlContent)
        renderer.layout()
        renderer.createPDF(outputStream)

        return outputStream.toByteArray()
    }
}