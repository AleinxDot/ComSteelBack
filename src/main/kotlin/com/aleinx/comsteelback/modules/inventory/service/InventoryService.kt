package com.aleinx.comsteelback.modules.inventory.service

import com.aleinx.comsteelback.common.enums.MovementType
import com.aleinx.comsteelback.modules.auth.repository.UserRepository
import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import com.aleinx.comsteelback.modules.inventory.dto.StockEntryRequest
import com.aleinx.comsteelback.modules.inventory.model.StockMovement
import com.aleinx.comsteelback.modules.inventory.model.StockMovementDetail
import com.aleinx.comsteelback.modules.inventory.repository.StockMovementRepository
import com.aleinx.comsteelback.modules.inventory.repository.SupplierRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class InventoryService(
    private val movementRepository: StockMovementRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val supplierRepository: SupplierRepository
) {

    @Transactional
    fun registerEntry(request: StockEntryRequest, username: String): String {
        val user = userRepository.findByUsername(username)
            .orElseThrow { RuntimeException("Usuario no encontrado") }

        val supplier = if (request.supplierId != null) {
            supplierRepository.findById(request.supplierId)
                .orElse(null)
        } else null

        val movement = StockMovement(
            type = MovementType.ENTRY,
            reference = request.reference,
            comments = request.comments,
            user = user
        )

        request.items.forEach { item ->
            val product = productRepository.findById(item.productId)
                .orElseThrow { RuntimeException("Producto ID ${item.productId} no encontrado") }

            val currentTotalValue = product.unitPrice.multiply(BigDecimal(product.stockQuantity))
            val incomingTotalValue = item.unitCost.multiply(BigDecimal(item.quantity))

            val newTotalStock = product.stockQuantity + item.quantity

            product.stockQuantity = newTotalStock
            productRepository.save(product)

            movement.addDetail(
                StockMovementDetail(
                    product = product,
                    quantity = item.quantity,
                    unitCost = item.unitCost
                )
            )
        }

        movementRepository.save(movement)
        return "Ingreso registrado correctamente. ID Movimiento: ${movement.id}"
    }
}