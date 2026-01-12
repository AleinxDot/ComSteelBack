package com.aleinx.comsteelback.modules.reports.service

import com.aleinx.comsteelback.modules.catalog.model.Product
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.context.Context
import java.io.ByteArrayOutputStream
import kotlin.collections.forEachIndexed

@Service
class PdfReportService(
    private val templateEngine: SpringTemplateEngine
) {
    fun generateReport(templateName: String, data: Map<String, Any>): ByteArray {
        val context = Context()
        context.setVariables(data)
        val htmlContent = templateEngine.process(templateName, context)

        ByteArrayOutputStream().use { outputStream ->
            val builder = PdfRendererBuilder()
            builder.useFastMode()
            builder.withHtmlContent(htmlContent,"")
            builder.toStream(outputStream)
            builder.run()
            return outputStream.toByteArray()
        }

    }
    fun generarExcelInventario(productos: List<Product>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Inventario")

        // Crear Cabecera
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Código")
        headerRow.createCell(1).setCellValue("Producto")
        headerRow.createCell(2).setCellValue("Stock")

        // Llenar datos
        productos.forEachIndexed { index, product ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(product.barcode)
            row.createCell(1).setCellValue(product.name)
            row.createCell(2).setCellValue(product.stockQuantity.toDouble())
        }

        ByteArrayOutputStream().use { ops ->
            workbook.write(ops)
            workbook.close()
            return ops.toByteArray()
        }
    }
}

