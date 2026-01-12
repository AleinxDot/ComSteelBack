package com.aleinx.comsteelback.modules.sales.service

import com.aleinx.comsteelback.common.enums.DocumentType
import com.aleinx.comsteelback.modules.auth.repository.UserRepository
import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import com.aleinx.comsteelback.modules.sales.dto.CreateSaleRequest
import com.aleinx.comsteelback.modules.sales.dto.SaleResponse
import com.aleinx.comsteelback.modules.sales.model.Document
import com.aleinx.comsteelback.modules.sales.model.DocumentDetail
import com.aleinx.comsteelback.modules.sales.repository.ClientRepository
import com.aleinx.comsteelback.modules.sales.repository.DocumentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class SaleService(
    private val documentRepository: DocumentRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository
) {

    @Transactional
    fun registerSale(request: CreateSaleRequest, username: String): SaleResponse {

        val seller = userRepository.findByUsername(username)
            .orElseThrow { RuntimeException("Usuario no encontrado") }

        val client = request.clientId?.let {
            clientRepository.findById(it).orElse(null)
        }

        val document = Document(
            documentType = DocumentType.valueOf(request.type), // BOLETA/FACTURA
            user = seller,
            customer = client,
            subtotal = BigDecimal.ZERO,
            taxAmount = BigDecimal.ZERO,
            totalAmount = BigDecimal.ZERO
        )

        var calculatedTotal = BigDecimal.ZERO

        request.items.forEach { itemRequest ->
            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { RuntimeException("Producto no encontrado ID: ${itemRequest.productId}") }

            if (product.stockQuantity < itemRequest.quantity) {
                throw RuntimeException("Stock insuficiente para: ${product.name}. Disponible: ${product.stockQuantity}")
            }

            product.stockQuantity -= itemRequest.quantity
            productRepository.save(product)

            val itemSubtotal = product.unitPrice.multiply(BigDecimal(itemRequest.quantity))
            calculatedTotal = calculatedTotal.add(itemSubtotal)

            val detail = DocumentDetail(
                product = product,
                quantity = itemRequest.quantity,
                unitPrice = product.unitPrice, // Precio Histórico
                subtotal = itemSubtotal
            )

            document.addDetail(detail)
        }

        // 5. Calcular Totales Finales

        document.totalAmount = calculatedTotal
        document.subtotal = calculatedTotal // Ajustar lógica según tus impuestos
        document.taxAmount = BigDecimal.ZERO // Ajustar si es necesario

        document.documentNumber = "B001-${System.currentTimeMillis()}"

        val savedDoc = documentRepository.save(document)

        return SaleResponse(
            saleId = savedDoc.id!!,
            documentNumber = savedDoc.documentNumber,
            totalAmount = savedDoc.totalAmount
        )
    }
}