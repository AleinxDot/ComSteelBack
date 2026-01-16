package com.aleinx.comsteelback.modules.sales.service

import com.aleinx.comsteelback.common.utils.NumberConverter
import com.aleinx.comsteelback.modules.sales.repository.DocumentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter

@Service
class PdfService(
    private val documentRepository: DocumentRepository,
    private val templateEngine: TemplateEngine
) {

    fun generateSalePdf(documentId: Long): ByteArray {
        val document = documentRepository.findByIdOrNull(documentId)
            ?: throw RuntimeException("Documento no encontrado")

        val context = Context()

        // --- 1. DATOS GENERALES ---
        context.setVariable("companyName", "COMERCIAL STEEL S.A.C.")
        context.setVariable("documentType", document.documentType.name)
        context.setVariable("documentNumber", document.documentNumber)
        context.setVariable("customerName", document.customer?.name ?: "CLIENTE VARIOS")
        context.setVariable("customerDoc", document.customer?.docNumber ?: "-")

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        context.setVariable("issueDate", document.issueDate.format(formatter))

        // --- 2. CÁLCULOS TRIBUTARIOS (IGV 18%) ---
        val total = document.totalAmount

        // Base Imponible = Total / 1.18
        val opGravada = total.divide(BigDecimal("1.18"), 2, RoundingMode.HALF_UP)

        // IGV = Total - Base Imponible
        val igv = total.subtract(opGravada)

        context.setVariable("totalAmount", total)
        context.setVariable("opGravada", opGravada)
        context.setVariable("igv", igv)

        // --- 3. CONVERSIÓN A LETRAS ---
        val amountInWords = NumberConverter.convert(total)
        context.setVariable("amountInWords", amountInWords)

        // --- 4. ITEMS ---
        val items = document.details.map {
            mapOf(
                "productName" to it.product.name,
                "quantity" to it.quantity,
                "unitPrice" to it.unitPrice,
                "subtotal" to it.subtotal
            )
        }
        context.setVariable("items", items)

        // Renderizado
        val htmlContent = templateEngine.process("receipt", context)
        val outputStream = ByteArrayOutputStream()
        val renderer = ITextRenderer()
        renderer.setDocumentFromString(htmlContent)
        renderer.layout()
        renderer.createPDF(outputStream)

        return outputStream.toByteArray()
    }
}